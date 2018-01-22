package com.chanjet.chanapp.qa.iFramework.common.xml.Entity;

/**
 * Created by haijia on 4/27/17.
 */
public class RequestDocument {
    private String path;
    private String host;
    private String flag;
    private String params;
    private String get_or_post;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getGet_or_post() {
        return get_or_post;
    }

    public void setGet_or_post(String get_or_post) {
        this.get_or_post = get_or_post;
    }


    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }



}
