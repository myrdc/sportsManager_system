package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.EntranceExamMsg;
import com.gdut.boot.service.EntranceExamMsgService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.vo.EntranceExamMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.util.MessageUtil.toPageContent;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * 
 * @TableName entrance_exam_msg
 */

@RestController
@RequestMapping("/api/entranceExam")
public class EntranceExamMsgController implements Serializable {

    @Autowired
    private EntranceExamMsgService entranceExamMsgService;

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.EntranceExamMsg.getValue(),
                toPageContent(map, reflectors.get(AllClass.EntranceExamMsg.getValue()).getEntity(), null)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(EntranceExamMsgVo entranceExamMsgVo, HttpServletRequest request){
        //判断这个学号有没有数据了
        List<EntranceExamMsg> number = entranceExamMsgService.list(new QueryWrapper<EntranceExamMsg>().eq("number", entranceExamMsgVo.getNumber()));
        if(number.size() != 0){
            return Msg.fail("添加失败，已经有相同学号的数据存在了");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, entranceExamMsgVo, AllClass.EntranceExamMsg.getValue(), null));
    }

    @PutMapping
    @AuthName(name = "put")
    public Msg put(EntranceExamMsgVo entranceExamMsgVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, entranceExamMsgVo, AllClass.EntranceExamMsg.getValue(), null));
    }

    @DeleteMapping
    @AuthName(name = "delete")
    public Msg delete(@RequestBody EntranceExamMsgVo entranceExamMsgVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, entranceExamMsgVo, AllClass.EntranceExamMsg.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.EntranceExamMsg.getValue());
    }
}