package com.jixunkeji.mapper;

import com.jixunkeji.entity.Share;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-24
 */
public interface ShareMapper extends BaseMapper<Share> {

    @Select("select * from share where phone = #{phone} and begin_time < #{time} and expire_time > #{time} OR (status = 1 and phone = #{phone}) ")
    List<Share> selectShare(@Param("phone") String phone,@Param("time") String time);
}
