package com.jixunkeji.utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: caikai
 * @description: com.zdkj.utils
 * @date:2019/3/14
 */
public class CommonUtil {
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isEmpty(Set<?> set) {
        if (set == null || set.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isEmpty(Long number) {
        if (number == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Object[] arr) {
        if (arr == null || arr.length==0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str) || "undefined".equals(str)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isEmpty(Integer number) {
        if (number == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isNotEmpty(List<?> list){
        return !isEmpty(list);
    }

    public static boolean isNotEmpty(Set<?> set){
        return !isEmpty(set);
    }

    public static boolean isNotEmpty(Object[] arr){
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Integer number){
        return !isEmpty(number);
    }

    public static boolean isNotEmpty(Long number){
        return !isEmpty(number);
    }

    public static boolean isNotEmpty(Map<?, ?> map){
        return !isEmpty(map);
    }


    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
