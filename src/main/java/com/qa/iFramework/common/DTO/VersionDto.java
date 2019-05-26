package com.qa.iFramework.common.DTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bailin on 2016/11/30.
 */
public class VersionDto implements Serializable {
    int id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    Date date;
    String pn;

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
