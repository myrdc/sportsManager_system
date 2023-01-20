package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2719:07
 */
@Service
public interface ImgService {

    public Msg upload(int id, int type, List<MultipartFile> files);
}
