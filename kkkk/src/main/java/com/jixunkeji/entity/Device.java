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
 * @since 2019-09-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device extends Model<Device> implements Serializable {


    private static final long serialVersionUID = 4816346905070962147L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名
     */
    private String deviceName;

    private String appid;

    private String mac;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long memberId;

    private String uid;

    private String password;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
    @TableField(exist = false)
    private Integer shareId;
    @TableField(exist = false)
    private Integer status;
}
