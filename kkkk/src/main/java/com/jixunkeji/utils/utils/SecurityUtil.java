package com.jixunkeji.utils.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 */
public class SecurityUtil {
    public static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    public static String sKey = "123456789zxcvbnm";
    public static String ivParameter = "123456789zxcvbnm";

    /**
     * 解密
     *
     * @param sSrc
     * @return
     */
    public static String decrypt(String sSrc) {
        if (ValidateUtil.isEmpty(sSrc)) {
            logger.info("无法对空值解密！");
            return sSrc;
        }
        if (ValidateUtil.isMobile(sSrc)) {
            logger.info("无需解密！");
            return sSrc;
        }
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("utf-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (RuntimeException e) {
            return null;
        } catch (Exception ex) {
            logger.info("解密失败！" + sSrc);
            return sSrc;
        }
    }
}
