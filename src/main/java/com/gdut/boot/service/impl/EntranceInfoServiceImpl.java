package com.gdut.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.boot.entity.EntranceInfo;
import com.gdut.boot.service.EntranceInfoService;
import com.gdut.boot.mapper.EntranceInfoMapper;
import com.gdut.boot.vo.EntranceInfoVo;
import com.gdut.boot.vo.res.EntranceInfoResultVo;
import org.springframework.stereotype.Service;

import static com.gdut.boot.util.CommonUtil.strToQueue;

/**
 *
 */
@Service
public class EntranceInfoServiceImpl extends ServiceImpl<EntranceInfoMapper, EntranceInfo>
    implements EntranceInfoService{

    @Override
    public EntranceInfoResultVo toVo(EntranceInfo entranceInfo) {
        EntranceInfoResultVo ev = new EntranceInfoResultVo();
        ev.setId(entranceInfo.getId());
        ev.setNumber(entranceInfo.getNumber());
        ev.setAcceptanceType(entranceInfo.getAcceptanceType());
        ev.setEntranceTime(entranceInfo.getEntranceTime());
        ev.setOriginalSchool(entranceInfo.getOriginalSchool());
        ev.setTotalCulturalScore(entranceInfo.getTotalCulturalScore());
        ev.setTakingProfessional(entranceInfo.getTakingProfessional());
        ev.setIngCollege(entranceInfo.getIngCollege());
        ev.setLevelClass(entranceInfo.getLevelClass());
        ev.setExaminationPlace(entranceInfo.getExaminationPlace());
        ev.setAddedScore(entranceInfo.getAddedScore());
        ev.setStudentCardNum(entranceInfo.getStudentCardNum());
        ev.setStudentCard(entranceInfo.getStudentCard());
        ev.setBelongCoach(strToQueue(entranceInfo.getBelongCoach()));
        return ev;
    }
}




