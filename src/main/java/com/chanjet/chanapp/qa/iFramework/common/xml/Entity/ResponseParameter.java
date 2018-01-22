package com.chanjet.chanapp.qa.iFramework.common.xml.Entity;

/**
 * Created by haijia on 11/23/16.
 */
public class ResponseParameter {
    private String name;
    private String value;
    private String action;
    private String path;
    private String excelFlag;
    private String excelExpectParameterColName;
    private String sequence_type_keyword;

    public String getSequence_type_keyword() {
        return sequence_type_keyword;
    }

    public void setSequence_type_keyword(String sequence_type_keyword) {
        this.sequence_type_keyword = sequence_type_keyword;
    }

    public String getExcelExpectParameterColName() {
        return excelExpectParameterColName;
    }

    public void setExcelExpectParameterColName(String excelExpectParameterColName) {
        this.excelExpectParameterColName = excelExpectParameterColName;
    }

    public String getExcelFlag() {
        return excelFlag;
    }

    public void setExcelFlag(String excelFlag) {
        this.excelFlag = excelFlag;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    @Override
    public String toString() {
        return "ResponseParameter{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", action=" + action +
                ", path=" + path +
                ", excelFlag=" + excelFlag +
                '}';
    }
}
