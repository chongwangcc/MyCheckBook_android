package com.ccmm.cc.mycheckbook.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccmm.cc.mycheckbook.Enum.SqliteTableName;
import com.ccmm.cc.mycheckbook.MyApplication;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.DetailGroupBean;

import java.text.SimpleDateFormat;
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
    public static List<CheckDetailBean> getAllDetailsInMonth(int checkbook_id,String year,String month){
        List<CheckDetailBean> result = new LinkedList<>();
        //0.检查年，月

        //1.读数据
        String sql = "select * from "+ SqliteTableName.CheckDetails+" where checkbook_id="+checkbook_id+" and year='"+year+"' and month='"+month+"' order by createTime DESC";
        Cursor detailsCursor = read_db.rawQuery(sql,null);
        while (detailsCursor.moveToNext()){
            CheckDetailBean entity = new CheckDetailBean();
            entity.setId(detailsCursor.getInt(detailsCursor.getColumnIndex("id")));
            entity.setCheckbook_id(detailsCursor.getInt(detailsCursor.getColumnIndex("checkbook_id")));
            entity.setAccount_id(detailsCursor.getInt(detailsCursor.getColumnIndex("account_id")));
            entity.setIsCreditcard(detailsCursor.getInt(detailsCursor.getColumnIndex("isCreditcard")));
            entity.setLast_update_user_id(detailsCursor.getInt(detailsCursor.getColumnIndex("last_update_user_id")));
            entity.setBalanceType(detailsCursor.getString(detailsCursor.getColumnIndex("incomeStatement")));
            entity.setCategory(detailsCursor.getString(detailsCursor.getColumnIndex("Categoryclassification")));
            entity.setDescription(detailsCursor.getString(detailsCursor.getColumnIndex("description")));
            entity.setMoney(detailsCursor.getFloat(detailsCursor.getColumnIndex("money")));

            entity.setDate(detailsCursor.getString(detailsCursor.getColumnIndex("date_str")));
            String account="Inbox";
            try{
                String sql_2 = "select * from "+ SqliteTableName.AccountInfo+" where account_id="+entity.getAccount_id();
                Cursor temp = read_db.rawQuery(sql_2,null);
                temp.moveToFirst();
                account=temp.getString(temp.getColumnIndex("account_name"));
            }catch (Exception e){

            }
            entity.setAccount(account);

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

    /***
     * 添加一条明细记录
     * @param checkDetailsBean
     * @return
     */
    public static boolean addOneCheckDetails(CheckDetailBean checkDetailsBean){
        boolean b=true;
        CheckbookEntity checkbook = CheckbookTools.getSelectedCheckbook();

        ContentValues values = new ContentValues();
        values.put("checkbook_id",checkbook.getCheckbookID());
        values.put("account_id",(checkDetailsBean.getAccount_id()));
        values.put("date_str",checkDetailsBean.getDate());
        values.put("year",checkDetailsBean.getYear());
        values.put("month",checkDetailsBean.getMonth());
        values.put("day",checkDetailsBean.getDay());
        values.put("money",checkDetailsBean.getMoney());
        values.put("description",checkDetailsBean.getDescription());
        values.put("incomeStatement",checkDetailsBean.getBalanceType());
        values.put("Categoryclassification",checkDetailsBean.getCategory());
        values.put("isCreditcard",checkDetailsBean.getIsCreditcard());
        // 添加更新日期
        values.put("updateTime",new Date().getTime());
        values.put("createTime",new Date().getTime());
        values.put("last_update_user_id",checkDetailsBean.getLast_update_user_id());
        write_db.insert(SqliteTableName.CheckDetails,null,values);
        Cursor cursor = write_db.rawQuery("select last_insert_rowid() from "+SqliteTableName.CheckDetails,null);
        int strid=-1;
        if(cursor.moveToFirst())
            strid = cursor.getInt(0);
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
        values.put("money",checkDetailsBean.getMoney());
        values.put("description",checkDetailsBean.getDescription());
        values.put("incomeStatement",checkDetailsBean.getBalanceType());
        values.put("Categoryclassification",checkDetailsBean.getCategory());
        values.put("isCreditcard",checkDetailsBean.getIsCreditcard());
        // 添加更新日期
        values.put("updateTime",new Date().getTime());
        values.put("last_update_user_id",checkDetailsBean.getLast_update_user_id());
        write_db.update(SqliteTableName.CheckDetails,values,"id="+checkDetailsBean.getId(),null);
        return b;
    }


    /**
     * 删除一条明细
     * @param checkDetailsBean
     * @return
     */
    public static boolean deleteCheckDetail(CheckDetailBean checkDetailsBean){
        if(checkDetailsBean==null) return false;
        String sql = "delete from "+SqliteTableName.CheckDetails+" where id="+checkDetailsBean.getId();
        write_db.execSQL(sql);
        return true;
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
