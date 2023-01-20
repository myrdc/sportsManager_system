package com.gdut.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName 考勤管理
 */
@TableName(value ="attendance_manege")
@Data
public class AttendanceManege implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 部门
     */
    private String department;

    /**
     * 日期
     */
    private String date;

    /**
     * 学号
     */
    private String stuNumber;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 考勤list的json格式
     */
    private String attendanceList;

    /**
     * 总计考勤数
     */
    private Integer sum;

    /**
     * 备注
     */
    private String note;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}