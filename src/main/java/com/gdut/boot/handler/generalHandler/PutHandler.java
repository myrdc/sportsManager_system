package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.HandlerPutService;
import com.gdut.boot.util.BeanUtils;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/26 23:09
 */

public class PutHandler extends Handler {

    private static HandlerPutService handlerPutService = null;

    static{
        handlerPutService = BeanUtils.getBean(HandlerPutService.class);
    }

    @Override
    public Msg deal(RequestMessage requestMessage) {
        //不是 GET 请求，就传给下一个 handler 处理
        if(!requestMessage.getReqType().equals(RequestStatus.PUT)){
            return this.next.deal(requestMessage);
        }
        return handlerPutService.put(requestMessage);
    }
}
