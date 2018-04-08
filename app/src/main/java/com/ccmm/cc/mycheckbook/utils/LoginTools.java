package com.ccmm.cc.mycheckbook.utils;

import com.ccmm.cc.mycheckbook.models.UserEntity;

/**
 * 登陆用的工具类
 * Created by cc on 2018/4/5.
 */

public class LoginTools {
    private static UserEntity loginUser=null;
    /**
     * 检查系统保存的登陆信息是否过期
     * @return
     */
    public static boolean checkDefaultLoginInfo(){
        boolean b=true;
        //1.TODO 读数据库检查登陆信息


        //2.创建全局的user实体
        if(b){
            UserEntity user = new UserEntity();
            user.setName("cc");
            user.setDescription("用户名");
            loginUser = user;
        }

        return b;
    }

    /***
     * 检查用户名密码是否正确
     * @param username
     * @param password
     * @return
     */
    public static boolean checkPassword(String username,String password){
        if(username==null || password==null) return false;
        if(username.length()==0 || password.length()==0) return false;
        boolean b = true;
        //1.判断用户名密码是否正确

        //2.创建全局的user实体
        if(b){
            UserEntity user = new UserEntity();
            user.setName(username);
            user.setDescription("用户名");
            loginUser = user;
        }
        return b;
    }

    public static UserEntity getLoginUser() {
        return loginUser;
    }
}
