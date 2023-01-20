package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName one_organization
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneOrganizationVo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 组织名称
     */
    @NotNull(message = "组织名称不能为空")
    private String name;

    /**
     * 组织简称
     */
    @NotNull(message = "组织简称不能为空")
    private String simpleName;

    /**
     * 组织英文名
     */
    private String engName;

    /**
     * 组织编码
     */
    @NotNull(message = "组织编码不能为空")
    private String code;

    /**
     * 分管领导
     */
    //@NotNull(message = "分管领导不能为空")
    private List<String> chargeLeader;

    /**
     * 部门领导
     */
    //@NotNull(message = "部门领导不能为空")
    private List<String> departmentLeader;

    /**
     * 存放位置
     */
    private String savePlace;

    /**
     * 存放位置前或者后 1：前   0：后
     */
    private int savePlaceChoice;

    /**
     * 描述
     */
    private String details;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}