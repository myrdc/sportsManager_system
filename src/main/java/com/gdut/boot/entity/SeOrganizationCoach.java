package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName se_organization_coach
 */
@TableName(value ="se_organization_coach")
@Data
public class SeOrganizationCoach implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 二级组织的id
     */
    private Integer secondOrganization;

    /**
     * 教练的id
     */
    private Integer coachId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}