package com.chanjet.chanapp.qa.iFramework.common.Util;

/**
 * Created by haijia on 7/17/17.
 */
public class MyCustomerException extends Exception {
    public MyCustomerException(String message){
        setCustomInfo(message);
    }

    public MyCustomerException(Integer errorCode, String message){
        setErrorCode(errorCode);
        setCustomInfo(message);
    }

    public String getCustomInfo() {
        return customerInfo;
    }

    public void setCustomInfo(String customInfo) {
        this.customerInfo = customInfo;
    }

    String customerInfo;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    Integer errorCode;
}
