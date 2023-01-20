package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gdut.boot.annotation.file.Img;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName second_organization
 */
@Data
//返回的二级组织中要加入教练
public class SecondOrganizationResVo implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 二级组织名字
     */
    private String name;

    /**
     * 一级组织名字
     */
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
    private String badge;

    private List<String> coach;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}