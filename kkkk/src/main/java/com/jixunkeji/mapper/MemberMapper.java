package com.jixunkeji.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jixunkeji.entity.Member;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户表 Mapper 接口
 * @author shiyuan
 * @since 2019-9-04
 */
public interface MemberMapper extends BaseMapper<Member> {
    @Select("select * from member where phone = #{phone} limit 1")
    Member findByPhone(String phone);

    @Select("select * from member where weapp_openid = #{weappOpenid} limit 1")
    Member findByWeappOpenid(String weappOpenid);

    @Select("select * from member where openid = #{openid} limit 1")
    Member findByOpenid(String Openid);

    Member findTestMember();

    @Select("select * from member where phone = #{phone} and password = #{password} limit 1")
    Member loginByPhoneAndPassword(@Param("phone") String phone,@Param("password") String password);

    @Select("select * from member where member_id = #{memberId}")
    Member findByMemberId(String memberId);
}
