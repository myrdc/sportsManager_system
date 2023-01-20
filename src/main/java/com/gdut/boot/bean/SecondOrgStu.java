package com.gdut.boot.bean;

import com.gdut.boot.entity.SportsBaseMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/9/2521:46
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondOrgStu {
    //二级组织名
    private String secondOrgName;
    //二级组织id
    private Integer secondId;
    //二级组织下面的运动员
    private List<SportsBaseMsg> sportsBaseMsgs;
}
