package com.gdut.boot.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.Img;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName sports_base_msg
 */
@TableName(value ="sports_base_msg")
@Data
public class SportsBaseMsg implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    /**
     * 中文姓名
     */
    @NotNull(message = "中文姓名不能为空")
    @Length(max = 20, message = "中文姓名长度不能超过20")
    @ExcelProperty(value = "中文姓名", index = 0)
    private String chineseName;

    /**
     * 运动项目
     */
    @NotNull(message = "运动项目不能为空")
    @Length(max = 20, message = "运动项目不能超过20")
    @ExcelProperty(value = "运动项目", index = 1)
    private String sportProject;

    /**
     * 身高
     */
    @NotNull(message = "身高不能为空")
    @Length(max = 10, message = "身高位数太多了")
    @ExcelProperty(value = "身高", index = 2)
    private String high;

    /**
     * 0：女   1：男
     */
    @NotNull(message = "性别不能为空")
    @ExcelProperty(value = "性别", index = 3)
    private String gender;

    /**
     * 组别
     */
    @NotNull(message = "组别不能为空")
    @ExcelProperty(value = "组别", index = 4)
    private String inGroup;

    /**
     * 体重
     */
    @NotNull(message = "体重不能为空")
    @Length(max = 10, message = "体重位数太多了")
    @ExcelProperty(value = "体重", index = 5)
    private String weight;

    /**
     * 学号
     */
    @NotNull(message = "学工号不能为空")
    @Length(max = 30, message = "学工号位数不能超出30")
    @ExcelProperty(value = "学号", index = 6)
    private String number;

    /**
     * 民族
     */
    @NotNull(message = "民族不能为空")
    @Length(max = 10, message = "民族不能超出10")
    @ExcelProperty(value = "民族", index = 7)
    private String nation;

    /**
     * 学院
     */
    @NotNull(message = "学院不能为空")
    @Length(max = 10, message = "学院不能超出10")
    @ExcelProperty(value = "学院", index = 8)
    private String college;

    /**
     * 录取专业
     */
    @NotNull(message = "录取专业不能为空")
    @Length(max = 20, message = "录取专业不能超出20")
    @ExcelProperty(value = "录取专业", index = 9)
    private String professional;

    /**
     * 班级
     */
    @NotNull(message = "班级不能为空")
    @Length(max = 20, message = "班级不能超出20")
    @ExcelProperty(value = "班级", index = 10)
    private String stuClass;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @Length(max = 20, message = "出生日期不能超出20")
    @ExcelProperty(value = "出生日期", index = 11)
    private String birth;

    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    @Length(max = 30, message = "身份证不能超出30")
    @ExcelProperty(value = "身份证号码", index = 12)
    private String idCardNumber;

    /**
     * 身份证类型
     */
    @ExcelProperty(value = "身份证类型", index = 13)
    //@NotNull(message = "身份证类型不能为空")
    private String idCardType;

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    @Length(max = 20, message = "手机号码不能超出20")
    @ExcelProperty(value = "手机号码", index = 14)
    private String phone;

    /**
     * 身份证正面
     */
    @Img
    @ExcelIgnore
    private String idCardFront;

    /**
     * 身份证反面
     */
    @Img
    @ExcelIgnore
    private String idCardAfter;

    /**
     * 照片路径
     */
    @Img
    @ExcelIgnore
    private String picture;

    /**
     * 学籍报告
     */
    @File
    @ExcelIgnore
    private String studentReport;

    @NotNull
    @ExcelProperty(value = "是否在校", index = 15)
    private String isSchool;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}