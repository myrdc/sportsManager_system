package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gdut.boot.annotation.file.File;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.ImgList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName competition_manage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionManageVo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 名称
     */
    @NotNull
    private String desgination;

    /**
     * 姓名
     */
    @NotNull
    private List<String> name;

    /**
     * 教练
     */
    @NotNull
    private List<String> coach;

    /**
     * 领队
     */
    private List<String> leader;

    /**
     * 比赛时间
     */
    private String competitionTime;

    /**
     * 比赛项目
     */
    private String competitionProject;

    /**
     * 比赛地点
     */
    private String competitionPlace;

    /**
     * 成绩
     */
    private String results;

    /**
     * 比赛级别
     */
    private String competitionLevel;

    /**
     * 成绩册地址
     */
    @FileList
    private List<MultipartFile> resultsBook;

    /**
     * 秩序册
     */
    @FileList
    private List<MultipartFile> orderBook;

    /**
     * 比赛照片：list转json
     */
    @ImgList
    private List<MultipartFile> competitionPicture;

    /**
     * 成绩证书：linux位置
     */
    @FileList
    private List<MultipartFile> resultsCertificate;

    /**
     * 颁奖照片：list转json
     */
    @ImgList
    private List<MultipartFile> prizesPicture;

    /**
     * 审核状态 0:代审核   1:等待分管领导审核 2：等待部门负责人审核 3：已通过
     * 一开始默认是0
     */
    private String state;

    //审核人
    private List<String> approvePerson;

    /**
     * 比赛经费
     */
    private String money;

    /**
     * 比赛前的或者比赛后的 1：比赛前的 2：比赛后的
     */
    private String beforeOrAfter;

    /**
     * data_type 数据状态：个人或者全部
     */
    private String dataType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}