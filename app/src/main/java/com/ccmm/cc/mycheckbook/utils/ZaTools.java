package com.ccmm.cc.mycheckbook.utils;

public class ZaTools {

    public static String toHexString(int number,int index){
        String str = Integer.toHexString(number);
        while (str.length()<index){
            str="0"+str;
        }
        return str;
    }
}
