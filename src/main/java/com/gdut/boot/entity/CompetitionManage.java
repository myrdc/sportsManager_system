package com.gdut.boot.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.ImgList;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName competition_manage
 */
@TableName(value ="competition_manage")
@Data
public class CompetitionManage implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    /**
     * 名称
     */
    @NotNull
    @ExcelProperty(value = "名称", index = 0)
    private String desgination;

    /**
     * 姓名
     */
    @NotNull
    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    /**
     * 教练
     */
    @NotNull
    @ExcelProperty(value = "教练", index = 2)
    private String coach;

    /**
     * 领队
     */
    @ExcelProperty(value = "领队", index = 3)
    private String leader;

    /**
     * 比赛时间
     */
    @ExcelProperty(value = "比赛时间", index = 4)
    private String competitionTime;

    /**
     * 比赛项目
     */
    @ExcelProperty(value = "比赛项目", index = 5)
    private String competitionProject;

    /**
     * 比赛地点
     */
    @ExcelProperty(value = "比赛地点", index = 6)
    private String competitionPlace;

    /**
     * 成绩
     */
    @ExcelProperty(value = "成绩", index = 7)
    private String results;

    /**
     * 比赛级别
     */
    @ExcelProperty(value = "比赛级别", index = 8)
    private String competitionLevel;

    /**
     * 成绩册地址
     */
    @FileList
    @ExcelIgnore
    private String resultsBook;

    /**
     * 秩序册
     */
    @FileList
    @ExcelIgnore
    private String orderBook;

    /**
     * 比赛照片：list转json
     */
    @ImgList
    @ExcelIgnore
    private String competitionPicture;

    /**
     * 成绩证书：linux位置
     */
    @FileList
    @ExcelIgnore
    private String resultsCertificate;

    /**
     * 颁奖照片：list转json
     */
    @ImgList
    @ExcelIgnore
    private String prizesPicture;

    /**
     * 审核状态
     */
    @ExcelIgnore
    private String state;

    /**
     * 审核人
     */
    @ExcelIgnore
    @NotNull
    private String approvePerson;

    /**
     * 比赛经费
     */
    @ExcelProperty(value = "比赛经费", index = 9)
    private String money;

    /**
     * 比赛前的或者比赛后的 1：比赛前的 2：比赛后的
     */
    @ExcelIgnore
    private String beforeOrAfter;

    /**
     * data_type 数据状态：个人或者全部
     */
    @ExcelIgnore
    private String dataType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}