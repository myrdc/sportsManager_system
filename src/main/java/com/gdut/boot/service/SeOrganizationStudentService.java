package com.gdut.boot.service;

import com.gdut.boot.entity.SeOrganizationStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface SeOrganizationStudentService extends IService<SeOrganizationStudent> {

    void saveSeStu(String inGroup, String sportsProject, Integer id);
}
