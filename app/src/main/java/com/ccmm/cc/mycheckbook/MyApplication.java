package com.ccmm.cc.mycheckbook;

import android.app.Application;
import android.content.Context;

/**
 * Created by cc on 2018/4/6.
 */

public class MyApplication extends Application {
    private  static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}