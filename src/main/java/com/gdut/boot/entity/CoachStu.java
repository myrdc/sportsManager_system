package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName coach_stu
 */
@TableName(value ="coach_stu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachStu implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 教练id
     */
    private Integer coachId;

    /**
     * 运动员id
     */
    private Integer stuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}