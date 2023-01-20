package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gdut.boot.annotation.file.Img;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @TableName second_organization
 */
@Data
public class SecondOrganizationVo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 二级组织名字
     */
    @NotNull(message = "二级组织名字不能为空")
    @Length(max = 50, message = "二级组织名字长度不能超过50")
    private String name;

    /**
     * 一级组织名字
     */
    @NotNull(message = "一级组织名字不能为空")
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
    private MultipartFile badge;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}