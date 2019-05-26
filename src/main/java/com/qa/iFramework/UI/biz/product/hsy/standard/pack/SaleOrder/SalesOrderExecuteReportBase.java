package com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder;

import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.SalesOrderExecuteReportAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.BasePack;

public class SalesOrderExecuteReportBase extends BasePack {
    protected SalesOrderExecuteReportAbst salesOrderExecuteReportAbst = new SalesOrderExecuteReportAbst();
    public SalesOrderExecuteReportBase(){

    }

    public SalesOrderExecuteReportBase(String prefix){
        String url = prefix +
                SalesOrderExecuteReportAbst.prefixURL +
                SalesOrderExecuteReportAbst.postfixURL;
        salesOrderExecuteReportAbst.setPageUrl(url);
    }

    public String checkData(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{

        } catch (Exception ex){

        }
        return resultSummary.toString();
    }
}
