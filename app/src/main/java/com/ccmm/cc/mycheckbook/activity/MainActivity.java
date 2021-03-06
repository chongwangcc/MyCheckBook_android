package com.ccmm.cc.mycheckbook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ccmm.cc.mycheckbook.utils.LoginTools;

public class MainActivity extends AppCompatActivity {
    static public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("userStatus", Context.MODE_PRIVATE);
        //1.检查登陆信息
        boolean isLogin = LoginTools.checkDefaultLoginInfo();
        Intent intent = new Intent();
        if(isLogin){
            //登陆未过期，进入选择记账本界面
            intent.setClass(MainActivity.this, CheckbookSelectActivity.class);
        }else{
            //登陆信息过期，显示登陆界面
            intent.setClass(MainActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        MainActivity.this.finish();//如果不关闭当前的会出现好多个页面
    }
}
