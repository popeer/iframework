package com.chanjet.chanapp.qa.iFramework.common.DTO;

import java.io.Serializable;

/**
 * Created by haijia on 8/7/18.
 */
public class FlagsDto implements Serializable {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    String note;
}
