package com.ccmm.cc.mycheckbook.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 记账明细的值，要获得的明细数据，全部为String类型
 */
public class CheckDetailBean implements Serializable {
    private int id; //这条记录的ID
    private int account_id; //这条明细对应账户的ID
    private int isCreditcard; //这条明细是不是信用账户
    private int last_update_user_id;//最后更新用户的ID
    private int checkbook_id;//对应记账本的ID
    private String incomeType ="支出"; //收入，支出，内部转账
    private String buyType ="一般"; //一般，餐饮，购物，服饰
    private String description =""; //备注信息
    private String moneyStr ="0"; //钱100.00
    private float money=0;


    private String year="";
    private String month="";
    private String day="";
    private String account ="Inbox";//花销-生活费-现金，投资-股票，投资-货币基金
    private String date ="";//2018-04-07


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
        String ss[] = date.split("-");
        this.year=ss[0];
        this.month=ss[1];
        this.date=ss[2];
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
        this.moneyStr = money+"";
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getIsCreditcard() {
        return isCreditcard;
    }

    public void setIsCreditcard(int isCreditcard) {
        this.isCreditcard = isCreditcard;
    }

    public int getLast_update_user_id() {
        return last_update_user_id;
    }

    public void setLast_update_user_id(int last_update_user_id) {
        this.last_update_user_id = last_update_user_id;
    }

    public int getCheckbook_id() {
        return checkbook_id;
    }

    public void setCheckbook_id(int checkbook_id) {
        this.checkbook_id = checkbook_id;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }
}
