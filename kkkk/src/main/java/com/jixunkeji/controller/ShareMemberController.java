package com.jixunkeji.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jixunkeji.annotation.Login;
import com.jixunkeji.cache.MemberLocalCache;
import com.jixunkeji.entity.Member;
import com.jixunkeji.entity.ShareMember;
import com.jixunkeji.result.ApiRequest;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.service.MemberService;
import com.jixunkeji.service.ShareMemberService;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shiyuan
 * @since 201919
 */
@Api("分享添加人")
@Slf4j
@RestController
@RequestMapping("/shareMember")
public class ShareMemberController {
    @Autowired
    private ShareMemberService shareMemberService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value="添加分享人员", notes="token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "手机",required = true,dataType = "String"),
            @ApiImplicitParam(name = "nickName",value = "名称",required = true,dataType = "String")
    })
    @Login
    @PostMapping(value = "/add")
    public ApiResponse add(ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        Member member = MemberLocalCache.get();
        ShareMember shareMember = new ShareMember();

        String email = apiRequest.getDataParamAsString("email");
        String nickName = apiRequest.getDataParamAsString("nickName");
        LambdaQueryWrapper<Member> lambda = new QueryWrapper<Member>().lambda();
        lambda.eq(Member::getPhone, email).eq(Member::getNickName, nickName);
        Member one = memberService.getOne(lambda);
        if (one == null) {
            response = ApiResponse.paramError("The person you shared does not exist");
            return response;
        }
        if (ValidateUtil.isOneEmpty(email, nickName)) {
            response = ApiResponse.paramError();
            return response;
        }
        shareMember.setEmail(email);
        shareMember.setNickName(nickName);
        shareMember.setMemberId(member.getMemberId());
        boolean save = shareMemberService.save(shareMember);
        if (!save) {
            response = ApiResponse.paramError("Add failed, please try again");
            return response;
        }
        return response;
    }

    @ApiOperation(value="删除分享人员", notes="token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "手机",required = true,dataType = "String")
    })
    @Login
    @PostMapping(value = "/delete")
    public ApiResponse delete(ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        int id = apiRequest.getDataParamAsInt("id");

        if (ValidateUtil.isOneEmpty(id)) {
            response = ApiResponse.paramError();
            return response;
        }
        QueryWrapper<ShareMember> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ShareMember::getId, id);
        int delete = shareMemberService.getBaseMapper().delete(wrapper);
        if (delete != 1) {
            response = ApiResponse.paramError("Delete failed, please try again");
            return response;
        }
        return response;
    }
    @ApiOperation(value="查询分享列表", notes="token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_pageNo",value = "手机",required = true,dataType = "String"),
            @ApiImplicitParam(name = "_pageSize",value = "手机",required = true,dataType = "String")
    })
    @Login
    @PostMapping(value = "/list")
    public ApiResponse list(ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();
        Member member = MemberLocalCache.get();
        Long pageNo = apiRequest.getDataParamAsLong("_pageNo");
        Long pageSize = apiRequest.getDataParamAsLong("_pageSize");
        Page<ShareMember> page = new Page<>(pageNo,pageSize);
        QueryWrapper<ShareMember> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ShareMember::getMemberId, member.getMemberId());
        IPage<ShareMember> page1 = shareMemberService.page(page, wrapper);
        response.addValueToData("page", page1);
        return response;
    }
}

