package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

public class GoodsIssueReportAbst {
    //销货单统计表url
    private String pageUrl;

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    //销货单统计表后缀url
    public static final String postfixURL = "goodsIssueTotalReport?activePrompt=false&closePrompt=false&closeable=true&leavePrompt=false&lock=false&pageId=goodsIssueTotalReport&pageParams=%7B%7D&path=goodsIssueTotalReport&tabId=goodsIssueTotalReport&tabLabel=销货单统计表";

    public static final String firstTab = "//*[@class='schemes']/div[1]";

    public static final String secondTab = "//*[@class='schemes']/div[2]";

    public static final String newAddTab = "//*[@class='schemes']/div[4]";

    public static final String settingTab = "//*[@class='schemes']/div[5]";

    public static final String currentMonth = "//*[@class='report-list-header']/div[3]/div/div/div/div/div/div/div/div[2]/ul/li[2]";

    public static final String searchBtn = "//*[@class='tosearch']";

    public static final String deleteBtn = "//*[@class='bottom-bar']/button[3]";
}
