package com.jixunkeji.utils.utils;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

/**

 */
public class WechatUtil {

    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    //算法名
    public static final String KEY_ALGORITHM = "AES";
    //加解密算法/模式/填充方式
    //可以任意选择，为了方便后面与iOS端的加密解密，采用与其相同的模式与填充方式
    //ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    //小程序登录凭证code校验
    public static String CODE_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
    //获取网页授权access_token：
    private static String HTML_ACCESS_TOKEN_URL ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=NEWCODE&grant_type=authorization_code";
    //通过网页授权access_token和openid拉取用户信息
    private static String USERINFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPEN_ID&lang=zh_CN";





    /**
     * 小程序通过code获取openId和sessionKey
     * @param code
     * @return
     */
    public static JSONObject code2Session(String code, String appId, String secret){
        String url = CODE_SESSION_URL.replace("APPID", "appid").
                replace("SECRET", "secret").replace("JSCODE", "jsCode");
        logger.info("小程序授权拿到openId和sessionKey,start------------------>>>>>>>>>>>url:{}", url);
        JSONObject jsonObject = HttpUtil.httpsRequest(url, "GET", null);
        logger.info("小程序授权拿到openId和sessionKey,end------------------>>>>>>>>>>>{}", jsonObject);
        return jsonObject;
    }

    /**
     * 网页授权登陆
     * @param code
     * @param appId
     * @param appsecret
     * @return
     */
    public static JSONObject getAccessToken(String code, String appId, String appsecret){
        String url = HTML_ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appsecret).replace("NEWCODE", code);
        logger.info("获取授权调用凭证start------------------>>>>>>>>>>>url:{}", url);
        JSONObject jsonObject =  HttpUtil.httpsRequest(url, "GET", null);
        if(jsonObject.get("errcode") != null){
            logger.error("getHtmlAccessToken失败" + jsonObject.toString());
            return null;
        }
        logger.info("获取获取授权调用凭证end------------------>>>>>>>>>>>{}", jsonObject);
        return jsonObject;
    }

    /**
     * 网页授权获取用户基本信息
     * @param accessToken
     * @param openId
     * @return
     */
    public static JSONObject getUserInfo(String accessToken, String openId) {
        String url = USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPEN_ID", openId);
        logger.info("获取用户基本信息start------------------>>>>>>>>>>>url:{}", url);
        JSONObject jsonObject =  HttpUtil.httpsRequest(url, "GET", null);
        logger.info("获取用户基本信息end------------------>>>>>>>>>>>{}", jsonObject);
        return jsonObject;
    }


    //解密
    public static byte[] decrypt(String encryptedData, String keyBytes, String iv){
        try{
            Key key = convertToKey(Base64.decode(keyBytes));
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            //设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Base64.decode(iv)));
            return cipher.doFinal(Base64.decode(encryptedData));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    //转化成JAVA的密钥格式
    public static Key convertToKey(byte[] keyBytes) throws Exception {
        SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        return secretKey;
    }


















  /*  public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }*/



}
