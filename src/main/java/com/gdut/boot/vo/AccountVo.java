package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @TableName account
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}