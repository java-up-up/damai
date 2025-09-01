package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 接口返回code码
 * @author: 阿星不是程序员
 **/
public enum BaseCode {
    /**
     * 基础code码
     * */
    SUCCESS(0, "OK"),
    
    SYSTEM_ERROR(-1,"系统异常，请稍后重试"),
    
    UID_WORK_ID_ERROR(500,"uid_work_id设置失败"),
    
    NAME_PASSWORD_ERROR(501,"账号名或登录密码不正确"),
    
    INITIALIZE_HANDLER_STRATEGY_NOT_EXIST(502,"初始化操作策略不存在"),
    
    VERIFY_CAPTCHA_ID_NOT_EXIST(503,"校验验证码id不存在"),
    
    VERIFY_CAPTCHA_EMPTY(504,"二次校验验证码的参数为空"),
    
    POINT_JSON_EMPTY(505,"点坐标为空"),
    
    CAPTCHA_TOKEN_JSON_EMPTY(506,"验证码token为空"),
    
    LOAD_BALANCER_NOT_EXIST(507,"负载均衡器不存在"),
    
    SERVER_LIST_NOT_EXIST(508,"服务列表不存在"),
    
    TWO_PASSWORDS_DIFFERENT(509,"两次输入的密码不同"),
    
    MESSAGE_CONSUMER_NOT_EXIST(510,"messageConsumer实现不存在"),
    
    REDIS_STREAM_CONSUMER_TYPE_NOT_EXIST(511,"redisStream消费方式不存在"),
    
    CHANNEL_DATA_NOT_EXIST(512,"没有找到ChannelData"),
    
    OPERATION_IS_TOO_FREQUENT_PLEASE_TRY_AGAIN_LATER(513,"操作太频繁，请稍后再试"),
    
    THREAD_INTERRUPTED(514,"线程中断错误"),
    
    EXECUTE_TIME_OUT(515,"执行超时"),
    
    LOGIN_USER_NOT_EXIST(516,"用户没有登录"),
    
    ONLY_SIGNATURE_ACCESS_IS_ALLOWED(517,"只允许签名访问"),
    
    MOBILE_ERROR_COUNT_TOO_MANY(518,"手机号输入错误次数过多，请稍后重试"),
    
    EMAIL_ERROR_COUNT_TOO_MANY(519,"邮箱输入错误次数过多，请稍后重试"),
    
    RSA_SIGN_ERROR(10000,"rsa签名验证失败"),
    
    RSA_DECRYPT_ERROR(10001,"rsa解密失败"),
    
    RSA_ENCRYPT_ERROR(10002,"rsa加密失败"),
    
    AES_ERROR(10003,"aes验证失败"),
    
    CUSTOM_ENABLED_RULE_EMPTY(10004,"customEnabledRule为空"),
    
    I_LOAD_BALANCER_RULE_EMPTY(10005,"iLoadBalancer为空"),
    
    SERVER_LIST_EMPTY(10006,"serverList为空，请检查灰度代码或者服务是否异常下线"),
    
    CHANNEL_DATA(10050,"渠道数据为空"),
    
    ARGUMENT_EMPTY(10051,"基础参数为空"),
    
    HEAD_ARGUMENT_EMPTY(10052,"请求头基础参数为空"),
    
    CODE_EMPTY(10053,"code参数为空"),
    
    PARAMETER_ERROR(10054,"参数验证异常"),
    
    TOKEN_EXPIRE(10055,"token过期"),
    
    API_RULE_TRIGGER(10056,"用户调用太过频繁，稍后再试"),
    
    API_RULE_TIME_WINDOW_INTERSECT(10057,"已有的时间范围已经包含"),
    
    USER_AUTHENTICATION(10058,"用户已认证"),
    
    SUBMIT_FREQUENT(20000,"执行频繁，请稍后再试"),
    
    USER_MOBILE_AND_EMAIL_NOT_EXIST(20001,"用户手机和邮箱需要选择一个"),
    
