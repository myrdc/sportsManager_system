package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface NoticeService extends IService<Notice> {

    Msg getContentById(int contentId, int type);
}
