package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

/**
 * Created by haijia on 2019/3/21.
 */
public class GoodsIssueAbst {
    //销货单url
    private String pageUrl;

    //销货单前缀url
    public static final String prefixURL = "/index.html#/voucher-goods-issue-new/";

    //销货单后缀url
    public static final String postfixURL = "?activePrompt=true&closePrompt=true&closeable=true&leavePrompt=true&lock=false&pageId=voucher-goods-issue-new%2F1553138047664&pageParams=%7B%7D&path=voucher-goods-issue-new%2F%3Ats&tabId=voucher-goods-issue-new&tabLabel=%E9%94%80%E8%B4%A7%E5%8D%95&_k=221v0p";

    //客户
    public static final  String customer = "//*[@id=\"react-select-3--value\"]/div/input";

    public static final  String customerKey = "customer";

    public static final  String wareseHourseKey = "wareseHourse";

    //仓库
    public static final String wareseHourse = "//*[@id=\"react-select-4--value\"]/div/input";

    //票据类型
    public static final String billType = "//*[@id=\"react-select-5--value\"]/div/input";

    //优惠卷匹配按钮
    public static final String matchCouponButton = "./div[1]/span/div[2]/div[1]/div[1]/div[1]/button";

    //整单优惠
    public static final String discountInput = "./div[1]/div[3]/div/span/input";
    //整单优惠关键字
    public static final String discountInputDataKey = "discount";

    //保存按钮区
    public static final String goodsIssueButtonAreaClass = "voucher-bottomBar-QwertButtonGroup";
    //放弃保存按钮
    public static final String dropButton = "./button[1]";
    //保存草稿按钮
    public static final String draftButton = "./button[2]";
    //保存按钮
    public static final String saveButton = "./div/button[1]";

    //销货单价格详情xpath
    public static final String detailGrid = "//div[@role='grid' and @class='ReactVirtualized__Grid']";

    //销货单价格详情关键字
    public static final String detailDataKey = "detail";

    //销货单数量价格数据推演初始化关键字
    public static final String initDataKey = "init";

    //销货单数量价格数据推演数据关键字
    public static final String deductionDataKey = "deduction";

    //销货单数量价格数据推演修改字段关键字
    public static final String updateFieldDataKey = "update";

    //销货单数量价格数据推演修改字段关键字
    public static final String expectedFieldDataKey = "expected";

    //销货单数量价格数据推演步骤描述字段关键字
    public static final String stepDescDataKey = "desc";

    //成交金额xpath
    public static final String turnoverAmount = "./div[1]/div[5]/div/span[2]/input";

    //成交金额关键字
    public static final String turnoverAmountDataKey = "turnoverAmount";

    //金额合计Class
    public static final String priceSumClass = "payment-info-view";
    //金额合计
    public static final String priceSum = "./div[1]/div[1]/span[2]";

    //金额合计关键字
    public static final String priceSumDataKey = "priceSum";

    //收款方式->结算方式记录线下收款
    public static final String paymentOfflineClass = "proceeds-component";
    public static final String paymentOfflineXpath = "./div/div/div/div/div/div[2]/span[2]/span";

    //收款方式->记录线下收款的结算方式
    public static final String paymentWayClass = "payment-item";
    //收款方式->结算方式INPUT
    public static final String paymentWayInput = "./div/div/div/div[2]/div/div/div/span[1]/div/input";
    //收款方式->线下收款结算方式的关键字
    public static final String paymentWayDataKey = "paymentWay";

    //收款方式->收款账号
    public static final String paymentAccount = "./div[2]/div/div/div[2]/div/div/div/span/div/input";

    //收款金额
    public static final String paymentAmountClass = "pi-cell-amount";
    public static final String paymentAmountXpath = "./div/div/div[2]/span/input";
    //收款金额关键字
    public static final String paymentAmountDataKey = "paymentAmount";

    //物流信息
    public static final String logisticInfoClass = "logistic-info-view";
    //物流选项
    public static final String logisticWayCheckBox = "./div/div/div/div/span[3]/span";

    //物流公司
    public static final String logisticCompany = "./div/div[2]/div/div/div[2]/div/div/div/span/div/input";
    //物流单号
    public static final String logisticNumber = "./div/div[2]/div[2]/div/div[2]/span/input";
    //物流费用承担方
    public static final String logisticFeeOwner = "./div/div[2]/div[3]/div/div/div/div[2]/div/div/span/div/input";
    //物流客户地址
    public static final String logisticAddress = "./div/div[2]/div[3]/div[2]/div/div/div/button";
    //物流公司关键字
    public static final String logisticCompanyDataKey = "logisticCompany";
    //物流单号关键字
    public static final String logisticNumberDataKey = "logisticNumber";
    //物流费用承担方关键字
    public static final String logisticFeeOwnerDataKey = "logisticFeeOwner";
    //物流客户地址关键字
    public static final String logisticAddressDataKey = "logisticAddress";

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
