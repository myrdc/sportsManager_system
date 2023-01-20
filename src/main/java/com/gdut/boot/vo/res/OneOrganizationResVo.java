package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Queue;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/141:28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneOrganizationResVo {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private int id;

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
    private Queue<String> chargeLeader;

    /**
     * 部门领导
     */
    private Queue<String> departmentLeader;

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
