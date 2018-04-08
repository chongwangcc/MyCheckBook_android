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
    private Bitmap pic;  //记账本对应的头像
    private boolean isLocal;
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

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }
}