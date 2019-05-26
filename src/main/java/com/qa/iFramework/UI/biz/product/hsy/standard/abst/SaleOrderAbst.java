package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

public class SaleOrderAbst {
    //销售订单页面地址
    private String pageUrl;
    //销售订单前缀url
    public static final String prefixURL = "/index.html#/voucher-sales-order-new/";
    //销售订单后缀
    public static final String postfixURL = "?activePrompt=true&closePrompt=true&closeable=true&leavePrompt=true&lock=false&pageId=voucher-sales-order-new%2F1556005010178&pageParams=%7B%7D&path=voucher-sales-order-new%2F%3Ats&tabId=voucher-sales-order-new&tabLabel=%E9%94%80%E5%94%AE%E8%AE%A2%E5%8D%95";

    //生成销货单按钮
    public static final String generateGoodsIssueBtn = "//*[@class='voucher-menu-dropdown-item']/button";

    //生成按钮
    public static final String generateBtn = "//*[@class='voucher-menu-box']/button[2]";

    //取消按钮
    public static final String cancleBtn = "//*[@class='chanjet-nova-ui-base-dialog']/div[2]/div/div/div/div/div/div/div/button[1]";

    //是否审核通过提示框--确定按钮
    public static final String ensureBtn = "//*[@class='chanjet-nova-ui-base-dialog']/div[2]/div/div/div/div/div/div/div/button[2]";

    //底部商品种类
    public static final String footerProductCategoryNumber = "//*[@class='voucher-footer-remark']/div/span[2]";

    //底部成交金额
    public static final String footerAmount = "//*[@class='voucher-footer-remark']/div[2]/span[2]";

    //客户
    public static final  String customer = "//*[@id=\"react-select-3--value\"]/div/input";

    public static final  String customerKey = "customer";

    //结算方式
    public static final  String paymentWayId = "//*[@id='react-select-14--value']/div/input";
    //结算金额
    public static final String paymentWay = "//*[@class='piv-order-type-field']/div/div/div[2]/span/input";

    //保存按钮区
    public static final String goodsIssueButtonAreaClass = "voucher-bottomBar-QwertButtonGroup";
    //放弃保存按钮
    public static final String dropButton = "./button[1]";
    //保存草稿按钮
    public static final String draftButton = "./button[2]";
    //审核
    public static final String reviewBtn = "./button[3]";
    //保存按钮
    public static final String saveButton = "./div/button[1]";
    //预收款input
    public static final String preCash = "//*[@class='piv-footer']/div/div/div/span/input";
    //收款方式->线下收款结算方式的关键字
    public static final String paymentWayDataKey = "paymentWay";
    //收款金额关键字
    public static final String paymentAmountDataKey = "paymentAmount";

    //销售订单价格详情关键字
    public static final String detailDataKey = "detail";

    public static final String tabCloseBtn = "//*[@class='TabLabelCloseButton']";

    public static final String checkInfos = "//*[@class='invoice-item-value']";

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
