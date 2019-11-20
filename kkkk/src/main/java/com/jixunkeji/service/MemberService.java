package com.jixunkeji.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jixunkeji.entity.Member;

/**
 * @author: shiyuan
 */

public interface MemberService extends IService<Member> {

    Member findByPhone(String phone);

    Member findByWeappOpenid(String weappOpenid);

    Member findByOpenid(String openid);

    boolean register(Member member);

    boolean login(Member member);

    boolean isLogin(String token);

    boolean logout(String token);

    Member findTestMember();

    Member loginByPhoneAndPassword(String phone, String password);

    Member findByMemberId(String memberId);
}
