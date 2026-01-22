package com.damai.service.strategy;

import com.damai.dto.ProgramOrderCreateDto;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目订单策略
 * @author: 阿星不是程序员
 **/
public interface ProgramOrderStrategy {
    
    /**
     * 创建订单
     * @param programOrderCreateDto 订单参数
     * @return 订单编号
     * */
    String createOrder(ProgramOrderCreateDto programOrderCreateDto);
    
    /**
     * 获取版本号
     * @return 版本号
     * */
    String version();
}
