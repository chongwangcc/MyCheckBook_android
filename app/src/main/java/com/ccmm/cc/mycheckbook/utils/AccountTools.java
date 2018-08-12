package com.ccmm.cc.mycheckbook.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccmm.cc.mycheckbook.Enum.SqliteTableName;
import com.ccmm.cc.mycheckbook.MyApplication;
import com.ccmm.cc.mycheckbook.models.AccountBean;
import java.util.LinkedList;
import java.util.List;

public class AccountTools {
    static private DBHelper helper=new DBHelper(MyApplication.getContext());
    static private SQLiteDatabase write_db=helper.getWritableDatabase();   //写数据库
    static private SQLiteDatabase read_db=helper.getReadableDatabase();   //读数据库
    static public  AccountBean defaultAccount ;
    static public AccountBean deleteAccountCacher;

    static{
        getAccountList(CheckbookTools.getSelectedCheckbook().getCheckbookID());
    }
    /**
     * 获得记账本下的所有账户
     * @param checkbook_id
     * @return
     */
    public static List<AccountBean> getAccountList(String checkbook_id){
        List<AccountBean> accountList = new LinkedList<>();
        //2.查询SQLlite，获得所有账户

        String sql="select * from "+SqliteTableName.CheckbookAccountMap+" where checkbook_id='"+checkbook_id+"'";


        Cursor userCheckbookMapCursor = read_db.rawQuery(sql,null);
        while (userCheckbookMapCursor.moveToNext()){
            String id = userCheckbookMapCursor.getString(userCheckbookMapCursor.getColumnIndex("account_id"));
            AccountBean entity = getAccountByID(id);
            if(entity!=null && entity.getAccount_id()!=null && !entity.getAccount_id().isEmpty())
                accountList.add(entity);
        }
        //3.如果账户为空,新增默认账户
        if(accountList==null || accountList.isEmpty()){
            AccountBean bean = new AccountBean();
            bean.setLevel(1);
            bean.setName("未分类");
            bean.setParent_id("");
            bean.setLiablities_num(0);
            bean.setAssets_nums(0);
            addOneAccount(checkbook_id,bean);
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
    public static AccountBean getAccountByID(String account_id){
        if(account_id==null || account_id.isEmpty())
            return null;
        AccountBean bean = new AccountBean();
        String sql_2 = "select * from "+ SqliteTableName.AccountInfo+" where account_id='"+account_id+"'";
        Cursor temp = read_db.rawQuery(sql_2,null);
        System.out.print(sql_2);
        if( temp != null &&  temp.moveToFirst() ){
            bean.setName(temp.getString(temp.getColumnIndex("account_name")));
            bean.setAccount_id(temp.getString(temp.getColumnIndex("account_id")));
            bean.setParent_id(temp.getString(temp.getColumnIndex("parent_id")));
            bean.setLevel(temp.getInt(temp.getColumnIndex("level")));
            bean.setKey(temp.getString(temp.getColumnIndex("key")));
            bean.setAssets_nums(temp.getFloat(temp.getColumnIndex("assets_nums")));
            bean.setLiablities_num(temp.getFloat(temp.getColumnIndex("liabilities_nums")));
        }
        return bean;
    }

    /***
     * 保存账户信息,新增id
     * @param account
     * @return
     */
    public static String saveAccountToSqlite(AccountBean account){
        String strid = saveAccountToSqlite(account, true);
        return strid;
    }

    /***
     * 保存账户信息,新增id
     * @param account
     * @param isNew
     * @return
     */
    public static String saveAccountToSqlite(AccountBean account,boolean isNew){
        ContentValues values = new ContentValues();
        String strid="";
        if(!isNew && account.getAccount_id()!=null && !account.getAccount_id().isEmpty()){
            strid=account.getAccount_id();
        }else{
            strid=ZaTools.genNewUUID();
        }
        values.put("account_id",strid);
        values.put("account_name",account.getName());
        values.put("parent_id",account.getParent_id());
        values.put("level",account.getLevel());
        values.put("key",account.getKey());
        values.put("assets_nums",account.getAssets_nums());
        values.put("liabilities_nums",account.getLiablities_num());

        write_db.replace(SqliteTableName.AccountInfo,null,values);
        account.setAccount_id(strid);
        return strid;
    }

    /***
     * 新增一个账户信息
     * @param checkbook_id
     * @param account
     */
    public static void addOneAccount(String checkbook_id,AccountBean account){
        //1.保存Checkbook实体 到数据库
        String id=saveAccountToSqlite(account);
        account.setAccount_id(id);

        //2.建立checkbook和account的对应管理
        ContentValues values_r = new ContentValues();
        values_r.put("account_id",id);
        values_r.put("checkbook_id",checkbook_id);
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
        if(account.getParent_id()==null || account.getParent_id().isEmpty()) return null;
        for(AccountBean bean:accountList){
            if(bean.getAccount_id().equals(account.getParent_id())){
                return bean;
            }
        }
        return null;
    }


    public static String concatAccountName(String account_id){
        String name="";
        AccountBean bean=  getAccountByID(account_id);
        name=bean.getName();
        AccountBean parent=  getAccountByID(bean.getParent_id());
        if(parent !=null)
            name=parent.getName()+"-"+name;
        return name;
    }

    public static AccountBean getDeleteAccountCacher() {
        return deleteAccountCacher;
    }

    public static void setDeleteAccountCacher(AccountBean deleteAccountCacher) {
        AccountTools.deleteAccountCacher = deleteAccountCacher;
    }
}
