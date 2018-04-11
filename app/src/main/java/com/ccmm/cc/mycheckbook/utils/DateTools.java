package com.ccmm.cc.mycheckbook.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateTools {

    public static String getNowDateStr(){

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectDate=sdf.format(dt);
        return selectDate;
    }

    public static String getDateStrWeek(String dateStr){
        try{
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String week = sdf.format(date);
            return week;
        }catch (Exception e){

        }
        return "";
    }

    public static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i <ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    public static String[] toStringArray(List<String> list){
        String[] ret = new String[list.size()];
        for(int i = 0;i <ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }
}
