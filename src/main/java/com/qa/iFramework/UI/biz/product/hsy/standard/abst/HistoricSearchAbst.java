package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

public class HistoricSearchAbst {
    private String pageUrl;

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public static final String postfixURL = "/index.html#/sales-order-price?activePrompt=false&closePrompt=false&closeable=true&leavePrompt=false&lock=false&pageId=sales-order-price&pageParams=%7B%7D&path=sales-order-price&tabId=sales-order-price&tabLabel=历史价跟踪";

    public static final String currentDay = "//*[@class='filter-convenient-date ']/li[2]";

    public static final String customer = "//*[@class='filter-fields-container']/div/div/div[2]/div/div/div[2]/div/div/div/span/div/input";

    public static final String product = "//*[@class='filter-fields-container']/div/div/div[3]/div/div/div[2]/div/div/div/span/div/input";

    public static final String searchBtn = "//*[@class='tosearch']";

    public static final String lasttimeOnly = "//*[@class='toolbar']/div/div/span/input";

    public static final String exportBtn = "//*[@class='toolbar']/button[2]";
}
