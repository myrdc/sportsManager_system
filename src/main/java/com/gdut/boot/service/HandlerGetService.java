package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import org.springframework.stereotype.Service;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/302:00
 */
@Service
public interface HandlerGetService {

    public Msg get(RequestMessage requestMessage);
}
