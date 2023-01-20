package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.service.impl.HandlerDeleteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2722:11
 */

@Service
public interface HandlerDeleteService {

    public Msg delete(RequestMessage requestMessage);

    Msg deleteBatch(List<Integer> ids, int type);
}
