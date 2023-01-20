package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName user_permission
 */
@TableName(value ="user_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermission implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户学工号
     */
    private String userNumber;

    /**
     * 权限id
     */
    private Integer permissionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}