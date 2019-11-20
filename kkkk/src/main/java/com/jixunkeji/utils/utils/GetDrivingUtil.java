package com.jixunkeji.utils.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21.
 */
public class GetDrivingUtil {

    public static String requestPost(String strUrl, String param) {
        URL url = null;
        String returnStr = null; // 返回结果定义
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // post方式
            httpURLConnection.connect();
            //POST方法时使用
            byte[] byteParam = param.getBytes("UTF-8");
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.write(byteParam);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }

    /**
     * HTTP的Post请求方式
     * 获取路径规划的一些数据
     * */
    public static Map getDriving(double start_lon, double start_lat, double end_lon, double end_lat) {
        String strUrl = "https://restapi.amap.com/v3/direction/driving";
        String param = "origin=" + start_lon+","+start_lat + "&destination=" + end_lon+","+end_lat + "&output=JSON" + "&key=bf4511bcec00ee43efab6149a608bcf3";
        String returnStr = requestPost(strUrl,param);
        JSONObject jsStr = JSONObject.parseObject(returnStr);
        JSONObject jsonObject = jsStr.getJSONObject("route");
        JSONArray jsonArray = (JSONArray) jsonObject.get("paths");
        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        String distance = (String) jsonObject1.get("distance");
        String duration = (String) jsonObject1.get("duration");
        double distance1 = Double.parseDouble(distance) / 1000;
//        DecimalFormat df = new DecimalFormat("#.00");
        BigDecimal b = new BigDecimal(distance1);
        double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        int duration1 = Integer.parseInt(duration);
        int h= (duration1/3600);
        int m=(duration1%3600)/60;
        String time;
        if (h == 0) {
            if (m == 0){
                time = "不足一分钟";
            }else {
                time = m + "分钟";
            }
        }else {
            time = h + "小时" + m + "分钟";
        }
        Long time1 = DateTime.now().getMillis() + duration1 * 1000;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String time2 = formatter.format(time1);
        Map map = new HashMap();

        map.put("distance",df);
        map.put("duration",time);
        map.put("durationArrived",time2);
        return map;
    }

    public static void main(String[] args) {
        getDriving(116.481028,39.989643,116.434446,39.90816);
    }

    public Map geocode(String location){
        String strUrl = "https://restapi.amap.com/v3/geocode/regeo";
        String param = "radius=300" + "&extensions=all" + "&output=JSON" + "&key=bf4511bcec00ee43efab6149a608bcf3&location=" + location;
        String returnStr = requestPost(strUrl,param);
        JSONObject jsStr = JSONObject.parseObject(returnStr);
        JSONObject jsonObject = jsStr.getJSONObject("regeocode");
//        JSONArray jsonArray = (JSONArray) jsStr.get("regeocode");
//        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        String address = (String) jsonObject.get("formatted_address");
        Map map = new HashMap();
        map.put("address",address);
        return map;
    }

}
