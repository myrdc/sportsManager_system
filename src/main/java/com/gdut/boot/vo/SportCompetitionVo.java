package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @TableName sport_competition
 */
@TableName(value ="sport_competition")
@Data
public class SportCompetitionVo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 运动员id
     */
    @NotNull
    private String number;

    /**
     * 比赛名字
     */
    private String competitionName;

    /**
     * 比赛时间
     */
    private String competitionTime;

    /**
     * 比赛具体内容的id
     */
    private int contentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}