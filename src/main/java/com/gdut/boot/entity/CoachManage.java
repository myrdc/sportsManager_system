package com.gdut.boot.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import com.gdut.boot.annotation.file.Img;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName coach_manage
 */
@TableName(value ="coach_manage")
@Data
public class CoachManage implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    /**
     * 工号
     */
    @NotNull(message = "工号不能为空")
    @Length(max = 20, message = "工号长度不能超出20")
    @ExcelProperty(value = "工号", index = 0)
    private String number;

    /**
     * 名字
     */
    @NotNull(message = "名字不能为空")
    @Length(max = 20, message = "名字长度不能超出20")
    @ExcelProperty(value = "名字", index = 1)
    private String name;

    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    @Length(max = 30, message = "身份证号码长度不能超出30")
    @ExcelProperty(value = "身份证号码", index = 2)
    private String idCardNumber;

    /**
     * 职称
     */
    @NotNull(message = "职称不能为空")
    @Length(max = 30, message = "职称长度不能超出30")
    @ExcelProperty(value = "职称", index = 3)
    private String professionName;

    /**
     * 证件照路径
     */
    @Img
    @ExcelIgnore
    private String cardPicture;

    /**
     * 运动项目（多个）
     */
    @NotNull(message = "运动项目和组别不能为空")
    @ExcelProperty(value = "运动项目", index = 4)
    private String projectGroup;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Length(max = 5, message = "性别长度不能超出5")
    @ExcelProperty(value = "性别", index = 5)
    private String gender;

    /**
     * 手机
     */
    @NotNull(message = "手机号不能为空")
    @Length(max = 20, message = "手机号长度不能超出20")
    @ExcelProperty(value = "手机", index = 6)
    private String phone;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}