package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gdut.boot.annotation.file.Img;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @TableName entrance_exam_msg
 */
@TableName(value ="entrance_exam_msg")
@Data
public class EntranceExamMsgVo implements Serializable {
    /**
     * 
     */
    @TableId
    private int id;

    /**
     * 学号
     */
    @NotNull
    private String number;

    /**
     * 考试所在省份/直辖市
     */
    @NotNull
    private String examProvinces;

    /**
     * 考试所在地级市
     */
    @NotNull
    private String examPrefecture;

    /**
     * 准考证号码
     */
    @NotNull
    private String admissionTicketNumber;

    /**
     * 是否在校
     */
    @NotNull
    private String inSchool;

    /**
     * 身份证附件路径
     */
    @Img
    private MultipartFile idCard;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}