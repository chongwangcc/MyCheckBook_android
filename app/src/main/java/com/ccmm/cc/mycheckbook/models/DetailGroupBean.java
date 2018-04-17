package com.ccmm.cc.mycheckbook.models;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.utils.AccountTools;
import com.ccmm.cc.mycheckbook.utils.DateTools;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2018/4/6.
 */

public class DetailGroupBean implements Serializable {
    private String year;
    private String month;
    private String day;
    private String week; //星期几
    private float total_income=0; //收入总计
    private String date; //日期
    private float total_spent=0; //支出总计
    private float total_inner=0; //内部转账
    private String balanceType;// 收入，支出，内部转账
    private String categoryType; //零食，居家，娱乐
    private String accountName; //账户名称,
    private String account_id; //账户名称,
    private float cashflow_in=0; //现金流入总计
    private float cashflow_out=0; //现金流出总计
    private float assets_diff=0; //资产差值
    private float liabilities_diff=0; //负债差值
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
        if(account_id==null || account_id.isEmpty())
            account_id=detail.getAccount_id();
        if(accountName==null)
            accountName= AccountTools.concatAccountName(detail.getAccount_id());
        if(week==null){
            //计算week字符串
            String date_Str=year+"-"+month+"-"+day;
            week= DateTools.getDateStrWeek(date_Str);
        }
        if(detail.getBalanceType().equals(BalanceName.Income)){
            total_income+=detail.getMoney();
            if(detail.getIsCreditcard()==0){ //不是债务,是现金
                cashflow_in+=detail.getMoney();
            }
            assets_diff+=detail.getMoney();
        }else if(detail.getBalanceType().equals(BalanceName.Expend)){
            total_spent+=detail.getMoney();
            if(detail.getIsCreditcard()==0){ //不是债务,是现金
                cashflow_out+=detail.getMoney();
                assets_diff-=detail.getMoney();
            }else{
                liabilities_diff+=detail.getMoney();
            }

        }else if(detail.getBalanceType().equals(BalanceName.Inner)){
            total_inner+=detail.getMoney();
        }
        if(balanceType ==null)
            balanceType =detail.getBalanceType();
        if(categoryType ==null)
            categoryType =detail.getCategory();

        data.add(detail);

        return true;
    }


    //////////////////////SET GET 方法/////////////////////////////////

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
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

    public float getTotal_inner() {
        return total_inner;
    }

    public void setTotal_inner(float total_inner) {
        this.total_inner = total_inner;
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
                money=getTotal_inner();
                break;
        }
        return money;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public float getCashflow_in() {
        return cashflow_in;
    }

    public void setCashflow_in(float cashflow_in) {
        this.cashflow_in = cashflow_in;
    }

    public float getCashflow_out() {
        return cashflow_out;
    }

    public void setCashflow_out(float cashflow_out) {
        this.cashflow_out = cashflow_out;
    }

    public float getAssets_diff() {
        return assets_diff;
    }

    public void setAssets_diff(float assets_diff) {
        this.assets_diff = assets_diff;
    }

    public float getLiabilities_diff() {
        return liabilities_diff;
    }

    public void setLiabilities_diff(float liabilities_diff) {
        this.liabilities_diff = liabilities_diff;
    }
}
