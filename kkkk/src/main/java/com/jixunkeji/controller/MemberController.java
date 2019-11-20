package com.jixunkeji.controller;


import com.jixunkeji.entity.Member;
import com.jixunkeji.redis.StringRedisTemplate;
import com.jixunkeji.result.ApiRequest;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.result.ResponseEnum;
import com.jixunkeji.service.MemberService;
import com.jixunkeji.utils.kits.Md5Kit;
import com.jixunkeji.utils.mail.MailOperation;
import com.jixunkeji.utils.utils.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

import static com.jixunkeji.utils.mail.MailOperation.getCode;


/**
 * 用户
 */
@Api("用户")
@Slf4j
@RestController
@RequestMapping(value = "/api/member")
public class MemberController {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MemberService memberService;

    @ApiOperation(value="用户注册", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "nickName",value = "名称",required = true,dataType = "String"),
            @ApiImplicitParam(name = "role",value = "角色 1使用者（顾客）、2管理者（锁的主人）、3跑腿（送单员）",required = true,dataType = "String")
    })
    @PostMapping(value = "/register")
    public ApiResponse register( ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();

        Member member = new Member();

//        member.setGender(apiRequest.getDataParamAsInt("gender"));
        member.setNickName(apiRequest.getDataParamAsString("nickName"));
        String phone = apiRequest.getDataParamAsString("phone");
        String password = apiRequest.getDataParamAsString("password");
        if (ValidateUtil.isOneEmpty(phone, password)) {
            response = ApiResponse.paramError();
            return response;
        }
        member.setPhone(phone);
        member.setPassword(Md5Kit.MD5(password));
        member.setRole(apiRequest.getDataParamAsInt("role"));

        Member byPhone = memberService.findByPhone(member.getPhone());
        if (byPhone != null) {
            response = ApiResponse.paramError("Account already exists");
        }

        boolean register = memberService.register(member);
        if (!register) {
            response = ApiResponse.requestError("login has failed");
            return response;
        }

        return response;
    }

    /**
     * 判断是否登陆
     * @param apiRequest
     * @return
     */
    @ApiOperation(value="判断是否登陆", notes="携带token")
    @PostMapping(value = "/isLogin")
    public ApiResponse isLogin( ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();
        Date now = new Date();

        boolean isLogin = memberService.isLogin(apiRequest.getToken());

        if (!isLogin) {
            response = ApiResponse.error(ResponseEnum.NOT_LOGGED_IN);
            return response;
        }

        return response;
    }

    /**
     * 注销
     */
    @ApiOperation(value="注销", notes="携带token")
    @PostMapping(value = "/logout")
    public ApiResponse logout(ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();
        boolean isLogout = memberService.logout(apiRequest.getToken());
        if (!isLogout) {
            response = ApiResponse.requestError("Logout failure");
        }
        return response;
    }

    /**
     * 登录
     * @param apiRequest
     * @return
     */
    @ApiOperation(value="登录", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "String")
    })
    @PostMapping(value = "/login")
    public ApiResponse login(ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();

        String phone = apiRequest.getDataParamAsString("phone");
        String password = apiRequest.getDataParamAsString("password");

        if (ValidateUtil.isOneEmpty(phone, password)) {
            return ApiResponse.paramError();
        }

        Member member = memberService.loginByPhoneAndPassword(phone, Md5Kit.MD5(password));

        boolean isLogin = memberService.login(member);
        if (!isLogin) {
            response = ApiResponse.requestError("Landing failed");
            return response;
        }

        response.addValueToData("memberId", member.getMemberId());
        response.addValueToData("token", member.getToken());
        response.addValueToData("role", member.getRole());
        return response;
    }

    @ApiOperation(value="发送邮件", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mail",value = "邮箱",required = true,dataType = "String")
    })
    @PostMapping(value = "/sendMail")
    public ApiResponse sendMail(ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();

        String mail = apiRequest.getDataParamAsString("mail");

        if (ValidateUtil.isOneEmpty(mail)) {
            return ApiResponse.paramError();
        }

        MailOperation operation = new MailOperation();
        String user = "15889406893@163.com";
        String password = "123456xx";
        String host = "smtp.163.com";
        String from = "15889406893@163.com";
        String to = mail;// 收件人
        String subject = "验证码";
        //邮箱内容
        StringBuffer sb = new StringBuffer();
        String yzm = getCode();
        sb.append("<!DOCTYPE>"+"<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                + "<div style='width:950px;font-family:arial;'>欢迎使用NET微活动，您的注册码为：<br/><h2 style='color:green'>"+yzm+"</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>深圳飓讯科技有限公司</div>"
                +"</div>");
        try {
            String res = operation.sendMail(user, password, host, from, to,
                    subject, sb.toString());
            System.out.println(res);
        } catch (Exception e) {
            return response = ApiResponse.paramError("发送失败,请重试");
        }
        response.addValueToData("code", yzm);
        return response;
    }
}
