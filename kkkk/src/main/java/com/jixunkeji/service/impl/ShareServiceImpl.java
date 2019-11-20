package com.jixunkeji.service.impl;

import com.jixunkeji.entity.Share;
import com.jixunkeji.mapper.ShareMapper;
import com.jixunkeji.service.ShareService;
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
 * @since 2019-09-24
 */
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Override
    public List<Share> selectShare(String phone, String time) {
        return shareMapper.selectShare(phone,time);
    }
}
