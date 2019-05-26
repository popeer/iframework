package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

/**
 * 好生意标准版商品页抽象
 */
public class ProductPageAbst {

    //价格的xpath
    public static final String priceGrid = "//div[@role='grid' and @class='ReactVirtualized__Grid']";

    //商品名称的xpath
    public static final String productName = "//*[@id=\"app\"]/div/div/div[2]/div[3]/div/div/div[2]/div/article[3]/div/div[1]/div/div/div[1]/div[2]/div[3]/div[1]/div[2]/div/div[2]/span/input";

    //计量单位
    public static final String uom = "react-select-9--value";

    //可点击的计量单位输入框
    public static final String uomInput = "./div/input";

    //保存按钮
    public static final String saveBtn = "//*[@id=\"app\"]/div/div/div[2]/div[3]/div/div/div[2]/div/article[3]/div/div[1]/div/footer/div[2]/button[2]";
}
