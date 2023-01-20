package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.HandlerDeleteService;
import com.gdut.boot.util.BeanUtils;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2623:10
 */

public class DeleteHandler extends Handler {

    private static HandlerDeleteService handlerDeleteService = null;

    static{
        handlerDeleteService = BeanUtils.getBean(HandlerDeleteService.class);
    }


    @Override
    public Msg deal(RequestMessage requestMessage) {
        //不是 GET 请求，就传给下一个 handler 处理
        if(!requestMessage.getReqType().equals(RequestStatus.DELETE)){
            return this.next.deal(requestMessage);
        }
        return handlerDeleteService.delete(requestMessage);
    }
}
