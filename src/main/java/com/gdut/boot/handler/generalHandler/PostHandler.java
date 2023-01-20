package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.service.HandlerPostService;
import com.gdut.boot.util.BeanUtils;

/**
 * @author JLHWASX
 * @Description 统一的上传请求
 * @verdion
 * @date 2022/1/2623:10
 */

public class PostHandler extends Handler {

    private static HandlerPostService handlerPostService = null;

    static{
        handlerPostService = BeanUtils.getBean(HandlerPostService.class);
    }

    @Override
    public Msg deal(RequestMessage requestMessage) {
        //不是 POST 请求，就传给下一个 handler 处理
        if(!requestMessage.getReqType().equals(RequestStatus.POST)){
            return this.next.deal(requestMessage);
        }
        //使用Service的save方法进行上传
        return handlerPostService.save(requestMessage);
    }
}
