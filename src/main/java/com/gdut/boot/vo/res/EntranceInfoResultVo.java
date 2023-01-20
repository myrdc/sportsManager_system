package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gdut.boot.annotation.file.Img;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @TableName entrance_info
 */
@TableName(value ="entrance_info")
@Data
public class EntranceInfoResultVo implements Serializable {
    /**
     * 
     */
    @TableId
    private int id;

    /**
     * 学工号
     */
    @NotNull
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
    @NotNull
    private String originalSchool;

    /**
     * 统考文化总分
     */
    @NotNull
    private int totalCulturalScore;

    /**
     * 录取专业
     */
    @NotNull
    private String takingProfessional;

    /**
     * 所在学院
     */
    @NotNull
    private String ingCollege;

    /**
     * 年级班别
     */
    @NotNull
    private String levelClass;

    /**
     * 报考地点
     */
    @NotNull
    private String examinationPlace;

    /**
     * 体尖加分分数
     */
    @NotNull
    private int addedScore;

    /**
     * 学生证号码
     */
    @NotNull
    private String studentCardNum;

    /**
     * 所属教练
     */
    @NotNull
    private Queue<String> belongCoach;

    /**
     * 学生证文件地址
     */
    @Img
    private String studentCard;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}