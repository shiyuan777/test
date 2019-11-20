package com.jixunkeji.redis;

import java.text.MessageFormat;

/**
 * @author: caikai
 * @description: com.zdkj.constant.redis 全局统一缓存key
 * @date:2019/3/12
 */
public enum RedisKeyEnum {
    /**
     * 短信验证码次数key
     */
    SMS_CODE_COUNT("SMS_CODE_COUNT_{0}", RedisTTLConstant.ONE_MINUTE * 3),
    /**
     * 验证码登陆缓存key SMS_LOGIN_13660xxxxx
     */
    SMS_LOGIN("SMS_LOGIN_{0}", RedisTTLConstant.HALF_HOUR),
    /**
     * 验证码绑定手机号缓存key
     */
    SMS_BIN_PHONE("SMS_REGISTER_{0}", RedisTTLConstant.HALF_HOUR),
    /**
     * 验证码忘记密码key
     */
    SMS_FORGET_PASSWORD("SMS_FORGET_PASSWORD_{0}", RedisTTLConstant.HALF_HOUR),
    /**
     * 验证码更改手机号key
     */
    SMS_CHANGE_PHONE("CHANGE_PHONE_{0}", RedisTTLConstant.HALF_HOUR),
    /**
     * 登陆信息key，一个月
     */
    LOGIN("LOGIN_{0}", RedisTTLConstant.ONE_MONTH),
    /**
     * 小程序openid和sessionKey缓存，一个月
     */
    WEAPP_LOGIN("WEAPP_LOGIN_{0}", RedisTTLConstant.ONE_MONTH),
    /**
     * APP微信openid和sessionKey缓存，一个月
     */
    APP_WECHAT_LOGIN("APP_WECHAT_LOGIN_{0}", RedisTTLConstant.ONE_MONTH);








    private String key;

    private int expiredTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    RedisKeyEnum(String key, int expiredTime) {
        this.key = key;
        this.expiredTime = expiredTime;
    }

    public String buildKey(Object... keys) {
        return MessageFormat.format(this.getKey(), keys);

    }

}
