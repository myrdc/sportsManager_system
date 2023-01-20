package com.gdut.boot.constance.common;

import lombok.Data;

/**
 * @author JLHWASX
 * @Description
 * @verdion
 * @date 2022/1/2923:23
 */

public enum AllClass {
    /**
         账号管理：Account(1),
         考勤管理：AttendanceManege(2),
         教练管理：CoachManage(3),
         比赛管理:CompetitionManage(4),
         一级组织管理：OneOrganization(5),
         权限管理：Permission(6),
         资源管理：Resource(7),
         中间表：ResourceMiddle(8),
         角色管理：Role(9),
         运动员基础信息：SportsBaseMsg(10),
         用户权限管理：UserPermission(11),
         审核管理：Approve(12),
         通知管理：Notice(13),
         入学成绩管理：EntranceExamMsg(14),
         入学信息管理：EntranceInfo(15),
         运动员比赛管理：SportCompetition(16),
         二级组织管理：SecondOrganization(17),
     */
    Account(1),
    AttendanceManege(2),
    CoachManage(3),
    CompetitionManage(4),
    OneOrganization(5),
    Permission(6),
    Resource(7),
    ResourceMiddle(8),
    Role(9),
    SportsBaseMsg(10),
    UserPermission(11),
    Approve(12),
    Notice(13),
    EntranceExamMsg(14),
    EntranceInfo(15),
    SportCompetition(16),
    SecondOrganization(17),
    SportsTeam(18),
    ;

    private int value;

    AllClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
