package com.damai.namefactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 线程工厂抽象基类
 * @author: 阿星不是程序员
 **/
public abstract class AbstractNameThreadFactory implements ThreadFactory {

    protected static final AtomicLong POOL_NUM = new AtomicLong(1);
    private final ThreadGroup group;
    private final AtomicLong threadNum = new AtomicLong(1);
    private String namePrefix = "";
    
    /**
     * 线程工厂命令
     */
    public AbstractNameThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = getNamePrefix() + "--thread--";
    }

    /**
     * 子类实现获取线程池名称的前缀
     *
     * @return String
     */
    public abstract String getNamePrefix();

    /**
     * 将线程池工厂中设置线程名进行重写
     * 例子:子类重写的namePrefix--thread--2(每个线程池中线程的数量)
     */
    @Override
    public Thread newThread(Runnable r) {
        String name = namePrefix + threadNum.getAndIncrement();
        Thread t = new Thread(group, r, name, 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
