package com.ccmm.cc.mycheckbook.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class ZaTools {

    public static String toHexString(int number,int index){
        String str = Integer.toHexString(number);
        while (str.length()<index){
            str="0"+str;
        }
        return str;
    }

    public static void showErrorMessage(Context context,String errorInfo){
        Toast toast = Toast.makeText(context, errorInfo, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);  //设置显示位置
        TextView vi =  toast.getView().findViewById(android.R.id.message);
        vi.setTextColor(Color.RED);     //设置字体颜色
        toast.show();
    }
}
