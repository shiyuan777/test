/**
 * Copyright (C), 2018-2019
 * FileName: OrderStatusEnum
 * Author:   MrYe
 * Date:     2019/3/15 10:28 AM
 * Description: 订单状态
 * History:
 * <author>          <time>          <version>          <desc>
 * MrYe        2019/3/15 10:28 AM         1.0.0         〈订单状态〉
 */
package com.jixunkeji.constant;

/**
 * 〈一句话功能简述〉<br> 
 * 〈订单状态〉
 *
 */
public enum DeviceEnum {



    ANDROID_APP(1, "安卓APP"),
    ANDROID_MEMBER(11, "安卓乘客端"),
    ANDROID_DRIVER(12, "安卓司机端"),

    iOS_APP(2, "苹果APP"),
    iOS_MEMBER(21, "苹果乘客端"),
    iOS_DRIVER(22, "苹果司机端"),

    WECHAT(3, "微信端"),
    WECHAT_ANDROID(31, "微信安卓端"),
    WECHAT_iOS(3, "微信苹果端"),

    SMALL_PROGRAM(4,"小程序端"),
    SMALL_PROGRAM_ANDROID(41,"小程序安卓端"),
    SMALL_PROGRAM_iOS(42,"小程序苹果端");


    private Integer type;
    private String text;

    DeviceEnum(Integer type, String text) {
        this.type = type;
        this.text = text;
    }

    public Integer getType() {
        return type;
    }

    public String getText() {
        return text;
    }

}