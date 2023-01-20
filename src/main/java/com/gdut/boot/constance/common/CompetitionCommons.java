package com.gdut.boot.constance.common;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/9/117:30
 */

public class CompetitionCommons {

    //比赛信息等待审核
    public static final String COMPETITION_APPROVE_WAIT = "1";
    //比赛信息审核通过
    public static final String COMPETITION_APPROVE_FINISH = "2";
    //比赛信息审核不通过
    public static final String COMPETITION_APPROVE_FAIL = "3";

    //审核撤销
    public static final String APPROVE_UNDO = "-1";
    //等待审核
    public static final String APPROVE_WAIT = "0";
    //审核通过
    public static final String APPROVE_SUCCESS = "1";
    //审核不通过
    public static final String APPROVE_FAIL = "2";

    //已审核
    public static final int APPROVE_STATE_DO = 1;
    //未审核
    public static final int APPROVE_STATE_UNDO = 2;

    //已读
    public static final int READ = 1;
    //未读
    public static final int UNREAD = 2;
}
