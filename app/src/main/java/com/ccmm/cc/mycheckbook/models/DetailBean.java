package com.ccmm.cc.mycheckbook.models;

/**
 * Created by cc on 2018/4/6.
 */

public class DetailBean {
    private String date; //日期
    private String incomeType;// 收入，支出，内部转账
    private String description ; //描述信息
    private float money;
    private String buyType; //零食，居家，娱乐
    private AccountBean account; //所属账户

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }
}
