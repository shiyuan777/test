package com.jixunkeji.utils.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: caikai
 * @description: com.zdkj.utils
 * @date:2019/4/4
 */
public class SmsUtil {

    private static final String url = "http://120.55.197.77:1210/Services/MsgSend.asmx/SendMsg";
    private static final String userCode = "CYQCCF";
    private static final String userPass = "Cycx0768";


    /**
     * 发送短信
     * @param phone
     * @param content
     * @return
     */
    public static String send(String phone, String content) {

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("userCode", userCode));
        nvps.add(new BasicNameValuePair("userPass", userPass));
        nvps.add(new BasicNameValuePair("DesNo", phone));
        nvps.add(new BasicNameValuePair("Msg", content));
        nvps.add(new BasicNameValuePair("Channel", "0"));

        String post =  HttpUtil.requestPost(url, nvps);
        return post;
    }

}
