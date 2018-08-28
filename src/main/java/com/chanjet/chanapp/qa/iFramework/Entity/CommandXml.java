package com.chanjet.chanapp.qa.iFramework.Entity;

import com.chanjet.chanapp.qa.iFramework.Entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public class CommandXml {
    public List<CommandInfo> allCommand = new ArrayList<CommandInfo>();// 信令信息

    public List<InitMethod> initList = new ArrayList<InitMethod>();//初始化方法列表

    public List<InitMethod> recoverList = new ArrayList<InitMethod>();//环境恢复方法列表

    public List<com.chanjet.chanapp.qa.iFramework.Entity.Userinfo> userList = new ArrayList<com.chanjet.chanapp.qa.iFramework.Entity.Userinfo>();//用户信息

    public static HashMap<String, com.chanjet.chanapp.qa.iFramework.Entity.Userinfo> userlistHm = new HashMap<String, com.chanjet.chanapp.qa.iFramework.Entity.Userinfo>();// 依赖参数

    public static HashMap<String, String> relaPara = new HashMap<String, String>();//依赖参数

    public static HashMap<String, String> funcRelaPara = new HashMap<String, String>();//恢复函数依赖

    public StringBuffer caseLogInfo = new StringBuffer(5*1024);

    public StringBuffer caseErrorInfo = new StringBuffer(1*1024);

    public boolean isResult = true;//是否执行通过

    private String runUserName;// 信令执行人

    private String runTimes;// 执行时间

    public boolean isSMSCommand = false;//是否执行通过


    public String getRunUserName() {
        return runUserName;
    }

    public void setRunUserName(String runUserName) {
        this.runUserName = runUserName;
    }

    public String getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(String runTimes) {
        this.runTimes = runTimes;
    }

    public List<CommandInfo> getAllCommand() {
        return allCommand;
    }

    public void setAllCommand(List<CommandInfo> allCommand) {
        this.allCommand = allCommand;
    }

    public List<InitMethod> getInitList() {
        return initList;
    }

    public void setInitList(List<InitMethod> initList) {
        this.initList = initList;
    }

    public List<InitMethod> getRecoverList() {
        return recoverList;
    }

    public void setRecoverList(List<InitMethod> recoverList) {
        this.recoverList = recoverList;
    }

    public List<com.chanjet.chanapp.qa.iFramework.Entity.Userinfo> getUserList() {
        return userList;
    }

    public void setUserList(List<com.chanjet.chanapp.qa.iFramework.Entity.Userinfo> userList) {
        this.userList = userList;
    }

    public String caseName=null ;//信令名字

    public String author =null;//信令名字

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean isResult) {
        this.isResult = isResult;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isSMSCommand() {
        return isSMSCommand;
    }

    public void setSMSCommand(boolean isSMSCommand) {
        this.isSMSCommand = isSMSCommand;
    }
}
