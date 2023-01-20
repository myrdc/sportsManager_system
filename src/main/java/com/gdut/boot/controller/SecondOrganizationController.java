package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.OneOrganization;
import com.gdut.boot.entity.SecondOrganization;
import com.gdut.boot.mapper.SecondOrganizationMapper;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.NoticeService;
import com.gdut.boot.service.OneOrganizationService;
import com.gdut.boot.service.SecondOrganizationService;
import com.gdut.boot.vo.OneOrganizationVo;
import com.gdut.boot.vo.SecondOrganizationVo;
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
 * @date 2022/5/1323:48
 */

@RestController
@RequestMapping("/api/secondOrganization")
public class SecondOrganizationController {

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @Autowired
    private OneOrganizationService oneOrganizationService;

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.SecondOrganization.getValue(),
                toPageContent(map, reflectors.get(AllClass.SecondOrganization.getValue()).getEntity(), null)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(SecondOrganizationVo secondOrganizationVo, HttpServletRequest request){
        final List<OneOrganization> oneOrganizations = oneOrganizationService.list(new QueryWrapper<OneOrganization>().eq("name", secondOrganizationVo.getOneOrg()));
        if(oneOrganizations.size() == 0){
            return Msg.fail("一级组织不存在");
        }
        final List<SecondOrganization> name = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("name", secondOrganizationVo.getName()).eq("one_org", secondOrganizationVo.getOneOrg()));
        if(name.size() != 0){
            return Msg.fail("二级组织名字重复");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, secondOrganizationVo, AllClass.SecondOrganization.getValue(), null));
    }

    @PutMapping
    @AuthName(name = "put")
    public Msg put(SecondOrganizationVo secondOrganizationVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, secondOrganizationVo, AllClass.SecondOrganization.getValue(), null));
    }

    @DeleteMapping
    @AuthName(name = "delete")
    public Msg delete(@RequestBody SecondOrganizationVo secondOrganizationVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, secondOrganizationVo, AllClass.SecondOrganization.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.SecondOrganization.getValue());
    }

    //查询二级组织下面的学生
    @RequestMapping("getStuById")
    @AuthName(name = "getStuById")
    public Msg getStuById(@RequestParam("id") Integer id,
                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                          @RequestParam(value = "size", required = false, defaultValue = "10")Integer size
            , HttpServletRequest request){
        return secondOrganizationService.selectStusById(id, page, size);
    }

    //查询二级组织下面的教练
    @RequestMapping("getCoachById")
    @AuthName(name = "getCoachById")
    public Msg getCoachById(@RequestParam("id") Integer id,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "10")Integer size
                             , HttpServletRequest request){
        return secondOrganizationService.selectCoachById(id, page, size);
    }
}
