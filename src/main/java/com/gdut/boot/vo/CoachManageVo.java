package com.gdut.boot.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gdut.boot.annotation.file.Img;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName coach_manage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachManageVo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工号
     */
    @NotNull(message = "工号不能为空")
    @Length(max = 20, message = "工号长度不能超出20")
    private String number;

    /**
     * 名字
     */
    @NotNull(message = "名字不能为空")
    @Length(max = 20, message = "名字长度不能超出20")
    private String name;

    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    @Length(max = 30, message = "身份证号码长度不能超出30")
    private String idCardNumber;

    /**
     * 职称
     */
    @NotNull(message = "职称不能为空")
    @Length(max = 30, message = "职称长度不能超出30")
    private String professionName;

    /**
     * 证件照路径
     */
    @Img
    @NotNull(message = "证件照不能为空")
    private MultipartFile cardPicture;

    /**
     * 运动项目（多个）
     */
    @NotNull(message = "运动项目和组别不能为空")
    private List<String> projectGroup;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Length(max = 5, message = "性别长度不能超出5")
    private String gender;

    /**
     * 手机
     */
    @NotNull(message = "手机号不能为空")
    @Length(max = 20, message = "手机号长度不能超出20")
    private String phone;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}