package com.damai.core;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分布式锁 业务名管理
 * @author: 阿星不是程序员
 **/
public class DistributedLockConstants {
    
    /**
     * 注册用户
     * */
    public final static String REGISTER_USER_LOCK = "d_register_user_lock";
    
    /**
     * 登录用户
     * */
    public final static String LOGIN_USER_LOCK = "d_login_user_lock";
    
    /**
     * 节目
     * */
    public final static String PROGRAM_LOCK = "d_program_lock";
    
    /**
     * 节目分组
     * */
    public final static String PROGRAM_GROUP_LOCK = "d_program_group_lock";
    
    /**
     * 查看节目
     * */
    public final static String GET_PROGRAM_LOCK = "d_get_program_lock";
    
    /**
     * 节目演出时间
     * */
    public final static String PROGRAM_SHOW_TIME_LOCK = "d_program_show_time_lock";
    
    /**
     * 查看节目演出时间
     * */
    public final static String GET_PROGRAM_SHOW_TIME_LOCK = "d_get_program_show_time_lock";
    
    /**
     * 座位
     * */
    public final static String SEAT_LOCK = "d_seat_lock";
    
    /**
     * 查看座位
     * */
    public final static String GET_SEAT_LOCK = "d_get_seat_lock";
    
    /**
     * 票档类型
     * */
    public final static String TICKET_CATEGORY_LOCK = "d_ticket_category_lock";
    
    /**
     * 查看票档类型
     * */
    public final static String GET_TICKET_CATEGORY_LOCK = "d_get_ticket_category_lock";
    
    /**
     * 节目类型
     * */
    public final static String PROGRAM_CATEGORY_LOCK = "d_program_category_lock";
    
    /**
     * 余票数量
     * */
    public final static String REMAIN_NUMBER_LOCK = "d_remain_number_lock";
    
    /**
     * 查看余票数量
     * */
    public final static String GET_REMAIN_NUMBER_LOCK = "d_get_remain_number_lock";
    
    
    /**
     * 修改订单状态
     * */
    public final static String UPDATE_ORDER_STATUS_LOCK = "update_order_status_lock";
    
    
    /**
     * 交易状态检查
     * */
    public final static String TRADE_CHECK = "d_trade_check_lock";
    
    /**
     * 节目服务订单创建V1
     * */
    public final static String PROGRAM_ORDER_CREATE_V1 = "d_program_order_create_v1_lock";
    
    /**
     * 节目服务订单创建V2
     * */
    public final static String PROGRAM_ORDER_CREATE_V2 = "d_program_order_create_v2_lock";
    
    /**
     * 节目服务订单创建V3
     * */
    public final static String PROGRAM_ORDER_CREATE_V3 = "d_program_order_create_v3_lock";
    
    /**
     * 节目服务订单创建V4
     * */
    public final static String PROGRAM_ORDER_CREATE_V4 = "d_program_order_create_v4_lock";
    
    /**
     * 支付服务的通用支付
     * */
    public final static String COMMON_PAY = "d_common_pay_lock";
}
