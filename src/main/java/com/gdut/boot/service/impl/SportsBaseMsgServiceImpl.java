package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.*;
import com.gdut.boot.mapper.CoachStuMapper;
import com.gdut.boot.service.*;
import com.gdut.boot.mapper.SportsBaseMsgMapper;
import com.gdut.boot.vo.SportsBaseMsgVo;
import com.gdut.boot.vo.res.SportsBaseMsgResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.gdut.boot.util.FileUtils.deleteFile;

/**
 *
 */
@Service
public class SportsBaseMsgServiceImpl extends ServiceImpl<SportsBaseMsgMapper, SportsBaseMsg>
    implements SportsBaseMsgService{

    @Autowired
    EntranceExamMsgService entranceExamMsgService;

    @Autowired
    EntranceInfoService entranceInfoService;

    @Autowired
    private SportCompetitionService sportCompetitionService;

    @Autowired
    private SportsBaseMsgMapper sportsBaseMsgMapper;

    @Autowired
    private SecondOrganizationServiceImpl secondOrganizationService;

    @Resource
    private SeOrganizationStudentService seOrganizationStudentService;

    @Resource
    private SeOrganizationCoachService seOrganizationCoachService;

    @Resource
    private CoachStuService coachStuService;

    @Resource
    private CoachStuMapper coachStuMapper;



    /**
     * 把里面的项目和组织转化为HashMap的方式返回
     * @param record
     * @return
     */
    @Override
    public Object toVo(SportsBaseMsg record) {
        SportsBaseMsgResVo res = new SportsBaseMsgResVo();
        res.setId(record.getId());
        res.setChineseName(record.getChineseName());
        res.setHigh(record.getHigh());
        res.setGender(record.getGender());
        res.setWeight(record.getWeight());
        res.setNumber(record.getNumber());
        res.setNation(record.getNation());
        res.setCollege(record.getCollege());
        res.setProfessional(record.getProfessional());
        res.setStuClass(record.getStuClass());
        res.setBirth(record.getBirth());
        res.setIdCardNumber(record.getIdCardNumber());
        res.setIdCardType(record.getIdCardType());
        res.setPhone(record.getPhone());
        res.setIdCardFront(record.getIdCardFront());
        res.setIdCardAfter(record.getIdCardAfter());
        res.setPicture(record.getPicture());
        res.setStudentReport(record.getStudentReport());
        res.setIsSchool(record.getIsSchool());
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(record.getSportProject(), record.getInGroup());
        res.setProjectGroup(record.getSportProject() + "," + record.getInGroup());
        return res;
    }

    @Override
    public void insertSportsBaseMsg(List<SportsBaseMsg> beans) {
        sportsBaseMsgMapper.insertBatchSomeColumn(beans);
    }

    @Override
    public Msg dealWithExcelBean(List<Object> beans) {
        for (Object bean : beans) {
            return secondOrganizationService.dealByExcel((SportsBaseMsg) bean);
        }
        return null;
    }

    @Override
    public Msg dealBeanBefore(List<Object> beans) {
        for (Object bean : beans) {
            SportsBaseMsg sportsBaseMsg = (SportsBaseMsg) bean;
            //首先要检测是不是重名了
            final String number = sportsBaseMsg.getNumber();
            final List<SportsBaseMsg> sportsBaseMsgs = sportsBaseMsgMapper.selectList(new QueryWrapper<SportsBaseMsg>().eq("number", number));
            if(sportsBaseMsgs.size() != 0){
                return Msg.fail("导入的学号为" + number +  "的运动员重复了");
            }
        }
        return Msg.success();
    }

    /**
     * 修改用户和组织的对应关系
     * @param sport 数据库原来的信息
     * @param sportsBaseMsgVo 用户新修改的信息
     * @param go 用户之后的组织
     * @return
     */
    @Override
    public boolean changeOrg(SportsBaseMsg sport, SportsBaseMsgVo sportsBaseMsgVo, SecondOrganization go) {
        //1. 首先找出这个新的组织的教练
        List<SeOrganizationCoach> newCoaches = seOrganizationCoachService.query().eq("second_organization", go.getId()).list();
        //2. 找出旧的组织的教练
        String oldSportProject = sport.getSportProject();
        String oldInGroup = sport.getInGroup();
        List<SecondOrganization> old = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", oldInGroup).eq("one_org", oldSportProject));
        if(!old.isEmpty()){
            //旧的组织还在，删除旧组织教练和运动员之间的关系
            List<SeOrganizationCoach> coaches = seOrganizationCoachService.query().eq("second_organization", old.get(0).getId()).list();
            if(!coaches.isEmpty()){
                //旧的组织还有教练, 找到教练的id
                List<Integer> coachIds = coaches.stream().map(SeOrganizationCoach::getCoachId).collect(Collectors.toList());
                //获取用户id
                Integer sportId = sportsBaseMsgVo.getId();
                //开始删除
                coachStuService.remove(new QueryWrapper<CoachStu>().eq("stu_id", sportId).in("coach_id", coachIds));
            }
        }else{
            //旧的组织被删除了, 那么不需要删除旧组织教练和运动员之间的关系
        }
        //3. 下面删除二级组织和运动员的关系
        seOrganizationStudentService.remove(new QueryWrapper<SeOrganizationStudent>()
                .eq("second_organization", old.get(0).getId())
                .eq("student_id", sportsBaseMsgVo.getId()));

        //4. 删除原来的关系，现在开始新增
        if(!newCoaches.isEmpty()){
            //4.1 新增教练和运动员的关系
            coachStuMapper.insertByCoachSet(newCoaches.stream().map(SeOrganizationCoach::getCoachId).collect(Collectors.toSet()), sportsBaseMsgVo.getId());
        }

        //4.1 新增二级组织和运动员之间的关系
        seOrganizationStudentService.save(new SeOrganizationStudent(0, go.getId(), sportsBaseMsgVo.getId()));

        return true;
    }
}




