package com.gdut.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.*;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.mapper.SportCompetitionMapper;
import com.gdut.boot.mapper.SportsBaseMsgMapper;
import com.gdut.boot.service.*;
import com.gdut.boot.mapper.CompetitionManageMapper;
import com.gdut.boot.util.RedisUtils;
import com.gdut.boot.vo.res.CompetitionManageVoResultVo;
import com.gdut.boot.vo.res.UserPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.gdut.boot.constance.common.RedisConstance.redis_login;
import static com.gdut.boot.util.CommonUtil.strToQueue;

/**
 *
 */
@Service
public class CompetitionManageServiceImpl extends ServiceImpl<CompetitionManageMapper, CompetitionManage>
    implements CompetitionManageService{

    @Autowired
    private CompetitionManageMapper competitionManageMapper;

    @Autowired
    private SportsBaseMsgMapper sportsBaseMsgMapper;

    @Resource
    private CoachManageService coachManageService;

    @Autowired
    private SportCompetitionMapper sportCompetitionMapper;

    @Autowired
    private SportCompetitionService sportCompetitionService;

    @Autowired
    private CoachCompetitionService coachCompetitionService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public CompetitionManageVoResultVo toVo(CompetitionManage competitionManage) {
        CompetitionManageVoResultVo vo = new CompetitionManageVoResultVo();
        vo.setId(competitionManage.getId());
        vo.setMoney(competitionManage.getMoney());
        vo.setDesgination(competitionManage.getDesgination());
        vo.setName(strToQueue(competitionManage.getName()));
        vo.setCoach(strToQueue(competitionManage.getName()));
        vo.setLeader(strToQueue(competitionManage.getName()));
        vo.setCompetitionTime(competitionManage.getCompetitionTime());
        vo.setCompetitionProject(competitionManage.getCompetitionProject());
        vo.setCompetitionPlace(competitionManage.getCompetitionPlace());
        vo.setResults(competitionManage.getResults());
        vo.setCompetitionLevel(competitionManage.getCompetitionLevel());
        vo.setResultsBook(strToQueue(competitionManage.getResultsBook()));
        vo.setOrderBook(strToQueue(competitionManage.getOrderBook()));
        vo.setCompetitionPicture(strToQueue(competitionManage.getCompetitionPicture()));
        vo.setResultsCertificate(strToQueue(competitionManage.getResultsCertificate()));
        vo.setState(competitionManage.getState());
        vo.setPrizesPicture(strToQueue(competitionManage.getPrizesPicture()));
        vo.setApprovePerson(strToQueue(competitionManage.getApprovePerson()));
        vo.setBeforeOrAfter(competitionManage.getBeforeOrAfter());
        vo.setDataType(competitionManage.getDataType());
        return vo;
    }

    @Override
    public void insertCompetitionManage(List<CompetitionManage> beans) {
        competitionManageMapper.insertBatchSomeColumn(beans);
    }

    @Override
    public Msg dealWithExcelBean(List<Object> beans) {
        for (Object bean : beans) {
            Msg msg = dealWithAllSports((CompetitionManage)bean);
            if(msg.getCode() == Msg.fail().getCode()){
                return msg;
            }
        }
        return Msg.success();
    }

    @Override
    public Msg dealBeanBefore(List<Object> beans) {
        for (Object bean : beans) {
            //?????????????????????????????????
            String competitionName = ((CompetitionManage)bean).getDesgination();
            List<CompetitionManage> competitionManages = competitionManageMapper.selectList(new QueryWrapper<CompetitionManage>().eq("desgination", competitionName));
            if(competitionManages.size() != 0){
                return Msg.fail("????????????"+ competitionName +"?????????");
            }
            //?????????????????????
            ((CompetitionManage) bean).setState("2");
            //?????????????????????
            UserPermissionVo user = getUser();
            ((CompetitionManage) bean).setApprovePerson(JSON.toJSONString(user.getUserNumber().split(",")));
            //????????????
            ((CompetitionManage) bean).setName(JSON.toJSONString(((CompetitionManage) bean).getName().split(",")));
            //????????????
            ((CompetitionManage) bean).setCoach(JSON.toJSONString(((CompetitionManage) bean).getCoach().split(",")));
            //????????????
            ((CompetitionManage) bean).setLeader(JSON.toJSONString(((CompetitionManage) bean).getLeader().split(",")));
        }
        return Msg.success();
    }

    public UserPermissionVo getUser(){
        String token = request.getHeader("token");
        Object object = redisUtils.get(redis_login + token);
        if(object == null){
            throw new BusinessException(Msg.fail("??????????????????, ?????????????????????"));
        }
        UserPermissionVo user = JSONObject.parseObject((String) object, UserPermissionVo.class);
        return user;
    }

    @Override
    public Msg getCompetitionByCoachNumber(String number, String state) {
        List<CoachManage> coachManages = coachManageService.query().eq("number", number).list();
        if(coachManages.size() == 0){
            return Msg.fail("?????????????????????");
        }
        List<CompetitionManage> res = competitionManageMapper.selectByNumber(number, state);
        if(res.size() == 0){
            return Msg.fail("???????????????????????????");
        }
        return Msg.success("??????" + res);
    }


    private Msg dealWithAllSports(CompetitionManage bean) {
        //?????????????????????
        String sports = bean.getName();
        Queue<String> queue = JSON.parseObject(sports, Queue.class);
//        String[] split = sports.split(",");
//        Queue<String> queue = new LinkedList<String>(Arrays.asList(split));
        //??????id
        Integer id = bean.getId();
        //????????????
        String competitionTime = bean.getCompetitionTime();
        //????????????
        String desgination = bean.getDesgination();
        //??????????????????
        List<SportsBaseMsg> res = sportsBaseMsgMapper.listByNames(queue);
        if(res.isEmpty() || res.size() != queue.size()){
            return Msg.fail("????????????????????????????????????????????????");
        }
        Map<String, String> nameNumbers = res.stream().collect(Collectors.toMap(SportsBaseMsg::getChineseName, SportsBaseMsg::getNumber, (key1, key2) -> key2));
        List<SportCompetition> list = new LinkedList<>();
        for (String name : queue) {
            list.add(new SportCompetition(0, nameNumbers.get(name), desgination, competitionTime, id));
        }
        //????????????
        if(!list.isEmpty()){
            sportCompetitionService.insertSportCompetition(list);
        }
        //?????????????????????, ????????????????????????
//        String coach = bean.getCoach();
//        Queue<String> parseObject = new LinkedList<String>(Arrays.asList(coach.split(",")));
        Queue<String> parseObject = JSON.parseObject(bean.getCoach(), Queue.class);
        for (String coachs : parseObject) {
            //???????????????id
            List<CoachManage> coachManages = coachManageService.query().eq("name", coachs).list();
            if(coachManages.size() != 0){
                coachCompetitionService.save(new CoachCompetition(0, coachManages.get(0).getNumber(), bean.getId()));
            }
        }
        return Msg.success("??????????????????");
    }
}




