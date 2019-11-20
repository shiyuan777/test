///**
// *
// */
//package com.jixunkeji.pay;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Map;
//
///**
// * @author liangzhuohao
// */
//public class AESUtil {
//
//    /**
//     * 密钥算法
//     */
//    private static final String ALGORITHM = "AES";
//    /**
//     * 加解密算法/工作模式/填充方式
//     */
//    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
//    /**
//     * 生成key
//     */
//    private static SecretKeySpec key = new SecretKeySpec(MD5.mD5Encode("sjkdjskdjksjdksjdkjskdjsjdksd111").toLowerCase().getBytes(), ALGORITHM);
//
//    /**
//     * AES加密
//     *
//     * @param data
//     * @return
//     * @throws Exception
//     */
//    public static String encryptData(String data) throws Exception {
//        // 创建密码器
//        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
//        // 初始化
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        return Base64Util.encode(cipher.doFinal(data.getBytes()));
//    }
//
//    /**
//     * AES解密
//     *
//     * @param base64Data
//     * @return
//     * @throws Exception
//     */
//    public static String decryptData(String base64Data) throws Exception {
//        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        return new String(cipher.doFinal(Base64Util.decode(base64Data)));
//    }
//
//    public static void main(String[] args) throws Exception {
//        //解密
////        String req_info="Ih5osM/5IbPfHouVrUmwebd1yAW2Gys91jv006W1237sSi3z022KxHafLIDMrQLYiBttTadgvy2cbx6DnmwDIQ52lPWfo6pAAHt7Q9DjBIpDRQ7JsbEBlomoQP2ZkdNHnWscVYuFEVlItaSlkSlcKLdB4UwMduqDYseFsUUthz6htPeBu987zXS6dKrgIbRwOxt5RfPmk1sf0oVB2yU3UH0Ly8SzBjmN1jrh4qAaUkfH6VkeMJcsZSGchQn2VresxJTbGH++JE1UsXUF3gyYpweyxBPtHoKdaggsIONR20UKNxJYPJLnEOnfQF/Ipmk8/QmTVRK7iqfVLC9EA1Auma0AlKBjZlYqynUlF3y+E2ZzgWMUlvDHZVWDbzp/TcE0q+Ukc7yQ3HBsibDR474SPlLTkCWz1iydXzkVcLqJKamsh76Liv1a0hzu0sI3qasMAfmwU6/q7/N6quq031toO1GxqkVaxBRK7e64gSOx9ArxxVFgZ7WN+JPq2OH/pTKH8ToxHA0rtxN5+aAgZGkXiIOUiHtp4mjpRxqe34WK7C7Nr0DQyOVwsXT2TTegSgWGm34aa//ZYxHedubv2iX+E7K222lptg9IqHlMXBbwKFtKtIcal61+8ciz+sB1FBpqHchC+3whTqWv5ZANiHBzaOhbIbA/mKX2XZ6Cy0iYh+bL/8Y/Hvz/UnMGzor+2anIUeBAGRQmseL4jY+Qic46WLuEhDcarCaO4JgJSAOC+VmsdrER9TRum26PFwTQwtNpxkrKCiO9Gv36Ood5D8hXnLHUH+4nbsek8ouxkCcFXq4Us0mipB3i5ksQpt23LiJm9Ahxyvptp9Q41SytS48NXiz3IxTOqDdknowedZwAtJ/fhBlwiOHD9N+pECXuNBKLaCZcatGycr0/DPELiCF+MIRQ6V60wzaZD74TKRFULd1ljNsoQIAbuGaT40WMDY6a28jBHQ/IXnD4gvSvfeumwQzp3Q9PiPyFtF6JxH7RBRj9/lmQuQozJIPZCaCNVTBfWQOdcFaBnPLN0ZNvzjA93g6jcIxHzkXHmiGfh98vq2E=";
//        String reqInfo = "axq/qVishHzKnnto9JpUdBG2Tn5R9F3AU6TlkYsRRyMqlNL/+cDAje1m/L7rSq4WQqwCaHCdy3E8Bfe+ZRxfQ5zt4rG/07bJQOayQmNrEMD0w8QuVSIoSB3yCJfT0Z5ZZLMG8Mfg55urM7Tp6Jw4/PrWcxvDN/V12KW4SkuJMh03ywudc6kxgVtBAKMfBdqNPWR/mUWIPLe3RbyGU8g88RCNh4Hi1GwSRt5C8CGeC70IiICQGGHL3+WlQfQ5z0+TVVuoBEfN6mqNMKNaqGYvampfgDkWTng4lf2OSy5kXoGa/jVCvLgYA9CFSEL/q8vMGqAyja06xsVt59BR5gAH8HFe0TmU2N4QiRpgYKWOOLbyJ/TB1BGJjxBP4eHf3nlIQ/5VKPowEHxDDkYulSFCQn1Ze1/Bj1OLDvebaqdsB23tDMi1XJG2mf9BcoTPdF6t13IKpXj5jSOwJQJhK9h8YN5Tnojci00rGc0eUBNUCGiSOpBZfUrY+EZRLAehC68Z3JepJJOmdYtjy28ZGMtKHT/XxTxj/guXNgLEBvXrc3aJKDowtKXAvr2dRkLjRmTL5SvMRjqyl1d2Oomt50uQDVZQrv7U8BMUniqyqs4jTENhJLgL6ty0TjpxO28kueMKzjI8MtVY43iUmUyWQR6EQxIDZqjTbJENkGE+Fn9OKwXVjmPD4wEC+JZnrfj+uJt04hnWrQbXkKOwmD/NrhyRbaT41lU8BmLSh8L5GLb4AUYYzXcdwboemUzhvR9yakHJTvbvv9zc/hxzPV7ClKWkqLT1t0OuWPmPf0jj6ITFEQCk+NZVPAZi0ofC+Ri2+AFGTgXZJELNJZJwRlG8+Vc82LbGFBgBt+dHgLWJs1GzPQt1M9d3lAJYf+dG3BkPJ7er7cww0NEWi7mvJOLGIzO7+Ap5O9egjjpUgTuZrWpiDeH/+FhCGT5RCk5EGEkBL7GUU6evFy8LHYKda+NlfTAMMI/XigaB1ldymw8LAzct2UTd94Zh0e5xIp9Ucv/vEc5TMahYadKXWNlXoqrh1p6A6nli8MWQA5jb9sEL3b8KxGyGMqUAtY7Gtsp/0hBcLw0aMtHZXkny9GRtquArvPWaww==";
//
//        String reqInfoXml = AESUtil.decryptData(reqInfo);
//        System.out.println(reqInfoXml);
//        Map<String, String> reqInfoMap = WxPayUtil.xmlToMap(reqInfoXml);
//        System.out.println(reqInfoMap);
//
//    }
//
//}