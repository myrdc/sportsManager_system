package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.permission.Permission;
import com.gdut.boot.entity.UserPermission;
import com.gdut.boot.mapper.PermissionMapper;
import com.gdut.boot.service.UserPermissionService;
import com.gdut.boot.mapper.UserPermissionMapper;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Service
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermission>
    implements UserPermissionService{

}




