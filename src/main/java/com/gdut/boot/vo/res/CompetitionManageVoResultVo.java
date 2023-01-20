package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gdut.boot.annotation.file.FileList;
import com.gdut.boot.annotation.file.ImgList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @TableName competition_manage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionManageVoResultVo implements Serializable {
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
    private Queue<String> name;

    /**
     * 教练
     */
    @NotNull
    private Queue<String> coach;

    /**
     * 领队
     */
    private Queue<String> leader;

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
    private Queue<String> resultsBook;

    /**
     * 秩序册
     */
    @FileList
    private Queue<String> orderBook;

    /**
     * 比赛照片：list转json
     */
    @ImgList
    private Queue<String> competitionPicture;

    /**
     * 成绩证书：linux位置
     */
    @FileList
    private Queue<String> resultsCertificate;

    /**
     * 颁奖照片：list转json
     */
    @ImgList
    private Queue<String> prizesPicture;

    /**
     * 审核状态 0:代审核   1:等待分管领导审核 2：等待部门负责人审核 3：已通过
     * 一开始默认是0
     */
    private String state;

    //审核人
    @NotNull
    private Queue<String> approvePerson;

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