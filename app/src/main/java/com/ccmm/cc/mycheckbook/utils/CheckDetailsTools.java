package com.ccmm.cc.mycheckbook.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccmm.cc.mycheckbook.Enum.BalanceName;
import com.ccmm.cc.mycheckbook.Enum.SqliteTableName;
import com.ccmm.cc.mycheckbook.MyApplication;
import com.ccmm.cc.mycheckbook.models.AccountBean;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 记账本明细的工具类
 * Created by cc on 2018/4/6.
 */

public class CheckDetailsTools {
    static private DBHelper helper=new DBHelper(MyApplication.getContext());
    static private SQLiteDatabase write_db=helper.getWritableDatabase();   //写数据库
    static private SQLiteDatabase read_db=helper.getReadableDatabase();   //读数据库

    private static String details_year="";
    private static String detals_month="";
    private static CheckDetailBean deleteDetails_cacher=null;

    private static List<CheckDetailBean> allDetails=null;
    private static List<DetailGroupBean> detailGroup_byDay=null;

    static{
        Date dt = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        details_year=sdf1.format(dt);
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        detals_month=sdf2.format(dt);
    }

    /***
     * 根据 类别选择它的上一次使用的账户
     * @param category
     * @return
     */
    public static AccountBean selectLastAcoountByCategory(String balanceType, String category){
        try{
            // 1.获得最近的account id
            String sql = "select account_id from "+SqliteTableName.CheckDetails+" where balanceType='"+balanceType+"' and Category='"+category+"' order by updateTime desc  limit 1";
            Cursor detailsCursor = read_db.rawQuery(sql,null);
            while (detailsCursor.moveToNext()) {
                String account_id = detailsCursor.getString(detailsCursor.getColumnIndex("account_id"));
                // 获得account_
                AccountBean account_bean = AccountTools.getAccountByID(account_id);
                return account_bean;
            }
        }catch(Exception e){

        }
        return null;
    }

    /***
     * 更新记账明细月份
     * @param year
     * @param month
     * @return
     */
    public static boolean updateYear(String year,String month){
        details_year=year;
        detals_month=month;

        boolean b=true;
        return b;
    }

    /***
     * 获得这个月的所有明细记录
     * @return
     */
    public static List<CheckDetailBean> getAllDetailsInMonth(String checkbook_id,String year,String month){
        List<CheckDetailBean> result = new LinkedList<>();
        //0.检查年，月

        //1.读数据
        String sql = "select * from "+ SqliteTableName.CheckDetails+" where checkbook_id='"+checkbook_id+"' and year='"+year+"' and month='"+month+"' order by createTime DESC";
        Cursor detailsCursor = read_db.rawQuery(sql,null);
        while (detailsCursor.moveToNext()){
            CheckDetailBean entity = new CheckDetailBean();
            entity.setId(detailsCursor.getString(detailsCursor.getColumnIndex("id")));
            entity.setCheckbook_id(detailsCursor.getString(detailsCursor.getColumnIndex("checkbook_id")));
            entity.setAccount_id(detailsCursor.getString(detailsCursor.getColumnIndex("account_id")));
            entity.setIsCreditcard(detailsCursor.getInt(detailsCursor.getColumnIndex("isCreditcard")));
            entity.setLast_update_user_id(detailsCursor.getString(detailsCursor.getColumnIndex("last_update_user_id")));
            entity.setBalanceType(detailsCursor.getString(detailsCursor.getColumnIndex("balanceType")));
            entity.setCategory(detailsCursor.getString(detailsCursor.getColumnIndex("Category")));
            entity.setDescription(detailsCursor.getString(detailsCursor.getColumnIndex("description")));
            float money = Float.parseFloat(String.format("%.2f",detailsCursor.getFloat(detailsCursor.getColumnIndex("money"))));
            entity.setMoney(money);
            entity.setDate(detailsCursor.getString(detailsCursor.getColumnIndex("date_str")));
            result.add(entity);
        }
        return result;
    }

