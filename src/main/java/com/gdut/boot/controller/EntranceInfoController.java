package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.EntranceInfo;
import com.gdut.boot.service.EntranceInfoService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.vo.EntranceInfoVo;
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
@RequestMapping("/api/entranceInfo")
public class EntranceInfoController implements Serializable {

    @Autowired
    EntranceInfoService entranceInfoService;

    @Autowired
    HandlerDeleteService handlerDeleteService;

    static String[] entranceInfoListType = null;
    static{
        entranceInfoListType = new String[]{"belongCoach"};
    }

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.EntranceInfo.getValue(),
                toPageContent(map, reflectors.get(AllClass.EntranceInfo.getValue()).getEntity(), entranceInfoListType)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(EntranceInfoVo entranceInfoVo, HttpServletRequest request){
        //判断这个学号有没有数据了
        List<EntranceInfo> number = entranceInfoService.list(new QueryWrapper<EntranceInfo>().eq("number", entranceInfoVo.getNumber()));
        if(number.size() != 0){
            return Msg.fail("添加失败，已经有相同学号的数据存在了");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, entranceInfoVo, AllClass.EntranceInfo.getValue(), null));
    }

    @PutMapping
    @AuthName(name = "put")
    public Msg put(EntranceInfoVo entranceInfoVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, entranceInfoVo, AllClass.EntranceInfo.getValue(), null));
    }

    @DeleteMapping
    @AuthName(name = "delete")
    public Msg delete(@RequestBody EntranceInfoVo entranceInfoVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, entranceInfoVo, AllClass.EntranceInfo.getValue(), null));
    }

    @DeleteMapping("deleteBatch")
    @AuthName(name = "deleteBatch")
    public Msg deleteBatch(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.EntranceInfo.getValue());
    }

}