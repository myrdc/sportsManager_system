package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @TableName sports_team
 */
@TableName(value ="sports_team")
@Data
public class SportsTeamVo implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Integer id;

    /**
     * 项目
     */
    @NotNull(message =  "项目不能为空")
    @Length(max = 20, message = "项目长度不能超过20")
    private String project;

    /**
     * 组别
     */
    @NotNull(message =  "组别不能为空")
    @Length(max = 20, message = "组别长度不能超过20")
    private String tranches;

    /**
     * 队名
     */
    private String name;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 队徽x
     */
    private MultipartFile badge;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}