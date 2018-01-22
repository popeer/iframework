package com.chanjet.chanapp.qa.iFramework.common.processor;

import java.util.LinkedList;

/**
 * Created by haijia on 6/21/17.
 */
public class CommandEntity {
    public String getSqlDriver() {
        return sqlDriver;
    }

    public void setSqlDriver(String sqlDriver) {
        this.sqlDriver = sqlDriver;
    }

    private String sqlDriver;

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    private String dns;

    public Integer getVersionID() {
        return versionID;
    }

    public void setVersionID(Integer versionID) {
        this.versionID = versionID;
    }

    private Integer versionID;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private String productName;


    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    private String dbURL;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private String pwd;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    private String domainName;

    public String getSqlKeyName() {
        return sqlKeyName;
    }

    public void setSqlKeyName(String sqlKeyName) {
        this.sqlKeyName = sqlKeyName;
    }

    private String sqlKeyName;
//
//    public String getXmlPath() {
//        return xmlPath;
//    }
//
//    public void setXmlPath(String xmlPath) {
//        this.xmlPath = xmlPath;
//    }
//
//    private String xmlPath;

    public LinkedList<String> getSqlList() {
        return sqlList;
    }

    public void setSqlList(LinkedList<String> sqlList) {
        this.sqlList = sqlList;
    }

    private LinkedList<String> sqlList;

}
