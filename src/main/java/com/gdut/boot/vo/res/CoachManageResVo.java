package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gdut.boot.annotation.file.Img;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @TableName coach_manage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachManageResVo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工号
     */
    private String number;

    /**
     * 名字
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idCardNumber;

    /**
     * 职称
     */
    private String professionName;

    /**
     * 证件照路径
     */
    private String cardPicture;

    /**
     * 运动项目和组别一起
     */
    private Queue<Queue> projectGroup;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机
     */
    private String phone;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}