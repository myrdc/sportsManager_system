package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @TableName approve
 */
@TableName(value ="approve")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Approve implements Serializable {
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
    private String number;

    /**
     * 审核状态，是否已审核，方便提醒
     */
    private int state;

    /**
     * 审核谁的
     */
    private String resourceNumber;


    /**
     * 审核插入时间
     */
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
     * 结果状态
     */
    private int result;

    /**
     * 审核时间
     */
    private String approveTime;

    /**
     * 插入时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    private String reqName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}