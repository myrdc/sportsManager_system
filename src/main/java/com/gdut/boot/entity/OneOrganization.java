package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName one_organization
 */
@TableName(value ="one_organization")
@Data
public class OneOrganization implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织简称
     */
    private String simpleName;

    /**
     * 组织英文名
     */
    private String engName;

    /**
     * 组织编码
     */
    private String code;

    /**
     * 分管领导
     */
    private String chargeLeader;

    /**
     * 部门领导
     */
    private String departmentLeader;

    /**
     * 存放位置
     */
    private String savePlace;

    /**
     * 存放位置前或者后 1：前   0：后
     */
    private Integer savePlaceChoice;

    /**
     * 描述
     */
    private String details;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}