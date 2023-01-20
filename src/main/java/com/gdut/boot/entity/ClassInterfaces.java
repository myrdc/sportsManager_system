package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName class_interfaces
 */
@TableName(value ="class_interfaces")
@Data
public class ClassInterfaces implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 类
     */
    private String clazz;

    /**
     * 接口名字
     */
    private String clazzInterface;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}