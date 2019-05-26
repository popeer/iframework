package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

public class SalesOrderExecuteReportAbst {
    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    private String pageUrl;

    public static final String prefixURL = "/index.html#/salesOrderExecuteReport";

    public static final String postfixURL = "?activePrompt=false&closePrompt=false&closeable=true&leavePrompt=false&lock=false&pageId=salesOrderExecuteReport&pageParams=%7B%7D&path=salesOrderExecuteReport&tabId=salesOrderExecuteReport&tabLabel=销售订单执行表";
}
