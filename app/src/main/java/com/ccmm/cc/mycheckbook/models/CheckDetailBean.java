package com.ccmm.cc.mycheckbook.models;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.utils.DateTools;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 记账明细的值，要获得的明细数据，全部为String类型
 */
public class CheckDetailBean implements Serializable {
    private String id; //这条记录的ID
    private String account_id=""; //这条明细对应账户的ID
    private int isCreditcard; //这条明细是不是信用账户
    private String last_update_user_id;//最后更新用户的ID
    private String checkbook_id;//对应记账本的ID
    private String balanceType = BalanceName.Expend; //收入，支出，内部转账
    private String category =""; //一般，餐饮，购物，服饰
    private String description =""; //备注信息
    private String moneyStr ="0"; //钱100.00
    private float money=0;

    private String year="";
    private String month="";
    private String day="";
    private String date ="";//2018-04-07


    public  CheckDetailBean(){
        setDate(DateTools.getNowDateStr());
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String ss[] = date.split("-");
        this.year=ss[0];
        this.month=ss[1];
        this.day=ss[2];
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
        this.money = Float.parseFloat(String.format("%.2f",Float.valueOf(moneyStr)));
    }

    public void setMoney(float money) {
        float money_1 = Float.parseFloat(String.format("%.2f",money));
        this.money = money_1;
        this.moneyStr = money_1+"";
    }
    public float getMoney() {
        return money;
    }
    public String toString(){
        String str="";
        str+= date +",";
        str+= balanceType +",";
        str+= moneyStr +",";
        str+= category +",";
        str+= description;
        return str;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getIsCreditcard() {
        return isCreditcard;
    }

    public void setIsCreditcard(int isCreditcard) {
        this.isCreditcard = isCreditcard;
    }

    public String getLast_update_user_id() {
        return last_update_user_id;
    }

    public void setLast_update_user_id(String last_update_user_id) {
        this.last_update_user_id = last_update_user_id;
    }

    public String getCheckbook_id() {
        return checkbook_id;
    }

    public void setCheckbook_id(String checkbook_id) {
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
