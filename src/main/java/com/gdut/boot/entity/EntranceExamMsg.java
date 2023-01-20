package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.gdut.boot.annotation.file.Img;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName entrance_exam_msg
 */
@TableName(value ="entrance_exam_msg")
@Data
public class EntranceExamMsg implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 
     */
    @NotNull
    private String number;

    /**
     * 
     */
    private String examProvinces;

    /**
     * 
     */
    private String examPrefecture;

    /**
     * 
     */
    private String admissionTicketNumber;

    /**
     * 
     */
    private String inSchool;

    /**
     * 
     */
    @Img
    private String idCard;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}