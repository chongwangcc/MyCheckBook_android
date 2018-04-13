package com.ccmm.cc.mycheckbook.models;

/**
 * Created by cc on 2018/4/6.
 */

public class AccountBean {
    private int account_id=-1;
    private String name="";//账户名称，例如：花销账户-生活费，花销账户-doodas等
    private int parent_id=-1;
    private int level=-1;
    private String key="";
    private double assets_nums=0;
    private double liablities_num=0;

    public String toString(){
        return name;
    }

    ////////////////////////// SET GET 方法///////////////////////////////////////////////
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getAssets_nums() {
        return assets_nums;
    }

    public void setAssets_nums(double assets_nums) {
        this.assets_nums = assets_nums;
    }

    public double getLiablities_num() {
        return liablities_num;
    }

    public void setLiablities_num(double liablities_num) {
        this.liablities_num = liablities_num;
    }
}
