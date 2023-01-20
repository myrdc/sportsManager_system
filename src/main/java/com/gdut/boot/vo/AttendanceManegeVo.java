package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 * @TableName attendance_manege
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceManegeVo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private int id;

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
    private int sum;

    /**
     * 备注
     */
    private String note;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}