package com.chanjet.chanapp.qa.iFramework.common.xml.Entity;

/**
 * Created by haijia on 11/23/16.
 */
public class RequestParameter {

    private String name;
    private String value;
    private String action;
    private String sequence;
    private String colName;
    private String keyword;
    private String type;
    private String sqlScript;
    private String timeFormat;
    private String exchangeFlag;
    private String storeParam;
    private String sequence_type_keyword_key;
    private String offlineFlag;
    private String param_type_key;
    private String alias;
    private String sqlSelectField;
    private String complexKeyword;
    private String sequence_type_resultPath_valuePath;
    private String param_keyword_keyPath;
    private String jarparam_keyvalue;
    private String customerClass;
    private String bodyData;
    private String regex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData;
    }

    public String getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(String customerClass) {
        this.customerClass = customerClass;
    }

    public String getJarparam_keyvalue() {
        return jarparam_keyvalue;
    }

    public void setJarparam_keyvalue(String jarparam_keyvalue) {
        this.jarparam_keyvalue = jarparam_keyvalue;
    }

    public String getParam_keyword_keyPath() {
        return param_keyword_keyPath;
    }

    public void setParam_keyword_keyPath(String param_keyword_keyPath) {
        this.param_keyword_keyPath = param_keyword_keyPath;
    }

    public String getSequence_type_resultPath_valuePath() {
        return sequence_type_resultPath_valuePath;
    }

    public void setSequence_type_resultPath_valuePath(String sequence_type_resultPath_valuePath) {
        this.sequence_type_resultPath_valuePath = sequence_type_resultPath_valuePath;
    }

    public String getComplexKeyword() {
        return complexKeyword;
    }

    public void setComplexKeyword(String complexKeyword) {
        this.complexKeyword = complexKeyword;
    }

    public String getSqlSelectField() {
        return sqlSelectField;
    }

    public void setSqlSelectField(String sqlSelectField) {
        this.sqlSelectField = sqlSelectField;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getParam_type_key() {
        return param_type_key;
    }

    public void setParam_type_key(String param_type_key) {
        this.param_type_key = param_type_key;
    }

    public String getSequence_type_keyword_key() {
        return sequence_type_keyword_key;
    }

    public void setSequence_type_keyword_key(String sequence_type_keyword_key) {
        this.sequence_type_keyword_key = sequence_type_keyword_key;
    }

    public String getStoreParam() {
        return storeParam;
    }

    public void setStoreParam(String storeParam) {
        this.storeParam = storeParam;
    }

    public String getOfflineFlag() {
        return offlineFlag;
    }

    public void setOfflineFlag(String offlineFlag) {
        this.offlineFlag = offlineFlag;
    }

    public String getExchangeFlag() {
        return exchangeFlag;
    }

    public void setExchangeFlag(String exchangeFlag) {
        this.exchangeFlag = exchangeFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSequence(){
        return sequence;
    }

    public void setSequence(String sequence){
        this.sequence = sequence;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSqlScript() {
        return sqlScript;
    }

    public void setSqlScript(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    @Override
    public String toString() {
        return "RequestParameter{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
