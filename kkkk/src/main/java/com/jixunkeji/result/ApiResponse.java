package com.jixunkeji.result;

import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: caikai
 * @description: com.zdkj.dto
 * @date:2019/3/13
 */
public class ApiResponse {
    private Integer code;

    private String message;

    private Boolean isSuccess;

    private Map<String, Object> data;

    public Integer getCode() {
        if (null == code) {
            return isSuccess ? ResponseEnum.SUCCESS.getCode() : code;
        }
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public ApiResponse(Integer code, String message, Boolean isSuccess, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public ApiResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.isSuccess = true;
    }


    public ApiResponse addObjectToData(Object obj){
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        if(obj instanceof HashMap){
            this.setData((HashMap)obj);
        }else{
            Map<String, Object> map = new HashMap<>();
            BeanUtils.copyProperties(obj, map);
            this.setData(map);
        }

       /* String jsonString = JSON.toJSONString(obj);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        Set<String> set  = jsonObject.keySet();
        for(String key : set){
            this.data.put(key, jsonObject.get(key));
        }*/

      /*  for (Field f : obj.getClass().getDeclaredFields()) {
            if ("this$0".equals(f.getName())) {
                continue;
            }
            f.setAccessible(true);
            this.data.put(f.getName(), f.get(obj));
        }*/
        return this;
    }

    public ApiResponse addValueToData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }

    public ApiResponse(){

    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", isSuccess=" + isSuccess +
                ", data=" + data +
                '}';
    }

    /**
     * 请求成功
     * @return
     */
    public static ApiResponse ok() {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(ResponseEnum.SUCCESS.getMsg());
        response.setIsSuccess(true);
        response.setData(new HashMap<>());
        return response;
    }

    public static ApiResponse ok(String message) {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMessage(message);
        response.setIsSuccess(true);
        response.setData(new HashMap<>());
        return response;
    }

    /**
     * 请求参数有误
     * @return
     */
    public static ApiResponse paramError() {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.PARAM_ERROR.getCode());
        response.setMessage(ResponseEnum.PARAM_ERROR.getMsg());
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }

    public static ApiResponse paramError(String msg) {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.PARAM_ERROR.getCode());
        response.setMessage(msg);
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }

    /**
     * 请求失败，某个实体不存在
     * @param msg
     * @return
     */
    public static ApiResponse entityNotExist(String msg) {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.ENTITY_NOT_EXIST.getCode());
        response.setMessage(msg);
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }

    /**
     * 请求失败，验证不通过
     * @param msg
     * @return
     */
    public static ApiResponse requestError(String msg) {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.REQUEST_ERROR.getCode());
        response.setMessage(msg);
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }

    /**
     * 请求失败，服务器抛异常
     * @return
     */
    public static ApiResponse serverError() {
        ApiResponse response = new ApiResponse();
        response.setCode(ResponseEnum.SERVER_ERROR.getCode());
        response.setMessage(ResponseEnum.SERVER_ERROR.getMsg());
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }

    /**
     * 自定义code  and  msg
     * @param code
     * @param msg
     * @return
     */
    public static ApiResponse error(Integer code, String msg) {
        ApiResponse response = new ApiResponse();
        response.setCode(code);
        response.setMessage(msg);
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }

    public static ApiResponse error(ResponseEnum responseEnum) {
        ApiResponse response = new ApiResponse();
        response.setCode(responseEnum.getCode());
        response.setMessage(responseEnum.getMsg());
        response.setIsSuccess(false);
        response.setData(new HashMap<>());
        return response;
    }




}
