package com.jixunkeji.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jixunkeji.entity.Member;
import com.jixunkeji.mapper.MemberMapper;
import com.jixunkeji.redis.HashRedisTemplate;
import com.jixunkeji.redis.RedisKeyEnum;
import com.jixunkeji.redis.StringRedisTemplate;
import com.jixunkeji.service.MemberService;
import com.jixunkeji.utils.kits.Md5Kit;
import com.jixunkeji.utils.utils.ValidateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 乘客用户表 服务实现类
 * </p>
 *
 * @author caikai
 * @since 2019-04-03
 */

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    HashRedisTemplate hashRedisTemplate;

    @Override
    public Member findByPhone(String phone) {
        return memberMapper.findByPhone(phone);
    }

    @Override
    public Member findByWeappOpenid(String weappOpenid) {
        return memberMapper.findByWeappOpenid(weappOpenid);
    }

    @Override
    public Member findByOpenid(String openid) {
        return memberMapper.findByOpenid(openid);
    }

    @Override
    public boolean register(Member member) {


        member.setStatus(1); //正常状态
        member.setCreateTime(new Date());
        member.setUpdateTime(new Date());
        member.setLastLoginTime(new Date());

        boolean isSave = this.save(member);

        return isSave;
    }


    /**
     * 登陆，登陆相关信息存redis
     * @param member
     */
    @Override
    public boolean login(Member member) {
        if (member == null) {
            logger.info("member or secret  is null");
            return false;
        }

        Date now = new Date();
        String token = member.getToken();
        if (ValidateUtil.isEmpty(token)) {
            token = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
        }
        member.setToken(token);
        member.setLastLoginTime(now);

        String key = Md5Kit.MD5( token);

        //只存memebrId
        stringRedisTemplate.set(RedisKeyEnum.LOGIN.buildKey(key), member.getMemberId(), RedisKeyEnum.LOGIN.getExpiredTime());
        //每次登陆都创建新的secret，用于登陆设备限制
//        String secret = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
//        member.setSecret(secret);

        Map<String,String> map =  new HashMap<>();
        map.put("memberId", String.valueOf(member.getMemberId()));
//        map.put("secret", secret);
        hashRedisTemplate.hmset(key, map);

        return this.updateById(member);
    }


    @Override
    public boolean isLogin(String token) {
        String key = Md5Kit.MD5( token);
        String value = stringRedisTemplate.get(RedisKeyEnum.LOGIN.buildKey(key));


        if (ValidateUtil.isEmpty(value)) {
           return false;
        }
        return  true;
    }

    @Override
    public boolean logout(String token) {
        String key = Md5Kit.MD5( token);
        //redis存memberId
        String memberId = stringRedisTemplate.get(RedisKeyEnum.LOGIN.buildKey(key));

       /* Member member = memberMapper.selectById(memberId);
        if (member != null) {
            *//*不把openId清空,因为member中的deviceType可以由不同客户端改变，
            **所以openid可以看哪些用户是用小程序登陆过的
            **//*
           *//* if(deviceType == DeviceEnum.SMALL_PROGRAM.getType()){
                member.setWeappOpenid("");
            }else if (deviceType == DeviceEnum.WECHAT.getType()){
                member.setOpenid("");
            }*//*
            member.setToken("");

            return stringRedisTemplate.del(key) && this.updateById(member);
        }*/
        return stringRedisTemplate.del(key);
    }

    //试一下xml写法的
    @Override
    public Member findTestMember(){
        return memberMapper.findTestMember();
    }

    @Override
    public Member loginByPhoneAndPassword(String phone, String password) {
        return memberMapper.loginByPhoneAndPassword(phone, password);
    }

    @Override
    public Member findByMemberId(String memberId) {
        return memberMapper.findByMemberId(memberId);
    }


    public Member chansferMember(JSONObject jsonObject){
        Member member = new Member();

        if (jsonObject.containsKey("nickname")) {
            member.setNickName(jsonObject.getString("nickname"));
        }
        if (jsonObject.containsKey("openid")) {
            member.setNickName(jsonObject.getString("openid"));
        }
        if (jsonObject.containsKey("sex")) {
            member.setGender(Integer.valueOf(jsonObject.getString("sex")));
        }

        //TODO unionid/subscribe
        return member;
    }






}
