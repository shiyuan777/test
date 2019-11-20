package com.jixunkeji.interceptor;

import com.alibaba.fastjson.JSON;
import com.jixunkeji.annotation.Login;
import com.jixunkeji.cache.MemberLocalCache;
import com.jixunkeji.entity.Member;
import com.jixunkeji.redis.HashRedisTemplate;
import com.jixunkeji.redis.StringRedisTemplate;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.result.ResponseEnum;
import com.jixunkeji.service.MemberService;
import com.jixunkeji.utils.kits.Md5Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private HashRedisTemplate hashRedisTemplate;
    @Autowired
    private MemberService memberService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        if (handler instanceof  HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            boolean isLoginAnnotation = handlerMethod.getMethod().isAnnotationPresent(Login.class);

            String token = request.getHeader("token");
//            String deviceTypeStr = request.getHeader("deviceType");
//            String secret = request.getHeader("secret");

//            Integer deviceType = NumberUtils.isDigits(deviceTypeStr) ? Integer.valueOf(deviceTypeStr) : 0;

            if (isLoginAnnotation) {
                response.setContentType("application/json;charset=utf-8");

                String key = Md5Kit.MD5( token);
                //redis没登陆信息
                if (!hashRedisTemplate.exists(key)) {
                    ApiResponse apiResponse = ApiResponse.error(ResponseEnum.NOT_LOGGED_IN);
                    response.getWriter().append(JSON.toJSONString(apiResponse));
                    return false;
                }

                // 限制同一账号只能在同一个设备登陆
//                if (deviceType.equals(DeviceEnum.ANDROID_MEMBER.getType()) || deviceType.equals(DeviceEnum.iOS_MEMBER.getType())) {
//                    String redisSecret = hashRedisTemplate.hget(key, "secret");
//                    if(ValidateUtil.isEmpty(secret) || secret.equals(redisSecret)){
//                        ApiResponse apiResponse = ApiResponse.error(ResponseEnum.MEMBER_HAS_LOGIN);
//                        response.getWriter().append(JSON.toJSONString(apiResponse));
//                        return false;
//                    }
//                }


                String memberId = hashRedisTemplate.hget(key, "memberId");
                //最新的member信息
//                Member member = memberService.findByMemberId(memberId);
                Member member = memberService.getById(memberId);
                if (member == null || member.getMemberId() <= 0) {
                    ApiResponse apiResponse = ApiResponse.error(ResponseEnum.NOT_LOGGED_IN);
                    response.getWriter().append(JSON.toJSONString(apiResponse));
                    return false;
                }

                //存放在本地线程内存中
                MemberLocalCache.set(member);

            }
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        MemberLocalCache.remove();

    }

}
