package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName resources_middle
 */
@TableName(value ="resources_middle")
@Data
public class ResourcesMiddle implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer classIntId;

    /**
     * 
     */
    private Integer permissionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}