package com.gdut.boot.controller;

import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.ApproveService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.vo.ApproveVo;
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
 * @date 2022/2/141:36
 */

@RestController
@RequestMapping("/api/approve")
public class ApproveController {

    @Autowired
    private ApproveService approveService;

    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @GetMapping
    @AuthName(name = "get")
    public Msg get(@RequestParam Map<String, String> map, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.GET, null, AllClass.Approve.getValue(),
                toPageContent(map, reflectors.get(AllClass.Approve.getValue()).getEntity(), null)));
    }


    @PostMapping
    @AuthName(name = "post")
    public Msg post(ApproveVo approveVo, HttpServletRequest request){
        return approveService.post(approveVo);
    }


    //审核被驳回或者审核不通过之后可以对原信息进行修改然后再次进行审核
    @PostMapping("postAgain")
    @AuthName(name = "postAgain")
    public Msg postAgain(ApproveVo approveVo, List<String> approves, HttpServletRequest request){
        return approveService.postAgain(approveVo, approves);
    }

    @PutMapping
    //不可以修改审核人
    @AuthName(name = "put")
    public Msg put(ApproveVo approveVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, approveVo, AllClass.Approve.getValue(), null));
    }

    @DeleteMapping
    //传入id删除
    @AuthName(name = "delete")
    public Msg delete(@RequestBody ApproveVo approveVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, approveVo, AllClass.Approve.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.Approve.getValue());
    }

    /**
     * 分管领导传给下一个
     * @param approveVo
     * @param request
     * @return
     */
//    @PostMapping("next")
//    public Msg next(ApproveVo approveVo, HttpServletRequest request){
//        return approveService.next(approveVo);
//    }

    //管理员完成
    @PutMapping("leaderFinish")
    @AuthName(name = "leaderFinish")
    public Msg finish(ApproveVo approveVo, HttpServletRequest request){
        return approveService.finish(approveVo, request);
    }

    //教练选择撤销
    @PutMapping("undo")
    @AuthName(name = "undo")
    public Msg undo(ApproveVo approveVo){
        return approveService.undo(approveVo);
    }

    //部门负责人或者分管领导选择不通过
    @PutMapping("fail")
    @AuthName(name = "fail")
    public Msg fail(ApproveVo approveVo, HttpServletRequest request){
        return approveService.fail(approveVo, request);
    }
}
