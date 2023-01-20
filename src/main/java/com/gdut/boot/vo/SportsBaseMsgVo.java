package com.gdut.boot.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.Img;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName sports_base_msg
 */
@TableName(value ="sports_base_msg")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SportsBaseMsgVo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 中文姓名
     */
    @NotNull(message = "中文姓名不能为空")
    @Length(max = 20, message = "中文姓名长度不能超过20")
    private String chineseName;

    /**
     * 运动项目
     */
//    @NotNull(message = "运动项目不能为空")
//    @Length(max = 20, message = "运动项目不能超过20")
    private String sportProject;

    /**
     * 身高
     */
    @NotNull(message = "身高不能为空")
    @Length(max = 10, message = "身高位数太多了")
    private String high;

    /**
     * 0：女   1：男
     */
    @NotNull(message = "性别不能为空")
    private String gender;

    /**
     * 组别
     */
    @NotNull(message = "组别不能为空")
    private String inGroup;

    /**
     * 体重
     */
    @NotNull(message = "体重不能为空")
    @Length(max = 10, message = "体重位数太多了")
    private String weight;

    /**
     * 学号
     */
    @NotNull(message = "学工号不能为空")
    @Length(max = 30, message = "学工号位数不能超出30")
    private String number;

    /**
     * 民族
     */
    @NotNull(message = "民族不能为空")
    @Length(max = 10, message = "民族不能超出10")
    private String nation;

    /**
     * 学院
     */
    @NotNull(message = "学院不能为空")
    @Length(max = 10, message = "学院不能超出10")
    private String college;

    /**
     * 录取专业
     */
    @NotNull(message = "录取专业不能为空")
    @Length(max = 20, message = "录取专业不能超出20")
    private String professional;

    /**
     * 班级
     */
    @NotNull(message = "班级不能为空")
    @Length(max = 20, message = "班级不能超出20")
    private String stuClass;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @Length(max = 20, message = "出生日期不能超出20")
    private String birth;

    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    @Length(max = 30, message = "身份证不能超出30")
    private String idCardNumber;

    /**
     * 身份证类型
     */
    //@NotNull(message = "身份证类型不能为空")
    private String idCardType;

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    @Length(max = 20, message = "手机号码不能超出20")
    private String phone;

    /**
     * 身份证正面
     */
    @Img
    @NotNull(message = "身份证正面不能为空")
    private MultipartFile idCardFront;

    /**
     * 身份证反面
     */
    @Img
    @NotNull(message = "身份证反面不能为空")
    private MultipartFile idCardAfter;

    /**
     * 照片路径
     */
    @Img
    @NotNull(message = "照片不能为空")
    private MultipartFile picture;

    /**
     * 学籍报告
     */
    @File
    private MultipartFile studentReport;

    /**
     * 是否在校
     */
    @NotNull(message = "是否在校不能为空")
    private String isSchool;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}