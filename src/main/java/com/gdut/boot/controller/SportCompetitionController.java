package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.CompetitionManage;
import com.gdut.boot.service.CompetitionManageService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.SportCompetitionService;
import com.gdut.boot.vo.SportCompetitionVo;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2022/2/1520:40
 */

@RestController
@RequestMapping("/api/sportCompetition")
public class SportCompetitionController {

    @Autowired
    private SportCompetitionService sportCompetitionService;

    @Autowired
    private CompetitionManageService competitionManageService;

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.SportCompetition.getValue(),
                toPageContent(map, reflectors.get(AllClass.SportCompetition.getValue()).getEntity(), null)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(SportCompetitionVo sportCompetitionVo, HttpServletRequest request){
        //判断对应的内容id有没有存在
        List<CompetitionManage> contents = competitionManageService.list(new QueryWrapper<CompetitionManage>().eq("id", sportCompetitionVo.getContentId()));
        if(contents.size() == 0){
            return Msg.fail("对应的比赛信息内容不存在");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, sportCompetitionVo, AllClass.SportCompetition.getValue(), null));
    }

    @PutMapping
    @AuthName(name = "put")
    public Msg put(SportCompetitionVo sportCompetitionVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, sportCompetitionVo, AllClass.SportCompetition.getValue(), null));
    }

    @DeleteMapping
    @AuthName(name = "delete")
    public Msg delete(@RequestBody SportCompetitionVo sportCompetitionVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, sportCompetitionVo, AllClass.SportCompetition.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.SportCompetition.getValue());
    }
}
