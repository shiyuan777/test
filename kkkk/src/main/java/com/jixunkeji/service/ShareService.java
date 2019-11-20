package com.jixunkeji.service;

import com.jixunkeji.entity.Share;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-24
 */
public interface ShareService extends IService<Share> {

    List<Share> selectShare(String phone, String time);
}
