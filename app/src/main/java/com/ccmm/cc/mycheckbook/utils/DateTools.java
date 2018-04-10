package com.ccmm.cc.mycheckbook.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
