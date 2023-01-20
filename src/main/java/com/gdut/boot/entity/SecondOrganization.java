package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.gdut.boot.annotation.file.Img;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName second_organization
 */
@TableName(value ="second_organization")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondOrganization implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 二级组织名字
     */
    @NotNull
    private String name;

    /**
     * 对应的一级组织的id
     */
    @NotNull
    private String oneOrg;

    /**
     * 队名
     */
    private String queueName;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 队徽
     */
    @Img
    private String badge;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}