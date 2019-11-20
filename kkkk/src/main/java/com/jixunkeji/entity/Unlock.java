package com.jixunkeji.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author shiyuan
 * @since 2019-11-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unlock extends Model<Unlock> implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 锁id
     */
    private Integer deviceId;

    /**
     * 锁名称
     */
    private String deviceName;

    private Long memberId;

    private LocalDateTime createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
