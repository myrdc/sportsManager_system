package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.SportsBaseMsg;
import com.gdut.boot.entity.SportsTeam;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.SportsBaseMsgService;
import com.gdut.boot.service.SportsTeamService;
import com.gdut.boot.vo.SportsBaseMsgVo;
import com.gdut.boot.vo.SportsTeamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
 * @date 2022/5/250:29
 */

@Deprecated
@RestController
@RequestMapping("/api/sportsTeam")
public class SportsTeamController {

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @Autowired
    private SportsTeamService sportsTeamService;

    @RequestMapping()
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.SportsTeam.getValue(),
                toPageContent(map, reflectors.get(AllClass.SportsTeam.getValue()).getEntity(), null)));
    }

    @PostMapping()
    @AuthName(name = "post")
    public Msg post(SportsTeamVo sportsTeamVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, sportsTeamVo, AllClass.SportsTeam.getValue(), null));
    }

    @DeleteMapping()
    @AuthName(name = "delete")
    public Msg delete(@RequestBody SportsTeamVo sportsTeamVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, sportsTeamVo, AllClass.SportsTeam.getValue(), null));

    }

    @PutMapping()
    @AuthName(name = "put")
    public Msg put(SportsTeamVo sportsTeamVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, sportsTeamVo, AllClass.SportsTeam.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.SportsTeam.getValue());
    }


}
