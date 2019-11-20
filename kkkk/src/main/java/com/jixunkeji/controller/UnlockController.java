package com.jixunkeji.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jixunkeji.annotation.Login;
import com.jixunkeji.cache.MemberLocalCache;
import com.jixunkeji.entity.Device;
import com.jixunkeji.entity.Member;
import com.jixunkeji.entity.Unlock;
import com.jixunkeji.result.ApiRequest;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.service.DeviceService;
import com.jixunkeji.service.UnlockService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shiyuan
 * @since 2019-11-20
 */
@Api(tags = "开锁")
@Slf4j
@RestController
@RequestMapping("/unlock")
public class UnlockController {
    @Autowired
    private UnlockService unlockService;
    @Autowired
    private DeviceService deviceService;

    @ApiOperation(value="增加开锁记录", notes="token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "锁id",required = true,dataType = "String")
    })
    @Login
    @PostMapping(value = "/add")
    public ApiResponse add(ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        Member member = MemberLocalCache.get();
        int id = apiRequest.getDataParamAsInt("id");

        if (ValidateUtil.isOneEmpty(id)) {
            response = ApiResponse.paramError("id is null");
            return response;
        }
        Device byId = deviceService.getById(id);
        if (byId == null) {
            response = ApiResponse.paramError("device is not exist");
            return response;
        }
        Unlock unlock = new Unlock();
        unlock.setDeviceId(id);
        unlock.setCreateTime(LocalDateTime.now());
        unlock.setDeviceName(byId.getDeviceName());
        unlock.setMemberId(member.getMemberId());
        unlockService.save(unlock);
        return response;
    }


    @Login
    @ApiOperation(value="开锁记录", notes="token")
    @PostMapping(value = "/get")
    public ApiResponse get(ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        Member member = MemberLocalCache.get();
        List<Unlock> unlocks = unlockService.getBaseMapper().selectList(new QueryWrapper<Unlock>().lambda().eq(Unlock::getMemberId, member.getMemberId()));
        response.addValueToData("list", unlocks);
        return response;
    }
}

