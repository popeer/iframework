package com.chanjet.chanapp.qa.iFramework.Entity;

import java.util.HashMap;

/**
 * Created by haijia on 11/21/16.
 */
public class CommandInfo {

    public HashMap<String, String> context = new HashMap<String, String>();// 信令请求值
    public HashMap<String, String> except = new HashMap<String, String>();// 期望结果
    public HashMap<String, String> realResult = new HashMap<String, String>();// 实际结果
    public HashMap<String, String> noExpect = new HashMap<String, String>();// 不应存在的预期结果
    public HashMap<String, String> noResponse = new HashMap<String, String>();// 不应收到应答
    public HashMap<String, String> relaPara = new HashMap<String, String>();// 依赖参数


    public StringBuffer commandLogInfo = new StringBuffer();
    public StringBuffer commandErrorInfo = new StringBuffer();
    private boolean needContext = false;// 是否需要赋值变量
    private boolean isPassed ;// 与期望结果匹配
    private boolean isExecuted = false;// 是否执行过
    private boolean isStop = false;// 是否停止执行
    public boolean isSMSCommand = false;//是否执行通过
    private String commandName;// 信令名字

    private String client;// 模拟客户端类型   (此类型实际上为服务类型)
    private String clientType;// 模拟客户端类型   (此类型实际登录的客户端)
    private String commandType;// 信令执行的方法（请求类型）
    private long mobile;// 执行信令的用户（手机号）
    private int userId;// 执行信令的用户（userid）

    private int sid;// 执行信令的用户（userid）
    private String nickName;// 信令的名称
    private int intervalTime=1;// 发送信令间隔时间
    private int runTimes=1;// 执行次数
    private String url;// 请求地址

    private long waitTime=0l;// 等待时间

    private int runCount=0;// 执行次数

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }



    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }


    public boolean isNeedContext() {
        return needContext;
    }

    public void setNeedContext(boolean needContext) {
        this.needContext = needContext;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setExecuted(boolean isExecuted) {
        this.isExecuted = isExecuted;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isSMSCommand() {
        return isSMSCommand;
    }

    public void setSMSCommand(boolean isSMSCommand) {
        this.isSMSCommand = isSMSCommand;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(int runTimes) {
        this.runTimes = runTimes;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }
}
