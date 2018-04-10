package com.ccmm.cc.mycheckbook.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class DetailGroupBean {
    private String year;
    private String month;
    private String day;
    private String week; //星期几
    private float total_income=0; //收入总计
    private String date; //日期
    private float total_spent=0; //支出总计
    private String incomeType;// 收入，支出，内部转账
    private String buyType; //零食，居家，娱乐
    private  List<CheckDetailBean> data=new LinkedList<>();


    /***
     * 已第一个的值填写统计值
     * @param detail
     * @return
     */
    public boolean addOneDetailBean(CheckDetailBean detail){
        if(year==null)
            year=detail.getYear();
        if(month==null)
            month=detail.getMonth();
        if(day==null)
            day=detail.getDay();
        if(date==null)
            date=detail.getDate();
        if(week==null){
            //计算week字符串
            try{
                String data_Str=year+"-"+month+"-"+day;
                DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
                Date date = fmt.parse(data_Str);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                week = sdf.format(date);
            }catch (Exception e){

            }

        }
        if(detail.getIncomeType().equals("收入")){
            total_income+=detail.getMoney();
        }else{
            total_spent+=detail.getMoney();
        }
        if(incomeType==null)
            incomeType=detail.getIncomeType();
        if(buyType==null)
            buyType=detail.getBuyType();

        data.add(detail);

        return true;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public float getTotal_income() {
        return total_income;
    }

    public void setTotal_income(float total_income) {
        this.total_income = total_income;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotal_spent() {
        return total_spent;
    }

    public void setTotal_spent(float total_spent) {
        this.total_spent = total_spent;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public List<CheckDetailBean> getData() {
        return data;
    }

    public void setData(List<CheckDetailBean> data) {
        this.data = data;
    }
}
