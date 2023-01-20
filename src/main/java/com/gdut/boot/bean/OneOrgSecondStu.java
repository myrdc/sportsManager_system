package com.gdut.boot.bean;

import com.gdut.boot.entity.OneOrganization;
import com.gdut.boot.entity.SecondOrganization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/3119:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneOrgSecondStu {
    //一级组织
    private String oneOrganizations;
    //二级组织bean(二级组织 + 二级组织下的运动员)
    private List<SecondOrgStu> secondOrganizations;

}
