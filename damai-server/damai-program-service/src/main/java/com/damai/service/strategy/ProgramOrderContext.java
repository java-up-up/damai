package com.damai.service.strategy;

import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目订单上下文
 * @author: 阿星不是程序员
 **/
@Component
public class ProgramOrderContext {
    
    /**
     * ⚠️注意！在升级的大麦pro版本中进行了优化，将不同的订单版本进一步细化拆分，并进行了详细的压测
     * ✅大麦pro升级功能的详细介绍，请看 <a href="https://javaup.chat/damai/damai-pro/release-intro">...</a>
     * ✨如何获取大麦pro项目？，请看 <a href="https://articles.zsxq.com/id_m4d7ni4zwkbq.html">...</a>
     **/
    private static final Map<String,ProgramOrderStrategy> MAP = new HashMap<>(8);
    
    @Autowired
    private List<ProgramOrderStrategy> programOrderStrategyList;
    
    @PostConstruct
    public void init() {
        for (ProgramOrderStrategy programOrderStrategy : programOrderStrategyList) {
            MAP.put(programOrderStrategy.version(), programOrderStrategy);
        }
    }
    
    public ProgramOrderStrategy get(String version){
        return Optional.ofNullable(MAP.get(version)).orElseThrow(() -> 
                new DaMaiFrameException(BaseCode.PROGRAM_ORDER_STRATEGY_NOT_EXIST));
    }
}
