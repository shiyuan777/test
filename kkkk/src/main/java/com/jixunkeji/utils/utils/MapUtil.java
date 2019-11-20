package com.jixunkeji.utils.utils;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 */
@Configuration
@Slf4j
public class MapUtil {

    @Autowired
    RestTemplateBuilder builder;
    @Bean
    public RestTemplate restTemplate(){
        return builder.build();
    }


    private static final String KEY = "FKYBZ-ILBRI-PPNGY-5WAUJ-5WXUQ-GXFPV";

    /**
     * 根据地址获取坐标
     * @param address
     * @return
     */
    public static String getJWDByAddress(String address) {

        String uri = "https://apis.map.qq.com/ws/geocoder/v1/?address="+address+"&key="+KEY;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String strbody =exchange.getBody();
        log.info("【根据地址获取坐标】返回值为:"+strbody);
//        if (!DevEnum.STATUS.getCode().equals(JSONObject.fromObject(strbody).getInt(DevEnum.STATUS.getMsg()))){
//            return null;
//        }
        JSONObject location = JSONObject.fromObject(strbody).getJSONObject("result").getJSONObject("location");
        log.info(address+"->的坐标为:"+location);
        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");
        return lat+","+lng;
    }

    /**
     * 根据地址获取坐标
     * @param location
     * @return
     */
    public static String getAddressByJWD(String location) {

        String uri = "https://apis.map.qq.com/ws/geocoder/v1/?location="+location+"&key="+KEY;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String strbody =exchange.getBody();
        log.info("【根据地址获取坐标】返回值为:"+strbody);
        String address = JSONObject.fromObject(strbody).getJSONObject("result").getString("address");
        log.info(location+"->的位置为:"+address);
        return address;
    }

    /**
     * 根据经纬度获取两地距离
     * @param jwd1
     * @return
     */
    public static String getDurationByJWD(String jwd1,String jwd2) {

        String uri = "https://apis.map.qq.com/ws/distance/v1/?mode=driving&from="+jwd1+"&to="+jwd2+"&key="+KEY;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String strbody =exchange.getBody();
        log.info("【根据经纬度获取两地距离】的返回值为:"+strbody);
        log.info("出发地:"+jwd1+",目的地:"+jwd2);
        JSONArray jsonArray = JSONObject.fromObject(strbody).getJSONObject("result").getJSONArray("elements");
        System.out.println(jsonArray);

        StringBuffer stringBuffer = new StringBuffer();
        for (Object json:jsonArray){
            stringBuffer.append(JSONObject.fromObject(json).get("distance")).append(";");
        }

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
//        System.out.println(getJWDByAddress("北京市-北京市-东城区摸"));
        System.out.println(getDurationByJWD(getJWDByAddress("罗湖区东门中路2110号"),getJWDByAddress("广东省深圳市宝安区前进二路固戍地铁站")));

    }

}

