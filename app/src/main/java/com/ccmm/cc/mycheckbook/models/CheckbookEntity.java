package com.ccmm.cc.mycheckbook.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**记账本描述实体
 * Created by cc on 2018/4/6.
 */

public class CheckbookEntity implements Serializable {
    private int checkbookID;
    private String title;
    private String description;
    private byte[] pic;  //记账本对应的头像
    private int isLocal;
    private UserEntity owner;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCheckbookID() {
        return checkbookID;
    }

    public void setCheckbookID(int checkbookID) {
        this.checkbookID = checkbookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public int isLocal() {
        return isLocal;
    }

    public void setLocal(int local) {
        isLocal = local;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }
}
