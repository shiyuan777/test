package com.jixunkeji.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户
 * </p>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Model<Member> implements Serializable {


    private static final long serialVersionUID = 7797794888794975262L;
    /**
     * 主键,用户ID
     */
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 1使用者（顾客）、2管理者（锁的主人）、3跑腿（送单员）
     */
    private Integer role;


    private String password;

    /**
     * 登陆检验token
     */
    private String token;

    /**
     * 用于登陆设备校验
     */
    private String secret;

    /**
     * unionid
     */
    private String unionid;

    /**
     * openid
     */
    private String gzhOpenid;

    /**
     * 小程序openid
     */
    private String xcxOpenid;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别(1:男;0:女;2:未知)
     */
    private Integer gender;

    /**
     * 会员等级 1-普通会员
     */
    private Integer level;

    /**
     * 1.正常  2.黑名单
     */
    private Integer status;


    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 真实头像
     */
    private String realHeadUrl;

    /**
     * 身份证正面
     */
    private String identityFront;

    /**
     * 身份证反面
     */
    private String identityBack;

    @TableField(exist = false)
    private String distance;
}