    /**
     * 获得明细的分组结果，按天获得组
     * @return
     */
    public static List<DetailGroupBean> detailsGroupByDay(List<CheckDetailBean> detailsList){
        List<DetailGroupBean> list = new LinkedList<DetailGroupBean>();
        Map<String,DetailGroupBean> tempMap=new HashMap<>();
        for(CheckDetailBean detail : detailsList){
            //1.获得group对象
            String key = detail.getDay();
            DetailGroupBean detailGroup =tempMap.get(key);
            if(detailGroup==null){
                detailGroup=new DetailGroupBean();
                tempMap.put(key,detailGroup);
            }
            //2.
            detailGroup.addOneDetailBean(detail);
        }

        //3.按照日期排序tempMap
        for(int i=31;i>=0;i--){
            String day_i=i+"";
            if(day_i.length()==1) day_i="0"+day_i;
            DetailGroupBean detailGroup =tempMap.get(day_i);
            if(detailGroup!=null){
                list.add(detailGroup);
            }
        }
        return list;

    }

    /**
     * 获得明细的分组结果，类别分组
     * @return
     */
    public static List<DetailGroupBean> detailsGroupByCategory(List<CheckDetailBean> detailsList,String balanceType){
        List<DetailGroupBean> list = new LinkedList<DetailGroupBean>();
        Map<String,DetailGroupBean> tempMap=new HashMap<>();
        //1.获得group对象
        for(CheckDetailBean detail : detailsList){
            if(detail.getBalanceType().equals(balanceType)
                    || (balanceType.equals(BalanceName.Inflow) && detail.getBalanceType().equals(BalanceName.Income) &&detail.getIsCreditcard()!=0)
                    || (balanceType.equals(BalanceName.Outflow) && detail.getBalanceType().equals(BalanceName.Expend )&&detail.getIsCreditcard()==0) ){
                String key = detail.getCategory();
                DetailGroupBean detailGroup =tempMap.get(key);
                if(detailGroup==null){
                    detailGroup=new DetailGroupBean();
                    tempMap.put(key,detailGroup);
                }
                //2.
                detailGroup.addOneDetailBean(detail);
            }
        }

        //3.按照金额大小tempMap
        for(String key:tempMap.keySet()){
            DetailGroupBean detailGroup =tempMap.get(key);
            if(detailGroup!=null){
                list.add(detailGroup);
            }
        }
        Collections.sort(list, new Comparator<DetailGroupBean>() {
            @Override
            public int compare(DetailGroupBean t1, DetailGroupBean t2) {
                float n1=0,n2=0;
                switch (t1.getBalanceType()){
                    case BalanceName.Expend:
                        n1=t1.getTotal_spent();
                        n2=t2.getTotal_spent();
                        break;
                    case BalanceName.Income:
                        n1=t1.getTotal_income();
                        n2=t2.getTotal_income();
                        break;
                    //TODO 处理流入、流出表
                    case BalanceName.Inflow:
                        n1=t1.getCashflow_in();
                        n2=t2.getCashflow_in();
                        break;
                    case BalanceName.Outflow:
                        n1=t1.getCashflow_out();
                        n2=t2.getCashflow_out();
                        break;
                }
                if(n2>n1){
                    return 1;
                }else if(n2<n1){
                    return -1;
                }
                return 0;
            }
        });

        return list;

    }

