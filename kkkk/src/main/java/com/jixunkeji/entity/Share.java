package com.jixunkeji.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author shiyuan
 * @since 2019-09-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Share extends Model<Share> implements Serializable {


    private static final long serialVersionUID = 7005171952009333935L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    private Integer deviceId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    private Integer status;

    private Long memberId;
    @TableField(exist = false)
    private Integer shareId;

    @TableField(exist = false)
    private Device device;
}