    USER_MOBILE_EMPTY(20002,"用户手机号不存在"),
    USER_EXIST(20003,"用户已存在"),
    
    DEPARTMENT_STRATEGY(30002,"departmentStrategy策略实现未找到"),
    
    PRODUCT_STOCK_NOT_ENOUGH(30003,"库存不足"),
    
    JOB_INFO_NOT_EXIST(30004,"jobInfo不存在"),
    
    MESSAGE_PLATFORM_NOT_EXIST(30005,"消息平台不存在"),
    
    GENERATE_STRATEGY_NOT_EXIST(30006,"执行的生成策略不存在"),
    
    REJECT_STRATEGY_NOT_EXIST(30007,"执行的拒绝策略不存在"),
    
    PROGRAM_GROUP_NOT_EXIST(30009,"节目分组不存在"),
    
    PROGRAM_NOT_EXIST(40000,"节目不存在"),
    
    SEAT_NOT_EXIST(40001,"座位不存在"),
    
    SEAT_LOCK(40002,"座位已锁定"),
    
    SEAT_SOLD(40003,"座位已售卖"),
    
    SEAT_OCCUPY(40004,"座位已占用"),
    
    TICKET_CATEGORY_NOT_EXIST(40005,"如果不选座位，那么票档必须要存在"),
    
    TICKET_COUNT_NOT_EXIST(40006,"如果不选座位，那么购买票数量必须要存在"),
    
    TICKET_COUNT_ERROR(40007,"如果不选座位，购买票数量必须大于0"),
    
    PRICE_ERROR(40008,"入参的订单价格大于座位规定价格"),
    
    RPC_RESULT_DATA_EMPTY(40009,"rpc服务返回数据为空"),
    
    TICKET_CATEGORY_NOT_EXIST_V2(40010,"票档不存在"),
    
    TICKET_REMAIN_NUMBER_NOT_SUFFICIENT(40011,"余票数量不足"),
    
    COMPOSITE_NOT_EXIST(40012,"通用验证不存在"),
    
    USER_REGISTER_FREQUENCY(40013,"用户注册频繁"),
    
    PROGRAM_SHOW_TIME_NOT_EXIST(40014,"节目演出时间不存在"),
    
    ORDER_NOT_EXIST(40015,"订单不存在"),
    
    ORDER_CANCEL(40016,"订单已取消"),
    
    ORDER_PAY(40017,"订单已支付"),
    
    ORDER_REFUND(40018,"订单已退单"),
    
    ORDER_CANAL_ERROR(40019,"订单取消失败"),
    
    TICKET_USER_ORDER_NOT_EXIST(40020,"购票人订单不存在"),
    
    TICKET_USER_ORDER_CANCEL(40021,"购票人订单已取消"),
    
    TICKET_USER_ORDER_PAY(40022,"购票人订单已支付"),
    
    TICKET_USER_ORDER_REFUND(40023,"购票人订单已退单"),
    
    LOCK_SEAT_LIST_EMPTY(40024,"锁定的座位为空"),
    
    ORDER_EXIST(40025,"订单存在"),
    
    SEAT_ID_EMPTY(40026,"座位id为空"),
    
    SEAT_TICKET_CATEGORY_ID_EMPTY(40027,"座位的票档id为空"),
    
    SEAT_ROW_CODE_EMPTY(40028,"座位的rowCode为空"),
    
    SEAT_COL_CODE_EMPTY(40029,"座位的colCode为空"),
    
    SEAT_PRICE_EMPTY(40030,"座位价格为空"),
    
    SEAT_IS_NOT_NOT_SOLD(40031,"座位不是未售卖"),
    
    CAN_NOT_CANCEL(40032,"订单不是未支付状态不能取消"),
    
    DELAY_QUEUE_CLIENT_NOT_EXIST(50001,"延迟队列客户端不存在"),
    
    DELAY_QUEUE_MESSAGE_NOT_EXIST(50002,"延迟队列消息不存在"),
    
    SEAT_IS_EXIST(50003,"该节目下座位以存在"),
    