    /***
     * 通过账户来对数据分组
     * @param detailsList
     * @return
     */
    public static List<DetailGroupBean> detailsGroupByAccount(List<CheckDetailBean> detailsList){
        List<DetailGroupBean> list = new LinkedList<DetailGroupBean>();
        Map<String,DetailGroupBean> tempMap=new HashMap<>();
        for(CheckDetailBean detail : detailsList){
            //1.获得group对象
            String key = AccountTools.concatAccountName(detail.getAccount_id());
            DetailGroupBean detailGroup =tempMap.get(key);
            if(detailGroup==null){
                detailGroup=new DetailGroupBean();
                tempMap.put(key,detailGroup);
            }
            detailGroup.addOneDetailBean(detail);
        }

        // 4.添加其他账户，当月没有消费的账户
        List<AccountBean> accountList_all = AccountTools.getAccountList(CheckbookTools.getSelectedCheckbook().getCheckbookID());
        for(AccountBean account_bean : accountList_all ){
            String account_name_whole = AccountTools.concatAccountName(account_bean.getAccount_id());
            DetailGroupBean detailGroup =tempMap.get(account_name_whole);
            if(detailGroup==null){
                detailGroup=new DetailGroupBean();
                detailGroup.setAccountName(account_name_whole);
                detailGroup.setAccount_id(account_bean.getAccount_id());
                detailGroup.setAssets_diff(0);
                detailGroup.setLiabilities_diff(0);
                tempMap.put(account_name_whole,detailGroup);
            }
        }

        //3.按照日期排序tempMap
        for(String key:tempMap.keySet()){
            DetailGroupBean detailGroup =tempMap.get(key);
            list.add(detailGroup);
        }
        return list;
    }


    /***
     * 添加一条明细记录
     * @param checkDetailsBean
     * @return
     */
    public static boolean addOneCheckDetails(CheckDetailBean checkDetailsBean){
        boolean b=true;
        CheckbookEntity checkbook = CheckbookTools.getSelectedCheckbook();

        ContentValues values = new ContentValues();
        String strid="";
        if(checkDetailsBean.getId()==null || checkDetailsBean.getId().isEmpty()){
            strid=ZaTools.genNewUUID();
        }else{
            strid=checkbook.getCheckbookID();

        }
        values.put("id",strid);
        values.put("checkbook_id",checkbook.getCheckbookID());
        values.put("account_id",(checkDetailsBean.getAccount_id()));
        values.put("date_str",checkDetailsBean.getDate());
        values.put("year",checkDetailsBean.getYear());
        values.put("month",checkDetailsBean.getMonth());
        values.put("day",checkDetailsBean.getDay());
        float money = Float.parseFloat(String.format("%.2f",checkDetailsBean.getMoney()));
        values.put("money",money);
        values.put("description",checkDetailsBean.getDescription());
        values.put("balanceType",checkDetailsBean.getBalanceType());
        values.put("Category",checkDetailsBean.getCategory());
        values.put("isCreditcard",checkDetailsBean.getIsCreditcard());
        // 添加更新日期
        values.put("updateTime",new Date().getTime());
        values.put("createTime",new Date().getTime());
        values.put("last_update_user_id",checkDetailsBean.getLast_update_user_id());
        write_db.insert(SqliteTableName.CheckDetails,null,values);
        checkDetailsBean.setId(strid);
        return b;
    }

    /***
     * 修改一条明细记录
     * @param checkDetailsBean
     * @return
     */
    public static boolean modifyOneCheckDetails(CheckDetailBean checkDetailsBean){
        boolean b=true;
        CheckbookEntity checkbook = CheckbookTools.getSelectedCheckbook();

        ContentValues values = new ContentValues();
        values.put("checkbook_id",checkbook.getCheckbookID());
        values.put("account_id",(checkDetailsBean.getAccount_id()));
        values.put("date_str",checkDetailsBean.getDate());
        values.put("year",checkDetailsBean.getYear());
        values.put("month",checkDetailsBean.getMonth());
        values.put("day",checkDetailsBean.getDay());
        float money = Float.parseFloat(String.format("%.2f",checkDetailsBean.getMoney()));
        values.put("money",money);
        values.put("description",checkDetailsBean.getDescription());
        values.put("balanceType",checkDetailsBean.getBalanceType());
        values.put("Category",checkDetailsBean.getCategory());
        values.put("isCreditcard",checkDetailsBean.getIsCreditcard());
        // 添加更新日期
        values.put("updateTime",new Date().getTime());
        values.put("last_update_user_id",checkDetailsBean.getLast_update_user_id());
        write_db.update(SqliteTableName.CheckDetails,values,"id='"+checkDetailsBean.getId()+"'",null);
        return b;
    }


