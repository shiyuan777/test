package com.jixunkeji.utils.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @TODO:
 * @Author:shiyuan
 * @Data :2019/11/20 15:12
 * @Version:1.0
 **/
public class MailAuthenticator extends Authenticator {
    public static String USERNAME = "";
    public static String PASSWORD = "";

    public MailAuthenticator() {
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
    }
}
