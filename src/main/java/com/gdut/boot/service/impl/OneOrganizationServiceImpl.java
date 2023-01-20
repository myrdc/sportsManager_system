package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.entity.OneOrganization;
import com.gdut.boot.entity.SecondOrganization;
import com.gdut.boot.service.OneOrganizationService;
import com.gdut.boot.mapper.OneOrganizationMapper;
import com.gdut.boot.service.SecondOrganizationService;
import com.gdut.boot.vo.OneOrganizationVo;
import com.gdut.boot.vo.res.OneOrganizationResVo;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gdut.boot.util.CommonUtil.strToQueue;

/**
 *
 */
@Service
public class OneOrganizationServiceImpl extends ServiceImpl<OneOrganizationMapper, OneOrganization>
    implements OneOrganizationService{

    @Autowired
    private SecondOrganizationService secondOrganizationService;

    @Override
    public void deleteTwoTable(Object o) {
        secondOrganizationService.remove(new QueryWrapper<SecondOrganization>().eq("one_org", ((OneOrganization)o).getName()));
    }

    @Override
    public Object toVo(OneOrganization record) {
        OneOrganizationResVo oneOrganizationVo = new OneOrganizationResVo();
        oneOrganizationVo.setCode(record.getCode());
        oneOrganizationVo.setDetails(record.getDetails());
        oneOrganizationVo.setId(record.getId());
        oneOrganizationVo.setName(record.getName());
        oneOrganizationVo.setEngName(record.getEngName());
        oneOrganizationVo.setSavePlace(record.getSavePlace());
        oneOrganizationVo.setSavePlaceChoice(record.getSavePlaceChoice());
        oneOrganizationVo.setSimpleName(record.getSimpleName());
        oneOrganizationVo.setChargeLeader(strToQueue(record.getChargeLeader()));
        oneOrganizationVo.setDepartmentLeader(strToQueue(record.getDepartmentLeader()));
        return oneOrganizationVo;
    }
}




