package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdut.boot.bean.Msg;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.service.ImgService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.gdut.boot.constance.common.Common.*;
import static com.gdut.boot.constance.cache.Cache.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/2719:07
 */

@Service
public class ImgServiceImpl implements ImgService {

    /**
     * 上传文件得方法, TODO: 不知道要不要写，先留着，看情况
     * @param id 主键
     * @param type 类型
     * @param files 文件，可能是多个
     */
    @Override
    public Msg upload(int id, int type, List<MultipartFile> files) {
        try {

        } catch (Exception e) {
           throw new BusinessException(Msg.fail("图片上传异常"));
        }
        return Msg.success();
    }
}
