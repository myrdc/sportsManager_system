package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.entity.Approve;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.boot.vo.ApproveVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Service
public interface ApproveService extends IService<Approve> {

    void postApproves(int type, int entityId, RequestMessage message);

    Msg post(ApproveVo approveVo);

    Msg next(ApproveVo approveVo);

    Msg get(String number, int pn, int size);

    Msg finish(ApproveVo approveVo, HttpServletRequest request);

    Msg undo(ApproveVo approveVo);

    Msg fail(ApproveVo approveVo, HttpServletRequest request);

    Msg postAgain(ApproveVo approveVo, List<String> approves);
}
