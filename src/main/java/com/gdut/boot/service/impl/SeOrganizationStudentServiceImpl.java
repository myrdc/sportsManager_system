package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.entity.SeOrganizationStudent;
import com.gdut.boot.entity.SecondOrganization;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.mapper.SecondOrganizationMapper;
import com.gdut.boot.service.SeOrganizationStudentService;
import com.gdut.boot.mapper.SeOrganizationStudentMapper;
import com.gdut.boot.service.SecondOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Service
public class SeOrganizationStudentServiceImpl extends ServiceImpl<SeOrganizationStudentMapper, SeOrganizationStudent>
    implements SeOrganizationStudentService{

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @Autowired
    private SeOrganizationStudentMapper seOrganizationStudentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSeStu(String inGroup, String sportsProject, Integer id) {
        //通过一级和二级组织同时找出对应的二级组织
        final List<SecondOrganization> name = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", inGroup).eq("one_org", sportsProject));
        if(name.size() == 0){
            //一般不会出现这个的
            throw new BusinessException(Msg.fail("添加的二级组织不存在"), "saveSeStu", null,
                    Arrays.asList(inGroup, id), SecondOrganization.class);
        }
        final Integer seId = name.get(0).getId();
        seOrganizationStudentMapper.insert(new SeOrganizationStudent(0, seId, id));
        return;
    }
}




