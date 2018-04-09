package com.ccmm.cc.mycheckbook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.ccmm.cc.mycheckbook.MyApplication;
import com.ccmm.cc.mycheckbook.activity.MainActivity;
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
        //1.读配置文件，取出登陆信息
        String name = MainActivity.sharedPreferences.getString("username", "");
        String password = MainActivity.sharedPreferences.getString("password", "");

        //2.检验登陆信息是否正确
        b= checkPassword(name,password);

        //3.创建全局的user实体
        if(b){
            setLoginUser(name,password);
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
        boolean b = false;
        //1.TODO 判断用户名密码是否正确
        if(username.equals("cc") && password.equals("mm")){
            b=true;
        }

        return b;
    }

    public static boolean setLoginUser(String username,String password){
        if(username==null) return false;
        //1.创建全局的user实体
        UserEntity user = new UserEntity();
        user.setName(username);
        user.setDescription("用户名");
        loginUser = user;

        //2.用户名写入配置文件
        SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
        return true;
    }

    public static UserEntity getLoginUser() {
        return loginUser;
    }
}
