package com.qa.iFramework.common.xml.Entity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by haijia on 4/27/17.
 */
public class ResponseDocument {

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    private JSONObject result;

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }



}
