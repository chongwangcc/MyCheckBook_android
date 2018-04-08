package com.ccmm.cc.mycheckbook.models;

import java.io.Serializable;

/**定义用户类
 * Created by cc on 2018/4/6.
 */

public class UserEntity implements Serializable {
    private String user_id;
    private String name;
    private String description;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
