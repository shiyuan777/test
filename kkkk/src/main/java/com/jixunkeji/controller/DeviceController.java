package com.jixunkeji.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jixunkeji.annotation.Login;
import com.jixunkeji.cache.MemberLocalCache;
import com.jixunkeji.entity.Device;
import com.jixunkeji.entity.Member;
import com.jixunkeji.entity.Share;
import com.jixunkeji.result.ApiRequest;
import com.jixunkeji.result.ApiResponse;
import com.jixunkeji.service.DeviceService;
import com.jixunkeji.service.ShareService;
import com.jixunkeji.utils.utils.ValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shiyuan
 * @since 201923
 */
@Api("设备")
@Slf4j
@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ShareService shareService;

    @Login
    @PostMapping(value = "/add")
    @ApiOperation(value="添加设备", notes="携带token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceName",value = "设备名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "appid",value = "appid",required = true,dataType = "String"),
            @ApiImplicitParam(name = "mac",value = "mac",required = true,dataType = "String"),
            @ApiImplicitParam(name = "uid",value = "uid",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "password",required = true,dataType = "String")

    })
    public ApiResponse add( ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();

        Member member = MemberLocalCache.get();

        String deviceName = apiRequest.getDataParamAsString("deviceName");
        String appid = apiRequest.getDataParamAsString("appid");
        String mac = apiRequest.getDataParamAsString("mac");
        String password = apiRequest.getDataParamAsString("password");
        String uid = apiRequest.getDataParamAsString("uid");

        if (ValidateUtil.isOneEmpty(deviceName, appid, mac)) {
            return ApiResponse.paramError();
        }
        Device device = new Device();
        device.setAppid(appid);
        device.setDeviceName(deviceName);
        device.setMac(mac);
        device.setCreateTime(new Date());
        device.setMemberId(member.getMemberId());
        device.setUid(uid);
        device.setPassword(password);

        boolean save = deviceService.save(device);
        if (!save) {
            response = ApiResponse.paramError("Add failure");
        }
        response.addValueToData("id", device.getId());
        return response;
    }

    @Login
    @PostMapping(value = "/query")
    @ApiOperation(value="查询设备列表", notes="携带token")
    public ApiResponse query( ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();
        List<Integer> list = new ArrayList<>();
        Member member = MemberLocalCache.get();
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member.getMemberId());
        List<Device> devices = deviceService.getBaseMapper().selectByMap(map);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = formatter.format(date);
        List<Share> shares = shareService.selectShare(member.getPhone(),format);

//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime parse = LocalDateTime.parse(now.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (!shares.isEmpty()) {
            shares.forEach(share -> {
                list.add(share.getDeviceId());
            });
        }

        List<Device> share = new ArrayList<>();

        if (!list.isEmpty()) {
            share = deviceService.selectByIdSet(list);
        }

        response.addValueToData("devices", devices);
        response.addValueToData("share", share);
        return response;
    }

    @Login
    @PostMapping(value = "/delete")
    @ApiOperation(value="删除设备", notes="携带token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "集合id",required = true,dataType = "String")
    })
    public ApiResponse delete( ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        int id = apiRequest.getDataParamAsInt("id");

        int i = deviceService.getBaseMapper().deleteById(id);
        if (i != 1) {
            response = ApiResponse.paramError("Delete failed");
        }
        return response;
    }

    @Login
    @PostMapping(value = "/update")
    @ApiOperation(value="修改设备", notes="携带token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "appid",value = "appid",required = false,dataType = "String"),
            @ApiImplicitParam(name = "deviceName",value = "设备名称",required = false,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "密码",required = false,dataType = "String")

    })
    public ApiResponse update( ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        int id = apiRequest.getDataParamAsInt("id");
        String deviceName = apiRequest.getDataParamAsString("deviceName");
        String appid = apiRequest.getDataParamAsString("appid");
        String password = apiRequest.getDataParamAsString("password");

        if (ValidateUtil.isEmpty(id)) {
            response = ApiResponse.paramError("id is null");
        }
        Device device = new Device();
        device.setId(id);
        device.setPassword(password);
        if (!StringUtils.isEmpty(deviceName)) {
            device.setDeviceName(deviceName);
        }
        if (!StringUtils.isEmpty(appid)) {
            device.setAppid(appid);
        }

        boolean b = deviceService.updateById(device);
        if (!b) {
            response = ApiResponse.paramError("Modification failed");
        }

        return response;
    }

    @Login
    @PostMapping(value = "/queryDetail")
    @ApiOperation(value="查询是否存在mac", notes="携带token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mac",value = "mac",required = true,dataType = "String")

    })
    public ApiResponse queryDetail( ApiRequest apiRequest) {

        ApiResponse response = ApiResponse.ok();

        String mac = apiRequest.getDataParamAsString("mac");

        if (ValidateUtil.isEmpty(mac)) {
            response = ApiResponse.paramError("mac is null");
        }
        LambdaQueryWrapper<Device> lambda = new QueryWrapper<Device>().lambda();
        lambda.eq(Device::getMac, mac);

        List<Device> list = deviceService.list(lambda);
        if (list.size() > 0) {
            response.addValueToData("status", 1);
            return response;
        }


        response.addValueToData("status", 0);
        return response;
    }

    @Login
    @PostMapping(value = "/queryShare")
    @ApiOperation(value="我接收到的分享锁接口", notes="携带token")
    public ApiResponse queryShare( ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();
        List<Integer> list = new ArrayList<>();
        Member member = MemberLocalCache.get();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = formatter.format(date);
        List<Share> shares = shareService.selectShare(member.getPhone(),format);

//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime parse = LocalDateTime.parse(now.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (!shares.isEmpty()) {
            shares.forEach(share -> {
                list.add(share.getDeviceId());
            });
        }

        List<Device> share = new ArrayList<>();

        if (!list.isEmpty()) {
            share = deviceService.selectByIdSet(list);
        }

        response.addValueToData("share", share);
        return response;
    }


    @Login
    @PostMapping(value = "/queryShareHistroy")
    @ApiOperation(value="我接收到的分享锁接口历史记录", notes="携带token")
    public ApiResponse queryShareHistroy( ApiRequest apiRequest) {
        ApiResponse response = ApiResponse.ok();
        List<Integer> list = new ArrayList<>();
        Member member = MemberLocalCache.get();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = formatter.format(date);
//        List<Share> shares = shareService.selectShare(member.getPhone(),format);

        List<Share> shares = shareService.getBaseMapper().selectList(new QueryWrapper<Share>().lambda().eq(Share::getPhone, member.getPhone()));

        if (!shares.isEmpty()) {
            shares.forEach(share -> {
                share.setDevice(deviceService.getById(share.getDeviceId()));
            });
        }

        response.addValueToData("share", shares);
        return response;
    }
}

