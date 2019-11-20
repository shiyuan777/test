package com.jixunkeji.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author shiyuan
 * @since 2019-10-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareMember extends Model<ShareMember> implements Serializable{


    private static final long serialVersionUID = 8644827310160583622L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 名称
     */
    private String nickName;

    /**
     * 用户id
     */
    private Long memberId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
