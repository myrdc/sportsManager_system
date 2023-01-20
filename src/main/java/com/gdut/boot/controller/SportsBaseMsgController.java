package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.SeOrganizationStudent;
import com.gdut.boot.entity.SecondOrganization;
import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.SeOrganizationStudentService;
import com.gdut.boot.service.SecondOrganizationService;
import com.gdut.boot.service.SportsBaseMsgService;
import com.gdut.boot.vo.SportsBaseMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.util.MessageUtil.toPageContent;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/2/1221:00
 */

@RestController
@RequestMapping("/api/sportsBaseMsg")
public class SportsBaseMsgController {

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @Autowired
    private SportsBaseMsgService sportsBaseMsgService;

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @Autowired
    private SeOrganizationStudentService seOrganizationStudentService;

    @RequestMapping()
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.SportsBaseMsg.getValue(),
                toPageContent(map, reflectors.get(AllClass.SportsBaseMsg.getValue()).getEntity(), null)));
    }

    @PostMapping()
    @AuthName(name = "post")
    public Msg post(SportsBaseMsgVo sportsBaseMsgVo, HttpServletRequest request){
        //首先要将sportsBaseMsgVo中的数据转化开来
        //首先判断下二级组织是否存在
        List<SportsBaseMsg> number = sportsBaseMsgService.list(new UpdateWrapper<SportsBaseMsg>().eq("number", sportsBaseMsgVo.getNumber()));
        if(!number.isEmpty()){
            return Msg.fail("已经有相同学号的数据存在了");
        }
        final String inGroup = sportsBaseMsgVo.getInGroup();
        final String[] split = inGroup.split(":");
        sportsBaseMsgVo.setSportProject(split[0]);
        sportsBaseMsgVo.setInGroup(split[1]);
        //通过一级和二级组织找出对应的二级组织
        final List<SecondOrganization> secondOrganizations = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", sportsBaseMsgVo.getInGroup()).eq("one_org", sportsBaseMsgVo.getSportProject()));
        if(secondOrganizations.isEmpty()){
            return Msg.fail("二级组织不存在");
        }
        final Msg deal = HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, sportsBaseMsgVo, AllClass.SportsBaseMsg.getValue(), null));
        if(deal.getCode() == 1){
            //处理教练的问题
            //组别
            sportsBaseMsgVo.setId(((SportsBaseMsg)deal.getData()).getId());
            Msg msg = secondOrganizationService.deal(sportsBaseMsgVo);
            if(msg.getCode() == 0){
                return msg;
            }
            //把用户和二级组织关联起来
            seOrganizationStudentService.saveSeStu(sportsBaseMsgVo.getInGroup(), sportsBaseMsgVo.getSportProject(), sportsBaseMsgVo.getId());
        }
        deal.setMessage("添加成功");
        return deal;
    }

    @DeleteMapping()
    @AuthName(name = "delete")
    @Transactional(rollbackFor = Exception.class)
    public Msg delete(@RequestBody SportsBaseMsgVo sportsBaseMsgVo, HttpServletRequest request){
        //删除基本信息之后要把另外三张表的信息也删除掉
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, sportsBaseMsgVo, AllClass.SportsBaseMsg.getValue(), null));

    }

    /**
     * 用户修改信息
     * @param sportsBaseMsgVo
     * @param request
     * @return
     */
    @PutMapping()
    @AuthName(name = "put")
    @Transactional(rollbackFor = Exception.class)
    public Msg put(SportsBaseMsgVo sportsBaseMsgVo, HttpServletRequest request){
        SportsBaseMsg sport = sportsBaseMsgService.query().eq("id", sportsBaseMsgVo.getId()).one();
        final String inGroup = sportsBaseMsgVo.getInGroup();
        final String[] split = inGroup.split(":");
        sportsBaseMsgVo.setSportProject(split[0]);
        sportsBaseMsgVo.setInGroup(split[1]);
        final List<SecondOrganization> secondOrganizations = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", sportsBaseMsgVo.getInGroup()).eq("one_org", sportsBaseMsgVo.getSportProject()));
        if(secondOrganizations.isEmpty()){
            return Msg.fail("二级组织不存在");
        }
        Msg res = HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, sportsBaseMsgVo, AllClass.SportsBaseMsg.getValue(), null));
        if(res.getCode() == 1){
            //修改成功, 判断用户有没有修改组织关系
            if(!sport.getInGroup().equals(sportsBaseMsgVo.getInGroup()) || !sport.getSportProject().equals(sportsBaseMsgVo.getSportProject())){
                sportsBaseMsgService.changeOrg(sport, sportsBaseMsgVo, secondOrganizations.get(0));
            }
        }
        return res;
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    @Transactional(rollbackFor = Exception.class)
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.SportsBaseMsg.getValue());
    }
}
