package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.service.HandlerGetService;
import com.gdut.boot.util.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author JLHWASX
 * @Description GET 请求只能处理一些比较单一的请求
 * @verdion
 * @date 2022/1/2623:09
 */

public class GetHandler extends Handler {

    private static HandlerGetService handlerGetService = null;

    static{
        handlerGetService = BeanUtils.getBean(HandlerGetService.class);
    }

    @Override
    public Msg deal(RequestMessage requestMessage) {
        if(requestMessage.getReqType() != RequestStatus.GET){
            return next.deal(requestMessage);
        }
        return handlerGetService.get(requestMessage);
    }
}
