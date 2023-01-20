package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/282:51
 */

public class PermissionHandler extends Handler {

    @Override
    public Msg deal(RequestMessage requestMessage) {
        return next.deal(requestMessage);
    }
}
