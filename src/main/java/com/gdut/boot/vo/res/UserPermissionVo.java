package com.gdut.boot.vo.res;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gdut.boot.bean.ResourceName;
import com.gdut.boot.constance.permission.Permission;
import com.gdut.boot.constance.permission.Resources;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @TableName user_permission
 */

@Data
public class UserPermissionVo implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户学工号
     */
    private String userNumber;

    /**
     * 权限
     */
    private List<String> permission;

    /**
     * 资源
     */
    private Map<String, List<ResourceName>> resourceMiddles;

    /**
     * 这个用户登陆使用的token
     */
    private String token;

    public UserPermissionVo() {
        this.permission = new LinkedList<>();
        this.resourceMiddles = new HashMap<>();
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}