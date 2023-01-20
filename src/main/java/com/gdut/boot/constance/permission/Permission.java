package com.gdut.boot.constance.permission;

/**
 * @author JLHWASX
 * @Description 权限
 * @verdion
 * @date 2022/1/282:42
 */
public enum Permission {
    sports_in(1, "在校运动员"),
    sports_out(2, "离校运动员"),
    coach(3, "教练"),
    mainCoach(4, "主教练"),
    manager(5, "管理员"),
    superManager(6, "超级管理员"),
    ;
    private Integer id;
    private String permission;

    Permission(Integer id, String permission) {
        this.id = id;
        this.permission = permission;
    }

    public Integer getId() {
        return id;
    }

    public static String getValue(Integer id) {
        if(id == 1){
            return sports_in.permission;
        }else if(id == 2){
            return sports_out.permission;
        }else if(id == 3){
            return coach.permission;
        }else if(id == 4){
            return mainCoach.permission;
        }else if(id == 5){
            return manager.permission;
        }else if(id == 6){
            return superManager.permission;
        }
        return null;
    }

    public static int getType(String name){
        if(sports_in.permission.equals(name)){
            return sports_in.id;
        }else if(sports_out.permission.equals(name)){
            return sports_out.id;
        }else if(coach.permission.equals(name)){
            return coach.id;
        }else if(mainCoach.permission.equals(name)){
            return mainCoach.id;
        }else if(manager.permission.equals(name)){
            return manager.id;
        }else if(superManager.permission.equals(name)){
            return superManager.id;
        }else{
            return -1;
        }
    }

    public static Permission getPermissionById(Integer id) {
        if(id == 1){
            return sports_in;
        }else if(id == 2){
            return sports_out;
        }else if(id == 3){
            return coach;
        }else if(id == 4){
            return mainCoach;
        }else if(id == 5){
            return manager;
        }
        return null;
    }

    public static boolean isValid(String name){
        return sports_in.permission.equals(name) || sports_out.permission.equals(name)
                || coach.permission.equals(name) || mainCoach.permission.equals(name)
                || manager.permission.equals(name) || superManager.permission.equals(name);
    }
}
