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
 * @TableName sport_competition
 */
@TableName(value ="sport_competition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SportCompetition implements Serializable {
    /**
     * 
     */
    @TableId
    private int id;

    /**
     * 
     */
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