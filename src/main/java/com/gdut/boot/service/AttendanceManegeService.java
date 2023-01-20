package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.AttendanceManege;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface AttendanceManegeService extends IService<AttendanceManege> {

    Msg getByCoach(String coach);
}
