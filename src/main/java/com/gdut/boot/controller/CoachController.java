package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.CoachManage;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.service.CoachManageService;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.SecondOrganizationService;
import com.gdut.boot.vo.CoachManageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.gdut.boot.constance.cache.Cache.HANDLERS;
import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.util.MessageUtil.toPageContent;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/301:33
 */

@RestController
@RequestMapping("/api/coach")
public class CoachController {

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @Autowired
    private CoachManageService coachManageService;

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @Resource
    private CompetitionManageService competitionManageService;

    static String[] coachListType = null;
    static{
        coachListType = new String[]{"projectGroup"};
    }

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.CoachManage.getValue(),
                toPageContent(map, reflectors.get(AllClass.CoachManage.getValue()).getEntity(), coachListType)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(CoachManageVo coachManage, HttpServletRequest request){
        List<CoachManage> number = coachManageService.list(new QueryWrapper<CoachManage>().eq("number", coachManage.getNumber()));
        if(!number.isEmpty()){
            return Msg.fail("已经有相同工号的数据存在了");
        }
        //1、首先判断教练里面的一级和二级组织有没有存在
        if(coachManageService.judgeIsUnExit(coachManage)){
            return Msg.fail("运动项目或者组别不存在");
        }
        //2、进行添加
        Msg deal = HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, coachManage, AllClass.CoachManage.getValue(), null));
        if(deal.getCode() == 1){
           try {
               //3.1 教练要和队伍绑定起来
               coachManage.setId(((CoachManage)deal.getData()).getId());   //设置id
               Msg msg = coachManageService.dealWithOrgAndCoach(coachManage);
               if(msg.getCode() == 0){
                   //3.2 遇到异常就删除加入数据库的教练
                   coachManageService.removeById(coachManage.getId());
                   return msg;
               }
           }catch (Exception e){
               //3.3 遇到异常就删除加入数据库的教练
               coachManageService.removeById(coachManage.getId());
               throw new BusinessException(Msg.fail("添加教练异常"), "post", e,
                       Arrays.asList(coachManage, request),
                       coachManage.getClass());
           }
        }
        return deal;
    }

    private void dealWithList(CoachManageVo coachManage) {
        List<String> projectGroup = coachManage.getProjectGroup();
        String[] split = projectGroup.get(0).split(",");
        coachManage.setProjectGroup(Arrays.asList(split));
    }

    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    @AuthName(name = "put")
    public Msg put(CoachManageVo coachManage, HttpServletRequest request){
        CoachManage resource = coachManageService.query().eq("id", coachManage.getId()).one();
        Msg res = HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, coachManage, AllClass.CoachManage.getValue(), null));
        if(res.getCode() == 1){
            //判断coach有没有修改组织关系
            coachManageService.dealIfPutOrg(resource, coachManage);
        }
        return res;
    }

    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    @AuthName(name = "delete")
    public Msg delete(@RequestBody CoachManageVo coachManage, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, coachManage, AllClass.CoachManage.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @Transactional(rollbackFor = Exception.class)
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.CoachManage.getValue());
    }

    @RequestMapping("getStu")
    @AuthName(name = "getStu")
    public Msg getStu(String number){
        return coachManageService.getStuByCoachId(number);
    }

    @RequestMapping("getOrg")
    @AuthName(name = "getOrg")
    public Msg getOrg(String number){
        return coachManageService.getOneOrgById(number);
    }

    /**
     * 传入一个教练id获取所有的组织和学生
     * @param number
     * @return
     */
    @RequestMapping("getAllStuAndOrg")
    @AuthName(name = "getAllStuAndOrg")
    public Msg getAll(String number){
        return coachManageService.getAllStuAndOrg(number);
    }


    @GetMapping("getCoachCompetition")
    @ResponseBody
    @AuthName(name = "getCoachCompetition")
    public Msg getCoachCompetition(String number, String state){
        //教练的number
        return competitionManageService.getCompetitionByCoachNumber(number, state);
    }
}
