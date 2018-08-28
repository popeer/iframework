package com.chanjet.chanapp.qa.iFramework.common.DTO;

import java.io.Serializable;

/**
 * Created by haijia on 8/8/18.
 */
public class NodeManagerDto implements Serializable {
    Long id;
    String flags;
    String path;
    String name;
    String note;
    String pn;

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
