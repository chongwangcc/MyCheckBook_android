package com.ccmm.cc.mycheckbook.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.ccmm.cc.mycheckbook.Enum.SqliteTableName;
import com.ccmm.cc.mycheckbook.MyApplication;
import com.ccmm.cc.mycheckbook.models.CheckDetailBean;
import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.UserEntity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 对记账本的增删改查的操作
 * Created by cc on 2018/4/6.
 */

public class CheckbookTools {
    static private DBHelper helper=new DBHelper(MyApplication.getContext());
    static private SQLiteDatabase write_db=helper.getWritableDatabase();   //写数据库
    static private SQLiteDatabase read_db=helper.getReadableDatabase();   //读数据库


    //记账本的Map,保存用户名与其所有的记账本
    private static Map<UserEntity,List<CheckbookEntity>> checkbookMap=new HashMap<>();
    //选中的记账本
    private static CheckbookEntity selectedCheckbook=null;
    private static CheckbookEntity deleteCheckbook_cacher;

    /***
     * 获得用户加入的所有记账本清单
     * @param user
     * @return
     */
    static public List<CheckbookEntity> fetchAllCheckbook(UserEntity user){
        //1.TODO 联网，根据用户名获得他加入的所有记账本
        List<CheckbookEntity> result=checkbookMap.get(user);
        if(result==null){
            result = new LinkedList<>();
            checkbookMap.put(user,result);
        }
        result.clear();

        //2.查询SQLlite，获得所有记账本
        String sql="select * from "+SqliteTableName.UserCheckbookMap+" where user_name='"+user.getName()+"'";
        try{
            Cursor userCheckbookMapCursor = read_db.rawQuery(sql,null);
            while (userCheckbookMapCursor.moveToNext()){
                String id = userCheckbookMapCursor.getString(userCheckbookMapCursor.getColumnIndex("checkbook_id"));
                CheckbookEntity entity = getCheckbookByID(id);
                result.add(entity);
            }
            userCheckbookMapCursor.close();
        }catch (Exception e){
            e.printStackTrace();

        }

        return result;
    }

    /***
     * 添加一个记账本
     * @param user
     * @param checkbook
     * @return
     */
    static public boolean addOneCheckbook(UserEntity user,CheckbookEntity checkbook){
        if(user==null || checkbook==null) return false;

        //1.保存Checkbook实体 到数据库
        String id=saveCheckbookToSqllite(checkbook);
        checkbook.setCheckbookID(id);

        //2.建立checkbook和User的对应管理
        ContentValues values_r = new ContentValues();
        values_r.put("id",ZaTools.genNewUUID());
        values_r.put("user_name",user.getName());
        values_r.put("checkbook_id",id);
        values_r.put("permission",1);
        values_r.put("description","");
        write_db.insert(SqliteTableName.UserCheckbookMap,null,values_r);

        //3.保存到内存中对象
        List<CheckbookEntity> checkbooklist = checkbookMap.get(user);
        if(checkbooklist==null){
            checkbooklist = new LinkedList<>();
            checkbookMap.put(user,checkbooklist);
        }
        checkbooklist.add(checkbook);

        //4.TODO 远程同步  记账本数据

        return true;
    }

    /***
     * 通过邀请码获得记账本
     * @param user
     * @param invitation
     * @return
     */
    static public  CheckbookEntity checkInvitation(UserEntity user,String invitation){
        if(invitation==null) return null;
        CheckbookEntity checkbook=null;
        if(invitation.equals("test")){

            //1.获得记账本
            checkbook = new CheckbookEntity();
            checkbook.setCheckbookID("");
            checkbook.setDescription("测试用");
            checkbook.setLocal(0);
            checkbook.setOwner(user);
            checkbook.setTitle("邀请码测试记账本");

            //2.将记账本保存到本地数据库中
        }
        return checkbook;
    }

    /***
     * 新建一个记账本实体,不填写ID字段
     * @param user
     * @param title
     * @param description
     * @param islocal
     * @param pic
     * @return
     */
    static public CheckbookEntity newChechbook(UserEntity user,String title, String description, boolean islocal, Bitmap pic){
        CheckbookEntity checkbook = new CheckbookEntity();
        //1.获得id
        int isLocal_int = 0;
        if(islocal)
            isLocal_int=1;
        checkbook.setTitle(title);
        checkbook.setDescription(description);
        checkbook.setLocal(isLocal_int);
        checkbook.setPic(bitmapConvertToByte(pic));
        checkbook.setOwner(user);
        return checkbook;
    }

