package com.jixunkeji.mapper;

import com.jixunkeji.entity.Device;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-23
 */
public interface DeviceMapper extends BaseMapper<Device> {

    List<Device> selectByIdSet(List<Integer> list);
}
