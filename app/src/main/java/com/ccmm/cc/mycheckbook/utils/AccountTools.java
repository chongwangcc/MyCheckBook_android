package com.ccmm.cc.mycheckbook.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccmm.cc.mycheckbook.Enum.SqliteTableName;
import com.ccmm.cc.mycheckbook.MyApplication;
import com.ccmm.cc.mycheckbook.models.AccountBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AccountTools {
    static private DBHelper helper=new DBHelper(MyApplication.getContext());
    static private SQLiteDatabase write_db=helper.getWritableDatabase();   //写数据库
    static private SQLiteDatabase read_db=helper.getReadableDatabase();   //读数据库
    static public  AccountBean defaultAccount ;
    static{
        getAccountList(CheckbookTools.getSelectedCheckbook().getCheckbookID());
    }
    /**
     * 获得记账本下的所有账户
     * @param checkbook_id
     * @return
     */
    public static List<AccountBean> getAccountList(int checkbook_id){
        List<AccountBean> accountList = new LinkedList<>();
        //2.查询SQLlite，获得所有账户
        String sql="select * from "+SqliteTableName.CheckbookAccountMap+" where checkbook_id="+checkbook_id+"";
        try{
            Cursor userCheckbookMapCursor = read_db.rawQuery(sql,null);
            while (userCheckbookMapCursor.moveToNext()){
                int id = userCheckbookMapCursor.getInt(userCheckbookMapCursor.getColumnIndex("account_id"));
                AccountBean entity = getAccountByID(id);
                if(entity!=null && entity.getAccount_id()>=0)
                    accountList.add(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //3.如果账户为空,新增默认账户
        if(accountList==null || accountList.isEmpty()){
            AccountBean bean = new AccountBean();
            bean.setLevel(1);
            bean.setName("未分类");
            bean.setParent_id(-1);
            bean.setLiablities_num(0);
            bean.setAssets_nums(0);
            saveAccountToSqlite(bean);
            accountList.add(bean);
        }

        //4.填写默认账户
        for(AccountBean bean:accountList){
            if(bean.getName().equals("未分类")){
                defaultAccount=bean;
                break;
            }
        }
        return accountList;
    }

    /***
     * 都数据库,获得account信息
     * @param account_id
     * @return
     */
    public static AccountBean getAccountByID(int account_id){
        AccountBean bean = new AccountBean();
        try{
            String sql_2 = "select * from "+ SqliteTableName.AccountInfo+" where account_id="+account_id;
            Cursor temp = read_db.rawQuery(sql_2,null);
            temp.moveToFirst();
            bean.setName(temp.getString(temp.getColumnIndex("account_name")));
            bean.setAccount_id(temp.getInt(temp.getColumnIndex("account_id")));
            bean.setParent_id(temp.getInt(temp.getColumnIndex("parent_id")));
            bean.setLevel(temp.getInt(temp.getColumnIndex("level")));
            bean.setKey(temp.getString(temp.getColumnIndex("key")));
            bean.setAssets_nums(temp.getFloat(temp.getColumnIndex("assets_nums")));
            bean.setLiablities_num(temp.getFloat(temp.getColumnIndex("liabilities_nums")));
        }catch (Exception e){
            e.printStackTrace();
            bean=null;
        }
        return bean;
    }

    /***
     * 保存账户信息,新增id
     * @param account
     * @return
     */
    public static int saveAccountToSqlite(AccountBean account){
        int strid = saveAccountToSqlite(account, true);
        return strid;
    }

    /***
     * 保存账户信息,新增id
     * @param account
     * @param isNew
     * @return
     */
    public static int saveAccountToSqlite(AccountBean account,boolean isNew){
        ContentValues values = new ContentValues();
        if(!isNew && account.getAccount_id()>0){
            values.put("account_id",account.getAccount_id());
        }

        values.put("account_name",account.getName());
        values.put("parent_id",account.getParent_id());
        values.put("level",account.getLevel());
        values.put("key",account.getKey());
        values.put("assets_nums",account.getAssets_nums());
        values.put("liabilities_nums",account.getLiablities_num());

        write_db.insert(SqliteTableName.AccountInfo,null,values);
        Cursor cursor = write_db.rawQuery("select last_insert_rowid() from "+SqliteTableName.CheckbookInfo,null);
        int strid=-1;
        if(cursor.moveToFirst())
            strid = cursor.getInt(0);
        account.setAccount_id(strid);
        return strid;
    }

    /***
     * 新增一个账户信息
     * @param checkbook
     * @param account
     */
    public static void addOneAccount(CheckbookEntity checkbook,AccountBean account){
        //1.保存Checkbook实体 到数据库
        int id=saveAccountToSqlite(account);
        account.setAccount_id(id);

        //2.建立checkbook和account的对应管理
        ContentValues values_r = new ContentValues();
        values_r.put("account_id",id);
        values_r.put("checkbook_id",checkbook.getCheckbookID());
        write_db.insert(SqliteTableName.CheckbookAccountMap,null,values_r);
    }

    /***
     * 账户名称是否被使用过
     * @param account_name
     * @param accountBeans
     * @return
     */
    public static boolean isNamesUsed(String account_name,List<AccountBean> accountBeans){
        if(accountBeans==null) return false;
        for(AccountBean account:accountBeans){
            if(account.getName().equals(account_name)){
                return true;
            }
        }
        return false;

    }

    /***
     * 找到一个账户的父节点
     * @param account
     * @return
     */
    public static AccountBean findParentAccount(AccountBean account,List<AccountBean> accountList){
        if(account == null || accountList==null) return null;
        if(account.getParent_id() <0) return null;
        for(AccountBean bean:accountList){
            if(bean.getAccount_id() == account.getParent_id()){
                return bean;
            }
        }
        return null;
    }


    public static String concatAccountName(int account_id){
        String name="";
        try{
            AccountBean bean=  getAccountByID(account_id);
            name=bean.getName();
            AccountBean parent=  getAccountByID(bean.getParent_id());
            name=parent.getName()+"-"+name;
        }catch(Exception e){

        }
        return name;
    }
}
