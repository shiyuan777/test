package com.jixunkeji.utils.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微信发送消息到公众号
 *
 * @author shiyuan
 * @date 2019年7月16日
 * 说明:
 */
@Scope("singleton")
@Component(value = "messagePush")
public class MessagePush {

    private static final Logger logger = LoggerFactory.getLogger(MessagePush.class);
    /*******************************微信参数**************************************/
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String APPID = "wxe91dc7d6b2a939be";
    private static final String APP_SECRET = "7f08682b676e03a3066abcf3f4cf23f7";
    private static final String SENDOUT_POSTURL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /*******************************微信获取 access_token**************************************/
    private static String access_token() {
        String parm = "grant_type=client_credential&appid=" + APPID + "&secret=" + APP_SECRET;
        String result = HttpRequest.sendGet(TOKEN_URL, parm);
        logger.info("result-------------------->"+ result);
        String accessToken = JSONObject.parseObject(result).getString("access_token");
        logger.info("accessToken-------------------->"+ accessToken);
        return accessToken;
    }

    private static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(128);

    private static void messagePush(Map<String, Object> map) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String postUrl = SENDOUT_POSTURL + access_token();
                String sendPost = HttpRequest.sendPost(postUrl, JSON.toJSONString(map));
                logger.info(map.get("openId") + "：" + sendPost);
//                JSONObject result = JSONObject.parseObject(sendPost);
            }
        });
    }

    /*******************************模板ID**************************************/
    private static final String TEMPLATE_TKCG = "FnNWl02ZoaowZiTT6fIjLN_UqtB9UeR4-scQKBaqHyI";


    /*******************************模板内容字体颜色，不填默认为黑色**************************************/
    /**
     * 黑色
     */
    private static final String COLOR_BLACK = "#173177";

    /**
     * test
     */
    public static void messageTest(String touser) {
        //模板内容
        final String FIRST = "test";
        //备注
        final String REMARK = "如有疑问请联系在线客服。";

        /*******************************模板数据**************************************/
        //内容
        Map<String, Object> first = new HashMap<>(2);
        first.put("value", FIRST);
        first.put("color", COLOR_BLACK);
        //登录名
        Map<String, Object> orderProductPrice = new HashMap<>(2);
        orderProductPrice.put("value", "test");
        orderProductPrice.put("color", COLOR_BLACK);
        //设备
        Map<String, Object> orderProductName = new HashMap<>(2);
        orderProductName.put("value", "test");
        orderProductName.put("color", COLOR_BLACK);
        //说明
        Map<String, Object> remark = new HashMap<>(2);
        remark.put("value", REMARK);
        remark.put("color", COLOR_BLACK);
        Map<String, Object> data = new HashMap<>(4);
        data.put("first", first);
        data.put("keyword1", orderProductPrice);
        data.put("keyword2", orderProductName);
        data.put("remark", remark);
        /*******************************跳小程序所需数据，不需跳小程序可不用传该数据**************************************/
        Map<String, Object> miniprogram = new HashMap<>(2);
        //所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
        miniprogram.put("appid", "");
        //所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
        miniprogram.put("pagepath", "");
        /*******************************消息参数**************************************/
        Map<String, Object> map = new HashMap<>(6);
        //openId
        map.put("openId", touser);
        //模板id
        map.put("template_id", TEMPLATE_TKCG);
        //模板跳转链接（海外帐号没有跳转能力）
        map.put("url", "");
        //跳小程序所需数据，不需跳小程序可不用传该数据
        map.put("miniprogram", miniprogram);
        //模板数据
        map.put("data", data);
        messagePush(map);
    }

    public static void main(String[] args) {
        messageTest("o_ATf53JdrkmONdNgRiPLw2ZC-NA");
    }
}