    START_DATE_TIME_NOT_EXIST(50004,"开始时间为空"),
    
    END_DATE_TIME_NOT_EXIST(50005,"结束时间为空"),
    
    PROGRAM_NOT_ALLOW_CHOOSE_SEAT(50006,"此节目不允许选择座位"),
    
    PARAMETERS_CANNOT_BE_EMPTY(50007,"参数不允许都为空"),
    
    PER_ORDER_PURCHASE_COUNT_OVER_LIMIT(50008,"该订单购买数量大于限制"),
    
    PER_ACCOUNT_PURCHASE_COUNT_OVER_LIMIT(50009,"该账户购买数量大于限制"),
    
    SEAT_UPDATE_REL_COUNT_NOT_EQUAL_PRESET_COUNT(50010,"座位数据更新数量与预设数量不一致"),
    
    PROGRAM_ORDER_STRATEGY_NOT_EXIST(50011,"创建订单策略不存在"),
    
    USER_LOG_IN_STATUS_ERROR(60001,"用户不是登录状态"),
    
    USER_LOG_IN(60002,"用户已登录"),
    
    USER_ID_EMPTY(60003,"用户id为空"),
    
    USER_EMPTY(60004,"用户不存在"),
    
    TICKET_USER_EXIST(60005,"此购票人已存在"),
    
    TICKET_USER_EMPTY(60006,"此购票人不存在"),
    
    NOT_FOUND(60007,"not found api %s %s"),
    
    GENERATE_RSA_SIGN_ERROR(60008,"生成res签名验证失败"),
    
    PAY_ERROR(60009,"支付异常"),
    
    PAY_STRATEGY_NOT_EXIST(60010,"此支付策略不存在"),
    
    PAY_BILL_IS_NOT_NO_PAY(60011,"此账单不是未支付状态"),
    
    ALIPAY_TRADE_STATUS_NOT_EXIST(60012,"支付宝支付状态未知"),
    
    PAY_PRICE_NOT_EQUAL_ORDER_PRICE(60013,"支付金额和订单金额不一致"),
    
    OPERATE_ORDER_STATUS_NOT_PERMIT(60014,"操作订单状态不允许"),
    
    PAY_CHANNEL_NOT_EXIST(60015,"支付方式不存在"),
    
    PAY_BILL_NOT_EXIST(60016,"账单不存在"),
    
    PAY_TRADE_CHECK_ERROR(60017,"支付状态检查错误"),
    
    UPDATE_TICKET_CATEGORY_COUNT_NOT_CORRECT(60018,"更新票档数量不正确"),
    
    GET_USER_AND_TICKET_USER_ERROR(60019,"获取用户和购票人信息错误"),
    
    TICKET_USER_ID_REPEAT(60020,"购票人id重复"),
    
    TICKET_USER_COUNT_UNEQUAL_SEAT_COUNT(60021,"购票人数量和要购买的座位数量不相等"),
    
    REFUND_ERROR(60022,"退款异常"),
    
    PAY_BILL_IS_NOT_PAY_STATUS(60023,"账单不是已支付状态"),
    
    REFUND_AMOUNT_GREATER_THAN_PAY_AMOUNT(60024,"退款金额大于支付金额"),
    
    ORDER_NUMBER_NOT_EXIST(70000,"order_number的值不存在"),
    
    USER_ID_NOT_EXIST(70001,"user_id的值不存在"),
    
    USER_EMAIL_NOT_EXIST(70002,"用户邮箱不存在"),
    
    NOT_FOUND_GENE(70003,"没有找到基因"),
    
    SERVICE_LOCK_FAIL(70004,"分布式锁加锁失败"),
    ;
    
    private final Integer code;
    
    private String msg = "";
    
    BaseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public String getMsg() {
        return this.msg == null ? "" : this.msg;
    }
    
    public static String getMsg(Integer code) {
        for (BaseCode re : BaseCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }
    
    public static BaseCode getRc(Integer code) {
        for (BaseCode re : BaseCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
