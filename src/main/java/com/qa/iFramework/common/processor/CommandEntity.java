package com.qa.iFramework.common.processor;

import org.openqa.selenium.WebDriver;

import java.util.LinkedList;

/**
 * Created by haijia on 6/21/17.
 */
public class CommandEntity {
    //前端传来的前置结果，会存入到preResults对象里。
    private String preResults;

    public String getPreResults() {
        return preResults;
    }

    public void setPreResults(String preResults) {
        this.preResults = preResults;
    }

    //测试数据源
    private String dataSource;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    //UI自动化WebDriver
    private WebDriver webDriver;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String user;

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
