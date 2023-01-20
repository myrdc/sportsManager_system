package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.gdut.boot.annotation.file.Img;
import lombok.Data;

/**
 * 
 * @TableName entrance_info
 */
@TableName(value ="entrance_info")
@Data
public class EntranceInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 学工号
     */
    private String number;

    /**
     * 录取方式
     */
    private String acceptanceType;

    /**
     * 入学时间
     */
    private String entranceTime;

    /**
     * 原所在学校
     */
    private String originalSchool;

    /**
     * 统考文化总分
     */
    private int totalCulturalScore;

    /**
     * 录取专业
     */
    private String takingProfessional;

    /**
     * 所在学院
     */
    private String ingCollege;

    /**
     * 年级班别
     */
    private String levelClass;

    /**
     * 报考地点
     */
    private String examinationPlace;

    /**
     * 体尖加分分数
     */
    private int addedScore;

    /**
     * 学生证号码
     */
    private String studentCardNum;

    /**
     * 所属教练
     */
    private String belongCoach;

    /**
     * 学生证文件地址
     */
    @Img
    private String studentCard;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}