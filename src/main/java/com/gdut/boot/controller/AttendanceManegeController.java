package com.gdut.boot.controller;

import com.gdut.boot.annotation.group.AuthName;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.AttendanceManegeService;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.vo.AttendanceManegeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.gdut.boot.constance.cache.Cache.HANDLERS;
import static com.gdut.boot.constance.common.Common.INVOKE_LINK;
import static com.gdut.boot.util.MessageUtil.toReqestMessage;

/**
 * @author JLHWASX
 * @Description 考勤管理
 * @verdion
 * @date 2022/4/216:09
 */

@RestController
@RequestMapping("/api/attendanceManege")
@Slf4j
public class AttendanceManegeController {


    @Autowired
    private HandlerDeleteService handlerDeleteService;

    @Autowired
    private AttendanceManegeService attendanceManegeService;

    @RequestMapping("getByCoach")
    @AuthName(name = "getByCoach")
    public Msg getByCoach(@RequestParam String coach, HttpServletRequest request){
        log.debug("错误");
        return attendanceManegeService.getByCoach(coach);
    }

    @PostMapping()
    @AuthName(name = "post")
    public Msg post(AttendanceManegeVo attendanceManegeVo, HttpServletRequest request){
        if(attendanceManegeVo.getStuNumber() == null){
            return Msg.fail("学号不能为空");
        }
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.POST, attendanceManegeVo, AllClass.AttendanceManege.getValue(), null));
    }

    @DeleteMapping()
    @AuthName(name = "delete")
    public Msg delete(@RequestBody AttendanceManegeVo attendanceManegeVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.DELETE, attendanceManegeVo, AllClass.AttendanceManege.getValue(), null));
    }

    @PutMapping()
    @AuthName(name = "put")
    public Msg put(AttendanceManegeVo attendanceManegeVo, HttpServletRequest request){
        return HANDLERS.get(INVOKE_LINK).deal(toReqestMessage(request, RequestStatus.PUT, attendanceManegeVo, AllClass.AttendanceManege.getValue(), null));
    }

    @DeleteMapping("batchDelete")
    @AuthName(name = "batchDelete")
    public Msg batchDelete(@RequestBody List<Integer> ids, HttpServletRequest request){
        return handlerDeleteService.deleteBatch(ids, AllClass.AttendanceManege.getValue());
    }
}
