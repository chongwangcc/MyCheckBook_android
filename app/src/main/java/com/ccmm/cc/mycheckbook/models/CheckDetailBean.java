package com.ccmm.cc.mycheckbook.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 记账明细的值，要获得的明细数据，全部为String类型
 */
public class CheckDetailBean implements Serializable {
    private String incomeType ="支出"; //收入，支出，内部转账
    private String buyType ="一般"; //一般，餐饮，购物，服饰
    private String account ="Inbox";//花销-生活费-现金，投资-股票，投资-货币基金
    private String date ="";//2018-04-07
    private String description =""; //备注信息
    private String moneyStr ="0"; //钱100.00


    private float money=0;

    public void ChoiceStatus(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectDate=sdf.format(dt);
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoneyStr() {
        return moneyStr;
    }

    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
        this.money = Float.valueOf(moneyStr);
    }

    public void setMoney(float money) {
        this.money = money;
    }
    public float getMoney() {
        return money;
    }
    public String toString(){
        String str="";
        str+= date +",";
        str+= incomeType +",";
        str+= moneyStr +",";
        str+= buyType +",";
        str+= account +",";
        str+= description;
        return str;
    }
}