    /**
     * 删除一条明细
     * @param checkDetailsBean
     * @return
     */
    public static boolean deleteCheckDetail(CheckDetailBean checkDetailsBean){
        if(checkDetailsBean==null) return false;
        String sql = "delete from "+SqliteTableName.CheckDetails+" where id='"+checkDetailsBean.getId()+"'";
        write_db.execSQL(sql);
        return true;
    }

    /***
     * 读数据库,获得最新值
     * @param checkDetailsBean
     * @return
     */
    public static CheckDetailBean queryCheckDetail(String checkbook_id,CheckDetailBean checkDetailsBean){
        CheckDetailBean entity =null;
        if(checkDetailsBean==null) return entity;
        String sql = "select * from "+ SqliteTableName.CheckDetails+" where id='"+checkDetailsBean.getId()+"' order by createTime DESC";
        Cursor detailsCursor = read_db.rawQuery(sql,null);
        while (detailsCursor.moveToNext()) {
            entity = new CheckDetailBean();
            entity.setId(detailsCursor.getString(detailsCursor.getColumnIndex("id")));
            entity.setCheckbook_id(detailsCursor.getString(detailsCursor.getColumnIndex("checkbook_id")));
            entity.setAccount_id(detailsCursor.getString(detailsCursor.getColumnIndex("account_id")));
            entity.setIsCreditcard(detailsCursor.getInt(detailsCursor.getColumnIndex("isCreditcard")));
            entity.setLast_update_user_id(detailsCursor.getString(detailsCursor.getColumnIndex("last_update_user_id")));
            entity.setBalanceType(detailsCursor.getString(detailsCursor.getColumnIndex("balanceType")));
            entity.setCategory(detailsCursor.getString(detailsCursor.getColumnIndex("Category")));
            entity.setDescription(detailsCursor.getString(detailsCursor.getColumnIndex("description")));
            float money = Float.parseFloat(String.format("%.2f",detailsCursor.getFloat(detailsCursor.getColumnIndex("money"))));
            entity.setMoney(money);

            entity.setDate(detailsCursor.getString(detailsCursor.getColumnIndex("date_str")));
        }
        return entity;
    }

    /////////////////////////////////SET GET方法///////////////////////////////

    public static String getDetails_year() {
        return details_year;
    }

    public static void setDetails_year(String details_year) {
        CheckDetailsTools.details_year = details_year;
    }

    public static String getDetals_month() {
        return detals_month;
    }

    public static void setDetals_month(String detals_month) {
        CheckDetailsTools.detals_month = detals_month;
    }

    public static CheckDetailBean getDeleteDetails_cacher() {
        return deleteDetails_cacher;
    }

    public static void setDeleteDetails_cacher(CheckDetailBean deleteDetails_cacher) {
        CheckDetailsTools.deleteDetails_cacher = deleteDetails_cacher;
    }

    public static List<CheckDetailBean> getAllDetails() {
        return allDetails;
    }

    public static void setAllDetails(List<CheckDetailBean> allDetails) {
        CheckDetailsTools.allDetails = allDetails;
    }

    public static List<DetailGroupBean> getDetailGroup_byDay() {
        if(detailGroup_byDay==null)
            return new LinkedList<>();
        return detailGroup_byDay;
    }

    public static void setDetailGroup_byDay(List<DetailGroupBean> detailGroup_byDay) {
        CheckDetailsTools.detailGroup_byDay = detailGroup_byDay;
    }
}
