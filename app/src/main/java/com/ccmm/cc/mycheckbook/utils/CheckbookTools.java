package com.ccmm.cc.mycheckbook.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ccmm.cc.mycheckbook.models.CheckbookEntity;
import com.ccmm.cc.mycheckbook.models.UserEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 对记账本的增删改查的操作
 * Created by cc on 2018/4/6.
 */

public class CheckbookTools {
    private static int id=0;
    private static ImageCache imageCache = new ImageCache();
    private static CheckbookEntity selectedCheckbook=null;
    //记账本的Map,保存用户名与其所有的记账本
    private static Map<UserEntity,List<CheckbookEntity>> checkbookMap=new HashMap<>();

    /***
     * 获得用户加入的所有记账本清单
     * @param user
     * @return
     */
    static public List<CheckbookEntity> fetchAllCheckbook(UserEntity user){
        List<CheckbookEntity> result=checkbookMap.get(user);
        if(result==null)
            result = new LinkedList<>();
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

        List<CheckbookEntity> checkbooklist = checkbookMap.get(user);
        if(checkbooklist==null){
            checkbooklist = new LinkedList<>();
            checkbookMap.put(user,checkbooklist);
        }
        checkbooklist.add(checkbook);
        if(checkbook.getPic()!=null){
            ImageCacheDBTools.saveImage(checkbook.getCheckbookID()+"", checkbook.getPic());
        }
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
            checkbook = new CheckbookEntity();
            checkbook.setCheckbookID(1);
            checkbook.setDescription("测试用");
            checkbook.setLocal(false);
            checkbook.setOwner(user);
            checkbook.setTitle("邀请码测试记账本");
        }
        return checkbook;
    }

    /***
     * 新建一个记账本实体
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

        checkbook.setCheckbookID(id++);
        checkbook.setTitle(title);
        checkbook.setDescription(description);
        checkbook.setLocal(islocal);
        checkbook.setPic(pic);
        checkbook.setOwner(user);
        return checkbook;
    }
    static public CheckbookEntity getChecckbookByIndex(UserEntity user,int index){
        CheckbookEntity entity = null;
        try{
            List<CheckbookEntity> checkbooklist = checkbookMap.get(user);
            entity=checkbooklist.get(index);
        }catch ( Exception e){

        }
        return entity;

    }


    static public Bitmap getBitmapFromCacher(CheckbookEntity checkbook){
        Bitmap bitmap =ImageCacheDBTools.getImage(checkbook.getCheckbookID()+"");
        return bitmap;
    }

    public static CheckbookEntity getSelectedCheckbook() {
        return selectedCheckbook;
    }

    public static void setSelectedCheckbook(CheckbookEntity selectedCheckbook) {
        CheckbookTools.selectedCheckbook = selectedCheckbook;
    }


}
