package com.ccmm.cc.mycheckbook.models;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.utils.DateTools;

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
    private String balanceType;// 收入，支出，内部转账
    private String categoryType; //零食，居家，娱乐
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
            String date_Str=year+"-"+month+"-"+day;
            week= DateTools.getDateStrWeek(date_Str);
        }
        if(detail.getBalanceType().equals(BalanceName.Income)){
            total_income+=detail.getMoney();
        }else if(detail.getBalanceType().equals(BalanceName.Expend)){
            total_spent+=detail.getMoney();
        }
        if(balanceType ==null)
            balanceType =detail.getBalanceType();
        if(categoryType ==null)
            categoryType =detail.getCategory();

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

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public List<CheckDetailBean> getData() {
        return data;
    }

    public void setData(List<CheckDetailBean> data) {
        this.data = data;
    }

    public float getMoney(){
        float money=0;
        switch (this.getBalanceType()){
            case BalanceName.Expend:
                money= getTotal_spent();
                break;
            case BalanceName.Income:
                money=getTotal_income();
                break;
            case BalanceName.Inner:
                break;
        }
        return money;
    }
}
