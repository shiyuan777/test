/**
 * Copyright (C)), 2015-2018), MrYe
 * FileName: ResponseEnum
 * Author:   MrYe
 * Date:     2018/5/26 下午3:16
 * Description: 返回值枚举类
 * History:
 * <author>                <time>          <version>          <desc>
 * MrYe        2018/5/26 下午3:16         1.0.0         〈返回值枚举类〉
 */
package com.jixunkeji.result;

/**
 * 〈一句话功能简述〉<br>
 * 〈返回值枚举类〉
 *
 * @author MrYe
 * @create 2018/5/26
 * @since 1.0.0
 */
public enum ResponseEnum {

    SUCCESS(200, "SUCCESS"),
    PARAM_ERROR(400, "Request parameter error"),
    ACCESS_FREQUENTLY(406, "访问频繁"),
    ACCESS_COUNT_OVERRUN(405, "访问次数过多"),
    DISABLE_IP_REQUEST(407, "该IP异常，已被查封，请联系管理员解封！"),
    NOT_LOGGED_IN(4001, "未登录"),
    USER_INFO_EXCEPTION(4002, "用户信息异常"),
    MESSAGE_READ(4003, "消息已读"),

    ENTITY_NOT_EXIST(4004, "实体不存在"),
    NOT_BIND_PHONE(4005, "未绑定手机号"),
    MEMBER_HAS_LOGIN(4006, "当前账号在其他设备已登陆"),

    SERVER_ERROR(500, "Server busy"),
    REQUEST_ERROR(501, "Request error"),
    SQL_ERROR(502, "SQL错误"),
    PARAMETER_EXCEPTION(5001, "请求参数异常"),
    NOT_ONLINE(5002, "未上线"),
    NO_OPENID(5003, "no_openid"),
    DRIVER_NO_CAR(5004, "司机未注册代驾"),
    NO_WECHAT_USER_INFO(5005, "未获取到微信登陆信息"),
    DAI_SHEN_HE(5006, "司机信息正在审核中"),
    SHENHE_WEITONGGUO(5008, "司机信息审核未通过"),
    DONG_JIE(5009, "司机信息已冻结"),
    DRIVER_REGISTRATION_EXCEPTION(5010, "司机注册异常"),


    COUPON_NOT_EXIST(6001, "优惠劵不存在"),
    COUPON_UNAVAILABLE(6002, "优惠券不可用"),
    COUPON_NOT_THE_SAME_TYPE(6003, "该优惠券不可用于此订单! 不是同一个类型"),
    COUPON_NO_DISCOUNT_SET(6004, "该优惠券未设置折扣，无法使用！请联系当地运营商！"),
    COUPON_INSUFFICIENT_AMOUNT_OF_ACTIVITY(6005, "公司活动金额不足，优惠券不可用！请联系当地运营商！"),
    PAY_INSUFFICIENT_BALANCE(6006, " 账户余额不足"),
    PAY_LIQUIDATED_DAMAGES_FOR_DRIVERSTARTGO(6007, " 司机已出发超过3分钟, 支付取消订单违约金！"),
    PAY_LIQUIDATED_DAMAGES_FOR_DRIVERARRIVERSTART(6008, " 司机已达到起点超过5分钟, 支付取消订单违约金！"),

    ORDER_START(6999, "订单已开始行程"),
    ORDER_NOT_EXIST(7000, "订单不存在"),
    ORDER_ABNORMAL(7001, "订单异常"),
    ORDER_PAID(7002, " 订单已支付！"),
    ORDER_CANCEL(7003, "订单已取消！"),
    ORDER_TYPE_ERROR(7004, "订单类型错误"),
    ORDER_NO_SUCCESS(7005, "司机有未完成订单"),
    ORDER_TIME_OUT(7006, "订单超时"),
    ORDER_TAKEN_AWAY(7007, "订单被抢走了"),
    ORDER_CANNOT_BE_CANCELLED(7020, "订单无法取消"),

    NO_START(7008, "状态修改有误，应修改为司机开始出发"),
    NO_ARRIVED_START(7009, "状态修改有误，应修改为司机到达起点"),
    NO_ORDER_START(7010, "状态修改有误，应修改为订单开始"),

    DRIVER_NOT_START_ADDRESS(7011, "司机未到达起点或者未开始等待"),
    DRIVER_NOT_END_ADDRESS(7013, "司机暂未到达终点"),
    DRIVER_NOT_START(7012, "司机暂未开始出发"),

    TIXIAN_TIME_OUT(8001, "当前时间不在提现范围内"),

    ZX_LINE_INFO(9001, "当前城市未开通，请切换起点城市"),
    ZX_LINE_INFO_NULL(9002, "当前城市无线路！"),
    ZX_LINE_UP_DOWNADDRESS_YES(9100, "该地点可直接上车！"),
    ZX_LINE_UP_DOWNADDRESS_NO(9101, "需要重新选择站点！"),

    /**
     * [黑名单]用户！请联系客服热线：
     */
    BLACKLIST_USER(9000, "400-100-3768");

    private Integer code;
    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}