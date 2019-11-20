package com.jixunkeji.result;


import com.jixunkeji.utils.utils.CommonUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class ApiRequest implements Serializable {

    private String secret;
    private String token;

    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Object getDataParam(String name) {
        if (data == null || name == null) {
            return null;
        }
        return data.get(name);
    }

    public String getDataParamAsString(String name) {
        return getDataParam(name) != null ? getDataParam(name).toString() : null;
    }

    public String getDataParamAsString(String name, String defaultValue) {
        return getDataParam(name) != null ? getDataParam(name).toString() : null;
    }

    public Integer getDataParamAsInt(String name) {
        return CommonUtil.isNotEmpty(getDataParamAsString(name)) ?
                new BigDecimal(getDataParamAsString(name)).intValue() : null;
    }

    public Integer getDataParamAsInt(String name, int defaultValue) {
        Integer i = defaultValue;
        try{
            i = CommonUtil.isNotEmpty(getDataParamAsString(name)) ?
                    new BigDecimal(getDataParamAsString(name)).intValue() : defaultValue;
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }

    public Long getDataParamAsLong(String name) {
        Long i = null;
        try{
            i = CommonUtil.isNotEmpty(getDataParamAsString(name)) ? Long.valueOf(getDataParamAsString(name)) : null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }

    public Long getDataParamAsLong(String name, Long defaultValue) {
        Long i = defaultValue;
        try{
            i = CommonUtil.isNotEmpty(getDataParamAsString(name)) ? Long.valueOf(getDataParamAsString(name)) : null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return i;
    }
}
