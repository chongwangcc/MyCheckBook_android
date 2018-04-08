package com.ccmm.cc.mycheckbook.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ccmm.cc.mycheckbook.MyApplication;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

/**
 * Created by cc on 2018/4/6.
 */

public class ImageCacheDBTools {
    static private ImageCacherDBHelper  helper=new ImageCacherDBHelper(MyApplication.getContext());;
    static private final String[] ORDER_COLUMNS = new String[] {"id", "image_cache"};

    static public boolean saveImage(String key,Bitmap bitmap){
        SQLiteDatabase write_db=helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", key);
        values.put("image_cache",convertBitmap(bitmap));
        write_db.replace(ImageCacherDBHelper.TABLE_NAME,null,values);

        return true;
    }

    static public Bitmap getImage(String key){
        SQLiteDatabase read_db=helper.getReadableDatabase();
        String selection =  "id = "+key;
        //String[] selectionArgs = { key};
        Cursor cur = read_db.query(ImageCacherDBHelper.TABLE_NAME, ORDER_COLUMNS, selection, null, null, null, null);
        cur.moveToFirst();
        byte[] in=cur.getBlob(cur.getColumnIndex("image_cache"));
        Bitmap bmpout= BitmapFactory.decodeByteArray(in,0,in.length);
        return bmpout;
    }

    static public byte[] convertBitmap(Bitmap bitmap){
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }
}
