package com.jixunkeji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jixunkeji.entity.Device;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-23
 */
public interface DeviceService extends IService<Device> {


    List<Device> selectByIdSet(List<Integer> list);
}
