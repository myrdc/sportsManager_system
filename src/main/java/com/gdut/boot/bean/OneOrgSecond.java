package com.gdut.boot.bean;

import com.gdut.boot.entity.OneOrganization;
import com.gdut.boot.entity.SecondOrganization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/5/3119:59
 */

@Data
@AllArgsConstructor
public class OneOrgSecond {
    //一级组织
    private LinkedList<OneOrganization> oneOrganizations;
    //二级组织
    private LinkedList<SecondOrganization> secondOrganizations;

    public OneOrgSecond() {
        oneOrganizations = new LinkedList<>();
        secondOrganizations = new LinkedList<>();
    }
}
