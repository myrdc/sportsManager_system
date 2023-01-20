package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.Img;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @TableName sports_base_msg
 */
@TableName(value ="sports_base_msg")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SportsBaseMsgResVo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 中文姓名
     */
    private String chineseName;

    /**
     * 身高
     */
    private String high;

    /**
     * 0：女   1：男
     */
    private String gender;

    /*
        项目组别的类型
     */
    private String projectGroup;

    /**
     * 体重
     */
    private String weight;

    /**
     * 学号
     */
    private String number;

    /**
     * 民族
     */
    private String nation;

    /**
     * 学院
     */
    private String college;

    /**
     * 录取专业
     */
    private String professional;

    /**
     * 班级
     */
    private String stuClass;

    /**
     * 出生日期
     */
    private String birth;

    /**
     * 身份证号码
     */
    private String idCardNumber;

    /**
     * 身份证类型
     */
    private String idCardType;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 身份证正面
     */
    private String idCardFront;

    /**
     * 身份证反面
     */
    private String idCardAfter;

    /**
     * 照片路径
     */
    private String picture;

    /**
     * 学籍报告
     */
    private String studentReport;

    /**
     * 是否在校
     */
    private String isSchool;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}