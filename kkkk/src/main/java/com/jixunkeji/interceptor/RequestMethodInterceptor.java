package com.jixunkeji.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.jixunkeji.result.ApiRequest;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.utils.utils.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: shiyuan
 * @description:
 * @date:2019/9/04
 */
@Component
@Aspect
public class RequestMethodInterceptor {
    Logger logger = LoggerFactory.getLogger(RequestMethodInterceptor.class);

    //expression = ...and @annotation(org.springframework.web.bind.annotation.RequestMapping)
    @Around("execution(* com.jixunkeji.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

//        String appVersion = request.getHeader("appVersion");
//        String appType = request.getHeader("appType");
//        String deviceType = request.getHeader("deviceType");
//        String deviceNo = request.getHeader("deviceNo");
//        String deviceName = request.getHeader("deviceName");
        String secret = request.getHeader("secret");
        String token = request.getHeader("token");

        String requestParam = this.readData(request);
        //TODO 是否是json格式的字符串
        Map<String, Object> data = null;
        try {
            data = JSONObject.parseObject(requestParam, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.requestError("请求参数格式不正确");
        }

        logger.info("request ip is {}, secret:{}, token:{}, playload:{}",
                IpUtil.getRealIp(request), secret, token, requestParam);

        //封装请求参数
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setData(data);

//        apiRequest.setAppVersion(appVersion);
//        apiRequest.setAppType(NumberUtils.isDigits(appType) ? Integer.valueOf(appType) : 0);
//        apiRequest.setDeviceType(NumberUtils.isDigits(deviceType) ? Integer.valueOf(deviceType) : 0);
//
//        apiRequest.setDeviceNo(deviceNo);
//        apiRequest.setDeviceName(deviceName);
        apiRequest.setSecret(secret);
        apiRequest.setToken(token);

        Signature s = joinPoint.getSignature();
        MethodSignature ms = (MethodSignature)s;
        Method method = ms.getMethod();


        //记录请求controller方法的时间
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed(new Object[]{apiRequest});
        long endTime = System.currentTimeMillis();
        logger.info("request result is {}, spend {} ms", result, endTime - startTime);


        return result;
    }


    private String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder sb;
            br = request.getReader();

            String line = br.readLine();
            if (line != null) {
                sb = new StringBuilder();
                sb.append(line);
            } else {
                return "";
            }

            while ((line = br.readLine()) != null) {
                sb.append('\n').append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (br != null) {
                try {br.close();} catch (IOException e) { e.printStackTrace();}
            }
        }
    }


}
