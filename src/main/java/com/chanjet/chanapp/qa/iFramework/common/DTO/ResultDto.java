package com.chanjet.chanapp.qa.iFramework.common.DTO;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by bailin on 2016/11/30.
 */
public class ResultDto implements Serializable {
    long id;
    String user_id;
    String module_id;
    int version_id;
    boolean is_passed;
    Object case_info;
    String node_name;
    String step_name;
    String environment;
    String rundatetime;
    String module_desc;

    public String getModule_desc() {
        return module_desc;
    }

    public void setModule_desc(String module_desc) {
        this.module_desc = module_desc;
    }

    public String getRundatetime() {
        return rundatetime;
    }

    public void setRundatetime(String rundatetime) {
        this.rundatetime = rundatetime;
    }

    public String getStep_name() {
        return step_name;
    }

    public void setStep_name(String step_name) {
        this.step_name = step_name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public boolean is_passed() {
        return is_passed;
    }

    public void setIs_passed(boolean is_passed) {
        this.is_passed = is_passed;
    }

    public Object getCase_info() {
        return case_info;
    }

    public void setCase_info(Object case_info) {
        this.case_info = case_info;
    }

    @Override
    public String toString() {
        JSONObject result = new JSONObject();
        result.put("case_NodeName", node_name);
        result.put("case_StepName", step_name);
        result.put("case_DetailInfo", case_info);
        result.put("case_ModuleName", module_id);
        result.put("case_Operator", user_id);
        result.put("case_DateTime", rundatetime);
        result.put("case_Environment", environment);
        return result.toJSONString();
//        return "{" +
//                " \"case_NodeName\":\"" + node_name + "\"" +
//                ", \"case_StepName\":\"" + step_name + "\"" +
//                ", \"case_DetailInfo\":" + case_info +
//                ", \"case_ModuleName\":\"" + module_id + "\"" +
//                ", \"case_Operator\":\"" + user_id + "\"" +
//                ", \"case_DateTime\":\"" + rundatetime + "\"" +
//                ", \"case_Environment\":\"" + environment + "\"" +
//                "}";
    }
}
