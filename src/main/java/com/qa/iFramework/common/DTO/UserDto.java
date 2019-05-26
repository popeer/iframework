package com.qa.iFramework.common.DTO;

import java.io.Serializable;

/**
 * create by bailin 2016-11-30
 * 用户表结构
 */
public class UserDto implements Serializable {
    long id;
    String name;
    String memo;
    String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
