package com.qa.iFramework.common.DTO;

import java.io.Serializable;

/**
 * Created by bailin on 2016/11/30.
 */
public class ModuleDto implements Serializable {
    long id;
    String name;
    String memo;

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
}
