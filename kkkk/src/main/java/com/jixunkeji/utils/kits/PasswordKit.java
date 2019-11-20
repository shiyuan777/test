package com.jixunkeji.utils.kits;

public class PasswordKit {

    /**
     * @param deviceSn 设备序列号
	 * @param key	密钥
	 * @param index 密码序号
	 * @param time	充电时间
	 */
    public static String getPwd(String deviceSn, String key, Integer index, Long time) {
        try {
            if(index > 20) {
                index = 1;	//重置密码
            }
            String pwdIndex = index.toString();
            if (index <= 9) {
                pwdIndex = "0" + pwdIndex;
            }
            String md5 = key + deviceSn + pwdIndex;
            md5 = Md5Kit.MD5(md5);
            System.out.println("str="+key + deviceSn + pwdIndex+" md5="+md5);
            String temp1 = md5.substring(md5.length() - 3);
            Integer val1 = Integer.valueOf(temp1, 16);
            int pwd1 = ((val1 >> 9) & 0x7) % 5 + 1;
            int pwd2 = ((val1 >> 6) & 0x7) % 5 + 1;
            int pwd3 = ((val1 >> 3) & 0x7) % 5 + 1;
            int pwd4 = (val1 & 0x7) % 5 + 1;
            String pwd = "" + pwd1 + pwd2 + pwd3 + pwd4;
            // 遇到这几个密码重新累计
            if (pwd.equals("1111") || pwd.equals("2222") || pwd.equals("3333") || pwd.equals("4444")
                    || pwd.equals("5555")) {
                index = index + 1;
                return getPwd(deviceSn,key,index,time);
            }
            if(time == 60) {
                return 2 + pwd;
            }else if(time == 120) {
                return 3 + pwd;
            }else if(time == 180) {
                return 4 + pwd;
            }else if(time == 720){
                return 5 + pwd;
            }else if(time == 1440) {
                return 5 + pwd;
            }else {
                return 1 + pwd;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        String s1 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 20, 30L);
        String s2 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 19, 30L);
        String s3 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 18, 30L);
        String s4 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 17, 30L);
        String s5 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 16, 30L);
        String s6 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 15, 30L);
        String s7 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 14, 30L);
        String s8 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 13, 30L);
        String s9 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 12, 30L);
        String s10 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 11, 30L);
        String s11 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 10, 30L);
        String s12 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 9, 30L);
        String s13 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 8, 30L);
        String s14 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 7, 30L);
        String s15 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 6, 30L);
        String s16 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 5, 30L);
        String s17 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 4, 30L);
        String s18 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 3, 30L);
        String s19 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 2, 30L);
        String s20 = PasswordKit.getPwd("ZJZ0000456", "ZJZ168", 1, 30L);
        System.out.println("20密码:"+s1);
        System.out.println("19密码:"+s2);
        System.out.println("18密码:"+s3);
        System.out.println("17密码:"+s4);
        System.out.println("16密码:"+s5);
        System.out.println("15密码:"+s6);
        System.out.println("14密码:"+s7);
        System.out.println("13密码:"+s8);
        System.out.println("12密码:"+s9);
        System.out.println("11密码:"+s10);
        System.out.println("10密码:"+s11);
        System.out.println("9密码:"+s12);
        System.out.println("8密码:"+s13);
        System.out.println("7密码:"+s14);
        System.out.println("6密码:"+s15);
        System.out.println("5密码:"+s16);
        System.out.println("4密码:"+s17);
        System.out.println("3密码:"+s18);
        System.out.println("2密码:"+s19);
        System.out.println("1密码:"+s20);

    }
}
