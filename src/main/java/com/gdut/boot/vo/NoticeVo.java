package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @TableName notice
 */
@TableName(value ="notice")
@Data
public class NoticeVo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 发给谁的学工号
     */
    @NotNull(message = "通知人不能为空")
    private String toPerson;

    /**
     * 通知信息
     */
    @NotNull(message = "通知信息不能为空")
    private String message;

    /**
     * 谁发的
     */
    @NotNull(message = "发送者不能为空")
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