package com.jixunkeji.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jixunkeji.annotation.Login;
import com.jixunkeji.cache.MemberLocalCache;
import com.jixunkeji.entity.Member;
import com.jixunkeji.entity.Share;
import com.jixunkeji.result.ApiRequest;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.service.MemberService;
import com.jixunkeji.service.ShareService;
import com.jixunkeji.utils.utils.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-24
 */
@Api("分享")
@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private ShareService shareService;
    @Autowired
    private MemberService memberService;

    @Login
    @PostMapping(value = "/share")
    @ApiOperation(value = "设备分享", notes = "携带token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态  0 正常 1 永久", required = true, dataType = "String"),
    })
    public ApiResponse share(ApiRequest apiRequest) {

        Member member = MemberLocalCache.get();

        ApiResponse response = ApiResponse.ok();

        int id = apiRequest.getDataParamAsInt("id");
        Long beginTime = apiRequest.getDataParamAsLong("beginTime");
        Long expireTime = apiRequest.getDataParamAsLong("expireTime");
        String phone = apiRequest.getDataParamAsString("phone");
        Integer status = apiRequest.getDataParamAsInt("status");

        Member byPhone = memberService.findByPhone(phone);
        if (byPhone == null) {
            response = ApiResponse.paramError("user does not exist");
            return response;
        }
//        LocalDateTime stringToLocalDateTime = LocalDateTime.parse(expireTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long exprire = System.currentTimeMillis() + expireTime;

        Date expire = new Date(expireTime);
        Date begin = new Date(beginTime);

        if (ValidateUtil.isOneEmpty(id, expireTime)) {
            response = ApiResponse.paramError("id is null");
            return response;
        }

        Share share = new Share();
        share.setPhone(phone);
        share.setCreateTime(new Date());
        share.setExpireTime(expire);
        share.setBeginTime(begin);
        share.setDeviceId(id);
        share.setStatus(status);
        share.setMemberId(member.getMemberId());
        boolean save = shareService.save(share);
        if (!save) {
            response = ApiResponse.paramError("Sharing failure");
        }
        return response;
    }

    @Login
    @PostMapping(value = "/shareHistory")
    @ApiOperation(value = "分享历史", notes = "携带token")
    public ApiResponse shareHistory(ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();
        Member member = MemberLocalCache.get();
        QueryWrapper<Share> ew = new QueryWrapper<>();
        ew.lambda().eq(Share::getMemberId, member.getMemberId());
        List<Share> list = shareService.list(ew);
        response.addValueToData("list", list);
        return response;
    }

    @Login
    @PostMapping(value = "/deleteShare")
    @ApiOperation(value = "删除分享设备", notes = "携带token")
    public ApiResponse deleteShare(ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();
        int id = apiRequest.getDataParamAsInt("id");
        if (ValidateUtil.isOneEmpty(id)) {
            response = ApiResponse.paramError("id不能为空");
            return response;
        }
        Member member = MemberLocalCache.get();
        QueryWrapper<Share> ew = new QueryWrapper<>();
        ew.lambda().eq(Share::getId,id);
        int delete = shareService.getBaseMapper().delete(ew);
        if (delete != 1) {
            response = ApiResponse.paramError("Delete failed");
            return response;
        }
        return response;
    }
}

