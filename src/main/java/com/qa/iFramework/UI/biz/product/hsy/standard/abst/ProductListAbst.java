package com.qa.iFramework.UI.biz.product.hsy.standard.abst;

import com.qa.iFramework.common.Util.StringUtils;

/**
 * 好生意标准版商品列表页
 */
public class ProductListAbst {
    public String getProductPageUrl() {
        return productPageUrl;
    }

    public void setProductPageUrl(String productPageUrl) {
        if(StringUtils.isNotEmpty(productPageUrl)){
            this.productPageUrl = productPageUrl + this.productPageUrl;
        } else {
            this.productPageUrl = "";
        }
    }

    //导航到商品列表页的URL
    private String productPageUrl = "#/product?activePrompt=false&closePrompt=false&closeable=true&leavePrompt=false&lock=false&pageId=product&pageParams=%7B%7D&path=product&tabId=product&tabLabel=%E5%95%86%E5%93%81&_k=jpk53a";

    //新增按钮的xpath
    public static final String newProductBtn = "//*[@id='app']/div/div/div[2]/div[3]/div/div/div[2]/div/article[2]/div/div[1]/div/div/div/div[2]/div/div/div[1]/span/div[1]/div[2]/div/button[1]";



}
