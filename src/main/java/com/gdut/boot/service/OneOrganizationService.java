package com.gdut.boot.service;

import com.gdut.boot.entity.OneOrganization;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface OneOrganizationService extends IService<OneOrganization> {

    void deleteTwoTable(Object o);

    Object toVo(OneOrganization record);
}
