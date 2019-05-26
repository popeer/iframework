package com.qa.iFramework.UI.biz.product.hsy.standard.pack;

import com.qa.iFramework.UI.biz.product.hsy.standard.abst.ProductListAbst;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductListPack {
    ProductListAbst productListAbst = new ProductListAbst();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    public ProductListPack(String pageUrl){
        productListAbst.setProductPageUrl(pageUrl);
    }

    public Boolean clickNewProductBtn(WebDriver driver) throws Exception{
        driver.get(productListAbst.getProductPageUrl());
        System.out.println("打开商品成功"  + df.format(new Date()));
        Thread.sleep(10000);

        //点击新增商品按钮
        driver.findElement(By.xpath(ProductListAbst.newProductBtn)).click();
        System.out.println("点击新增商品成功"  + df.format(new Date()));
        Thread.sleep(4000);
        return true;
    }


}
