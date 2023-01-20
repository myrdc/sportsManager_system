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
 * @TableName se_organization_student
 */
@TableName(value ="se_organization_student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeOrganizationStudent implements Serializable {
    /**
     * 主
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 二级组织id
     */
    private Integer secondOrganization;

    /**
     * 运动员id
     */
    private Integer studentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}