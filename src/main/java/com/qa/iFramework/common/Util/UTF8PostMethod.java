package com.qa.iFramework.common.Util;

import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Created by haijia on 2018/12/6.
 */
public class UTF8PostMethod extends PostMethod {
    public UTF8PostMethod(String url){
        super(url);
    }
    @Override
    public String getRequestCharSet() {
        return "utf-8";
    }
}
