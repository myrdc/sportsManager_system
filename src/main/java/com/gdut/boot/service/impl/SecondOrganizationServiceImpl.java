package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.controller.SecondOrganizationController;
import com.gdut.boot.entity.*;
import com.gdut.boot.mapper.CoachStuMapper;
import com.gdut.boot.mapper.SeOrganizationStudentMapper;
import com.gdut.boot.service.*;
import com.gdut.boot.mapper.SecondOrganizationMapper;
import com.gdut.boot.vo.SportsBaseMsgVo;
import com.gdut.boot.vo.res.SecondOrganizationResVo;
import com.gdut.boot.vo.res.SportsBaseMsgResVo;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import static com.gdut.boot.util.MessageUtil.getPage;

/**
 *
 */
@Service
public class SecondOrganizationServiceImpl extends ServiceImpl<SecondOrganizationMapper, SecondOrganization>
    implements SecondOrganizationService{

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @Autowired
    private SeOrganizationCoachService seOrganizationCoachService;

    @Autowired
    private CoachStuMapper coachStuMapper;

    @Autowired
    private SeOrganizationStudentService seOrganizationStudentService;

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;


    @Autowired
    private CoachManageService coachManageService;

    @Autowired
    private SeOrganizationStudentMapper seOrganizationStudentMapper;


    /**
     * 处理二级组织教练的问题
     * @param sportsBaseMsgVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg deal(SportsBaseMsgVo sportsBaseMsgVo){
        //组别
        final String inGroup = sportsBaseMsgVo.getInGroup();
        final String sportProject = sportsBaseMsgVo.getSportProject();
        //通过一级和二级组织同时找出来
        final List<SecondOrganization> name = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", inGroup).eq("one_org", sportProject));
        if(name.size() == 0){
            return Msg.fail("二级组织不存在");
        }
        //二级组织的id
        final Integer seId = name.get(0).getId();
        //找到所属的教练
        final List<SeOrganizationCoach> coaches = seOrganizationCoachService.list(new QueryWrapper<SeOrganizationCoach>().eq("second_organization", seId));
        if(coaches.size() != 0){
            final Map<Integer, Integer> collect = coaches.stream().collect(Collectors.
                    toMap(SeOrganizationCoach::getCoachId, SeOrganizationCoach::getId, (key1, key2) -> key2));
            final Set<Integer> integers = collect.keySet(); //所有的教练
            //插入教练和学生表中
            if(!integers.isEmpty()){
                coachStuMapper.insertByCoachSet(integers, sportsBaseMsgVo.getId());
            }
            return Msg.success();
        }
        return Msg.success();
    }

    @Override
    public Msg selectStusById(Integer id, Integer page, Integer size) {
        final List<SeOrganizationStudent> seOrganizationStudents = seOrganizationStudentService.list(new QueryWrapper<SeOrganizationStudent>().eq("second_organization", id));
        Set<Integer> set = new HashSet<>();
        for (SeOrganizationStudent seOrganizationStudent : seOrganizationStudents) {
            set.add(seOrganizationStudent.getStudentId());
        }
        if(set.isEmpty()){
            return Msg.success("该组织没有运动员");
        }
        final QueryWrapper<SportsBaseMsg> queryWrapper = new QueryWrapper<SportsBaseMsg>().in("id", set);
        final Page records = sportsBaseMsgService.page(getPage(page, size), queryWrapper);
        final List res = records.getRecords();
        for (int i = 0; i < res.size(); i++) {
            res.set(i, sportsBaseMsgService.toVo((SportsBaseMsg) res.get(i)));
        }
        return Msg.success("成功", records);
    }

    @Override
    public Msg selectCoachById(Integer id, Integer page, Integer size) {
        final List<SeOrganizationCoach> seOrganizationCoaches = seOrganizationCoachService.list(new QueryWrapper<SeOrganizationCoach>().eq("second_organization", id));
        Set<Integer> set = new HashSet<>();
        for (SeOrganizationCoach seOrganizationCoach : seOrganizationCoaches) {
            set.add(seOrganizationCoach.getCoachId());
        }
        if(set.isEmpty()){
            return Msg.success("该组织没有教练");
        }
        final QueryWrapper<CoachManage> queryWrapper = new QueryWrapper<CoachManage>().in("id", set);
        final Page records = coachManageService.page(getPage(page, size), queryWrapper);
        final List res = records.getRecords();
        for (int i = 0; i < res.size(); i++) {
            res.set(i, coachManageService.toVo((CoachManage) res.get(i)));
        }
        return Msg.success("成功", records);
    }

    @Override
    public Object toVo(SecondOrganization record) {
        SecondOrganizationResVo res = new SecondOrganizationResVo();
        res.setId(record.getId());
        res.setName(record.getName());
        res.setOneOrg(record.getOneOrg());
        res.setQueueName(record.getQueueName());
        res.setIntroduce(record.getIntroduce());
        res.setBadge(record.getBadge());
        final Integer id = record.getId();
        final List<SeOrganizationCoach> seOrganizationCoaches = seOrganizationCoachService.list(new QueryWrapper<SeOrganizationCoach>().eq("second_organization", id));
        final Set<Integer> collect = seOrganizationCoaches.stream().map(SeOrganizationCoach::getCoachId).collect(Collectors.toSet());
        if(!collect.isEmpty()){
            final List<CoachManage> coachManages = coachManageService.listByIds(collect);
            final List<String> names = coachManages.stream().map(CoachManage::getName).collect(Collectors.toList());
            res.setCoach(names);
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public Msg dealByExcel(SportsBaseMsg sportsBaseMsg){
        //组别
        final String inGroup = sportsBaseMsg.getInGroup();
        final String sportProject = sportsBaseMsg.getSportProject();
        //通过一级和二级组织同时找出来
        final List<SecondOrganization> name = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", inGroup).eq("one_org", sportProject));
        if(name.size() == 0){
            return Msg.fail("二级组织不存在");
        }
        //二级组织的id
        final Integer seId = name.get(0).getId();
        //找到所属的教练
        final List<SeOrganizationCoach> coaches = seOrganizationCoachService.list(new QueryWrapper<SeOrganizationCoach>().eq("second_organization", seId));
        if(coaches.size() != 0){
            final Map<Integer, Integer> collect = coaches.stream().collect(Collectors.
                    toMap(SeOrganizationCoach::getCoachId, SeOrganizationCoach::getId, (key1, key2) -> key2));
            final Set<Integer> integers = collect.keySet(); //所有的教练
            //插入教练和学生表中
            if(!integers.isEmpty()){
                coachStuMapper.insertByCoachSet(integers, sportsBaseMsg.getId());
            }
        }
        //然后关联一级用户和组织
        seOrganizationStudentMapper.insert(new SeOrganizationStudent(0, seId, sportsBaseMsg.getId()));
        return Msg.success();
    }
}




