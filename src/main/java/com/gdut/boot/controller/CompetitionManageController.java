package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.CompetitionManage;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.vo.CompetitionManageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static com.gdut.boot.constance.cache.Cache.HANDLERS;
import static com.gdut.boot.constance.cache.Cache.reflectors;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.constance.common.CompetitionCommons.COMPETITION_APPROVE_WAIT;
import static com.gdut.boot.util.MessageUtil.toPageContent;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/2/132:20
 */

@Controller
@RequestMapping("/api/competitionManage")
public class CompetitionManageController {

    @Autowired
    CompetitionManageService competitionManageService;

    @Autowired
    HandlerDeleteService handlerDeleteService;


    static String[] competitionManageListType = null;
    static{
        competitionManageListType = new String[]{"name", "coach", "leader", "approvePerson"};
    }

    @GetMapping
    @ResponseBody
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.CompetitionManage.getValue(),
                toPageContent(map, reflectors.get(AllClass.CompetitionManage.getValue()).getEntity(), competitionManageListType)));
    }

    @PostMapping
    @ResponseBody
    @AuthName(name = "post")
    public Msg post(CompetitionManageVo competitionManageVo, HttpServletRequest request){
        //比赛不能重名
        List<CompetitionManage> desgination = competitionManageService.list(new QueryWrapper<CompetitionManage>().eq("desgination", competitionManageVo.getDesgination()));
        if(desgination.size() != 0){
            return Msg.fail("名字和已有比赛重复");
        }
        competitionManageVo.setState(COMPETITION_APPROVE_WAIT);
        Msg res = HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, competitionManageVo, AllClass.CompetitionManage.getValue(), null));
        if(res.getCode() == Msg.success().getCode()){
            res.setMessage("添加成功, 等待审核");
        }
        return res;
    }

    @PutMapping
    @ResponseBody
    @AuthName(name = "put")
    public Msg put(CompetitionManageVo competitionManageVo, HttpServletRequest request){
        //只有比赛申请撤回了才可以进行
        int id = competitionManageVo.getId();
        List<CompetitionManage> ids = competitionManageService.list(new QueryWrapper<CompetitionManage>().eq("id", id));
        if(ids.size() == 0){
            return Msg.fail("不存在该id");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, competitionManageVo, AllClass.CompetitionManage.getValue(), null));
    }

    @DeleteMapping
    @ResponseBody
    @AuthName(name = "delete")
    public Msg delete(@RequestBody CompetitionManageVo competitionManageVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, competitionManageVo, AllClass.CompetitionManage.getValue(), null));
    }

    @DeleteMapping("deleteBatch")
    @ResponseBody
    @AuthName(name = "deleteBatch")
    public Msg deleteBatch(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.CompetitionManage.getValue());
    }


}
