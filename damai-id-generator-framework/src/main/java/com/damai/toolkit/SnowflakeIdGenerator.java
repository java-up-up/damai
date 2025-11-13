package com.damai.toolkit;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 雪花算法
 * @author: 阿星不是程序员
 **/
@Slf4j
public class SnowflakeIdGenerator {
    
    private static final long BASIS_TIME = 1288834974657L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    
    private final long workerId;
    
    
    private final long datacenterId;
   
    private long sequence = 0L;
   
    private long lastTimestamp = -1L;
    
    private InetAddress inetAddress;
    
    public SnowflakeIdGenerator(WorkDataCenterId workDataCenterId) {
        if (Objects.nonNull(workDataCenterId.getDataCenterId())) {
            this.workerId = workDataCenterId.getWorkId();
            this.datacenterId = workDataCenterId.getDataCenterId();
        }else {
            this.datacenterId = getDatacenterId(maxDatacenterId);
            workerId = getMaxWorkerId(datacenterId, maxWorkerId);
        }
    }

    public SnowflakeIdGenerator(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
        initLog();
    }

    private void initLog() {
        if (log.isDebugEnabled()) {
            log.debug("Initialization SnowflakeIdGenerator datacenterId:" + this.datacenterId + " workerId:" + this.workerId);
        }
    }
    
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        Assert.isFalse(workerId > maxWorkerId || workerId < 0,
            String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        Assert.isFalse(datacenterId > maxDatacenterId || datacenterId < 0,
            String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        initLog();
    }
    
    protected long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            mpid.append(name.split("@")[0]);
        }
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }
    
    protected long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            if (null == this.inetAddress) {
                this.inetAddress = InetAddress.getLocalHost();
            }
            NetworkInterface network = NetworkInterface.getByInetAddress(this.inetAddress);
            if (null == network) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                }
            }
        } catch (Exception e) {
            log.warn(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }
    
    public long getBase(){
        int five = 5;
        long timestamp = timeGen();
        //闰秒
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= five) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }
        }
        
        if (lastTimestamp == timestamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 同一毫秒的序列数已经达到最大
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒内，序列号置为 1 - 3 随机数
            sequence = ThreadLocalRandom.current().nextLong(1, 3);
        }
        
        lastTimestamp = timestamp;
        
        return timestamp;
    }
    
    public synchronized long nextId() {
        long timestamp = getBase();
        
        return ((timestamp - BASIS_TIME) << timestampLeftShift)
            | (datacenterId << datacenterIdShift)
            | (workerId << workerIdShift)
            | sequence;
    }
    
    public synchronized long getOrderNumber(long userId,long tableCount) {
        long timestamp = getBase();
        long sequenceShift = log2N(tableCount);
        return ((timestamp - BASIS_TIME) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | (sequence << sequenceShift)
                | (userId % tableCount);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return SystemClock.now();
    }
    
    public static long parseIdTimestamp(long id) {
        return (id>>22)+ BASIS_TIME;
    }
    
    public long log2N(long count) {
        return (long)(Math.log(count)/ Math.log(2));
    }
    
    public long getMaxWorkerId() {
        return maxWorkerId;
    }
    
    public long getMaxDatacenterId() {
        return maxDatacenterId;
    }
}