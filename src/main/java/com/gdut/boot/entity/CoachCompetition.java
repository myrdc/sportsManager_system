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
 * @TableName coach_competition
 */
@TableName(value ="coach_competition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachCompetition implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 教练number
     */
    private String number;

    /**
     * 比赛id
     */
    private Integer competitionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}