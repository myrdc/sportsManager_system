package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName approve
 */
@TableName(value ="approve")
@Data
public class ApproveVo implements Serializable {
    /**
     * 
     */
    @TableId
    private int id;

    /**
     * 审核的类型 目前只有一个比赛类型需要审核
     */
    private int type;

    /**
     * 审核的信息id
     */
    private int contentId;

    /**
     * 审核人的学工号
     */
    private List<String> number;

    /**
     * 审核状态，是否已审核，方便提醒 1已审核 2未审核
     */
    private int state;

    /**
     * 审核谁的
     */
    private String resourceNumber;

    /**
     * 审核插入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String time;

    /**
     * 审核意见
     */
    private String message;

    /**
     * 之前的还是之后的
     */
    private int beforeOrAfter;

    /**
     * 结果
     */
    private int result;

    /**
     * 审核时间
     */
    private String approveTime;

    /**
     * 插入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}