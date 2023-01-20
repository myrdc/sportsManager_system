package com.gdut.boot.service;

import org.springframework.stereotype.Service;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/4/1818:53
 */
@Service
public interface VoService {

    public Object toVo(Object record, int type);

    public Object toVoByType(Object record, int type);
}
