package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.handler.reflect.MyReflector;
import org.springframework.stereotype.Service;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2022/1/27 19:41
 */

@Service
public interface HandlerPostService {

    public Msg save(RequestMessage requestMessage);

    public RequestMessage dealWithImgAndFile(RequestMessage requestMessage, MyReflector reflector) throws Exception;
}
