package com.ccmm.cc.mycheckbook.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import java.util.UUID;
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

    public static String formatMoneyStr(double money,String balanceType){
        String flag="";
        switch (balanceType){
            case BalanceName.Expend:
                if(money>=0)
                    flag="-";
                break;
            case BalanceName.Income:
                if(money>=0)
                    flag="+";
                break;
            case BalanceName.Inflow:
                flag="";
                break;
            case BalanceName.Outflow:
                flag="";
                break;
        }
        String money_str=String.format("%.2f", money);
        String result = flag+money_str;
        return result;
    }

    public static String makeDetailDescription(CheckDetailBean details){
        String result="";
        if(details==null) return result;
        if(details.getIsCreditcard()==1){
            result+="[债]";
        }
        if(details.getDescription()==null || details.getDescription().isEmpty()){
            result+=details.getCategory();
        }else{
            result+=details.getDescription();
        }
        return result;

    }

    /***
     * 生成UUID
     * @return
     */
    public static String genNewUUID(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }
}
