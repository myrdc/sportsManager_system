package com.gdut.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.annotation.group.NotNeedAuth;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.OneSecondRes;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.entity.OneOrganization;
import com.gdut.boot.entity.SecondOrganization;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.NoticeService;
import com.gdut.boot.service.OneOrganizationService;
import com.gdut.boot.service.SecondOrganizationService;
import com.gdut.boot.vo.NoticeVo;
import com.gdut.boot.vo.OneOrganizationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.gdut.boot.constance.cache.Cache.*;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.util.MessageUtil.toPageContent;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/1323:21
 */

@RestController
@RequestMapping("/api/oneOrganization")
public class OneOrganizationController {

    @Autowired
    private OneOrganizationService oneOrganizationService;

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    static String[] oneInfoListType = null;
    static{
        oneInfoListType = new String[]{"chargeLeader,departmentLeader"};
    }

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.OneOrganization.getValue(),
                toPageContent(map, reflectors.get(AllClass.OneOrganization.getValue()).getEntity(), oneInfoListType)));
    }

    @PostMapping
    @AuthName(name = "post")
    public Msg post(OneOrganizationVo noticeVo, HttpServletRequest request){
        //判断是否已存在
        final List<OneOrganization> name = oneOrganizationService.list(new QueryWrapper<OneOrganization>().eq("name", noticeVo.getName()));
        if(name.size() != 0){
            return Msg.fail("已存在名字的组织");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, noticeVo, AllClass.OneOrganization.getValue(), null));
    }

    @PutMapping
    @AuthName(name = "put")
    public Msg put(OneOrganizationVo noticeVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, noticeVo, AllClass.OneOrganization.getValue(), null));
    }

    @DeleteMapping
    @AuthName(name = "delete")
    public Msg delete(@RequestBody OneOrganizationVo noticeVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, noticeVo, AllClass.OneOrganization.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.OneOrganization.getValue());
    }

    @RequestMapping("getAll")
    @NotNeedAuth
    public Msg getAll(HttpServletRequest request){
        final List<OneOrganization> list = oneOrganizationService.list(new QueryWrapper<>());
        return Msg.success("成功", list);
    }

    @RequestMapping("getAllOneName")
    @NotNeedAuth
    public Msg getAllHasName(HttpServletRequest request){
        final List<OneOrganization> list = oneOrganizationService.list(new QueryWrapper<>());
        Set<String> set = new HashSet<>();
        for (OneOrganization oneOrganization : list) {
            set.add(oneOrganization.getName());
        }
        return Msg.success("成功", set);
    }

    @RequestMapping("getAllTwoName")
    @NotNeedAuth
    public Msg getAllByOneName(String name, HttpServletRequest request){
        final List<SecondOrganization> list = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("one_org", name));
        if(list.isEmpty()){
            return Msg.fail("没有对应的二级组织");
        }
        Set<String> set = new HashSet<>();
        for (SecondOrganization secondOrganization : list) {
            set.add(secondOrganization.getName());
        }
        return Msg.success("成功", set);
    }

    /**
     * 获取所有一级组织和一级组织对应的二级组织
     * @return
     */
    @RequestMapping("getAllOT")
    @NotNeedAuth
    public Msg getAllOT(HttpServletRequest request){
        List<OneSecondRes> res = new LinkedList<>();
        final List<OneOrganization> allOnes = oneOrganizationService.list(new QueryWrapper<>());
        for (OneOrganization allOne : allOnes) {
            final List<SecondOrganization> seconds = secondOrganizationService.list(new QueryWrapper<SecondOrganization>().eq("one_org", allOne.getName()));
            final List<String> names = seconds.stream().map(SecondOrganization::getName).collect(Collectors.toList());
            OneSecondRes oneSecondRes = new OneSecondRes(allOne.getName(),names );
            res.add(oneSecondRes);
        }
        return Msg.success("成功", res);
    }
}
