package com.jixunkeji.service.impl;

import com.jixunkeji.entity.Device;
import com.jixunkeji.mapper.DeviceMapper;
import com.jixunkeji.service.DeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-23
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public List<Device> selectByIdSet(List<Integer> list) {
        return deviceMapper.selectByIdSet(list);
    }

}
