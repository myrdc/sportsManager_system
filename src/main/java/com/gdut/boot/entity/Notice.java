package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 
 * @TableName notice
 */
@TableName(value ="notice")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 发给谁的学工号
     */
    private String toPerson;

    /**
     * 通知信息
     */
    private String message;

    /**
     * 谁发的
     */
    private String fromPerson;

    /**
     * 是否已读
     */
    private int isRead;

    /**
     * 内容的id
     */
    private int contentId;

    /**
     * 内容的类型
     */
    private int type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}