    /***
     * 获得列表框中的第i个记账本
     * @param user
     * @param index
     * @return
     */
    static public CheckbookEntity getCheckbookByIndex(UserEntity user,int index){
        CheckbookEntity entity = null;
        try{
            List<CheckbookEntity> checkbooklist = checkbookMap.get(user);
            entity=checkbooklist.get(index);
        }catch ( Exception e){
            e.printStackTrace();
        }
        return entity;

    }

    /***
     * 通过ID从本地数据中读取记账本数据
     * @param id
     * @return
     */
    static public CheckbookEntity getCheckbookByID(String id){
        String sql_2="select * from " +SqliteTableName.CheckbookInfo+" where id='"+id+"'";
        Cursor tempCursor = read_db.rawQuery(sql_2,null);
        while (tempCursor.moveToNext()){
            //1.获得记账本实体
            CheckbookEntity checkbookEntity = new CheckbookEntity();
            checkbookEntity.setCheckbookID(tempCursor.getString(tempCursor.getColumnIndex("id")));
            checkbookEntity.setTitle(tempCursor.getString(tempCursor.getColumnIndex("name")));
            checkbookEntity.setDescription(tempCursor.getString(tempCursor.getColumnIndex("description")));
            checkbookEntity.setLocal(tempCursor.getInt(tempCursor.getColumnIndex("islocal")));
            byte[] in=tempCursor.getBlob(tempCursor.getColumnIndex("coverImage"));
            checkbookEntity.setPic(in);
            //加入到返回的列表中
            tempCursor.close();
            return checkbookEntity;
        }
        tempCursor.close();
        return null;
    }

    /***
     * 删除sqlite中一个记账本
     * @return
     */
    static public boolean deleteCheckbookByID(UserEntity user,CheckbookEntity checkbook){
        //1.自己是笔记本的创造者
        //TODO 删除本地记账本，
        //TODO 删除云端记账本
        //TODO 通知其他用户删除他们本地的副本
        //2.如果自己只是加入一个记账本中
        //TODO 删除checkbook和用户的对应关系
        String sql = "delete from "+SqliteTableName.UserCheckbookMap+" where checkbook_id='"+checkbook.getCheckbookID()+"' and user_name='"+user.getName()+"'";
        write_db.execSQL(sql);
        return true;
    }

    /***
     * 保存记账本到数据库中，返回数据库中的ID
     * @param checkbook
     * @return
     */
    static public String saveCheckbookToSqllite(CheckbookEntity checkbook){
        ContentValues values = new ContentValues();
        String strid="";
        if(checkbook.getCheckbookID()==null || checkbook.getCheckbookID().isEmpty()){
            strid=ZaTools.genNewUUID();
        }else{
            strid=checkbook.getCheckbookID();
        }

        values.put("id",strid);
        values.put("coverImage",checkbook.getPic());
        values.put("islocal",(checkbook.isLocal()));
        values.put("description",checkbook.getDescription());
        values.put("name",checkbook.getTitle());
        write_db.insert(SqliteTableName.CheckbookInfo,null,values);
        checkbook.setCheckbookID(strid);
        return strid;
    }

    static public Bitmap getBitmapFromCacher(CheckbookEntity checkbook){
        byte[] bb =getCheckbookByID(checkbook.getCheckbookID()).getPic();
        return byteConvertToBitmap(bb);
    }

    public static CheckbookEntity getSelectedCheckbook() {
        return selectedCheckbook;
    }

    public static void setSelectedCheckbook(CheckbookEntity selectedCheckbook) {
        CheckbookTools.selectedCheckbook = selectedCheckbook;
    }

    public static byte[] bitmapConvertToByte(Bitmap bitmap){
        if(bitmap==null) return null;
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static Bitmap byteConvertToBitmap( byte[] in){
        if(in==null) return null;
        Bitmap bmpout= BitmapFactory.decodeByteArray(in,0,in.length);
        return bmpout;
    }

    public static void setDeleteCheckbook_cacher(CheckbookEntity deleteCheckbook_cacher) {
        CheckbookTools.deleteCheckbook_cacher = deleteCheckbook_cacher;
    }

    public static CheckbookEntity getDeleteCheckbook_cacher() {
        return deleteCheckbook_cacher;
    }
}
