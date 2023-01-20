package com.gdut.boot.controller;

import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.NoticeService;
import com.gdut.boot.vo.CoachManageVo;
import com.gdut.boot.vo.NoticeVo;
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
 * @date 2022/2/141:33
 */

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.Notice.getValue(),
                toPageContent(map, reflectors.get(AllClass.Notice.getValue()).getEntity(), null)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(NoticeVo noticeVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, noticeVo, AllClass.Notice.getValue(), null));
    }

    @PutMapping
    @AuthName(name = "put")
    public Msg put(NoticeVo noticeVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, noticeVo, AllClass.Notice.getValue(), null));
    }

    @DeleteMapping
    @AuthName(name = "delete")
    public Msg delete(@RequestBody NoticeVo noticeVo, HttpServletRequest request){
        if(noticeVo.getId() == 0){
            return Msg.fail("传入的id不能为空");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, noticeVo, AllClass.Notice.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.Notice.getValue());
    }

    @GetMapping("getContentById")
    @AuthName(name = "getContentById")
    public Msg getById(int id, int type){
        return noticeService.getContentById(id, type);
    }
}
