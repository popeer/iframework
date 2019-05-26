package com.qa.iFramework.UI.biz.product.hsy.standard.pack;

import com.qa.iFramework.UI.biz.product.hsy.standard.abst.ProductPageAbst;
import com.qa.iFramework.UI.common.Controls.DataGrid;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductPack {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    public ProductPack(){

    }

    public ProductPack(String abc){

    }

    public Boolean saveNewProduct(Map<String, String> map) throws Exception{
        //找到价格grid
        List<WebElement> grids = Browser.driver.findElements(By.xpath(ProductPageAbst.priceGrid));
        WebElement priceGrid = grids.get(grids.size() - 1);

        //添加商品名称
        ElementBase.findElement(By.xpath(ProductPageAbst.productName), 10, false).sendKeys(map.get("name"));
        System.out.println("新增商品名称成功"  + df.format(new Date()));

        //输入计量单位
        WebElement uomElement = ElementBase.findElement(By.id(ProductPageAbst.uom), 10, false).findElement(By.xpath(ProductPageAbst.uomInput));
        uomElement.sendKeys(map.get("uom"));
        //必须等待长一点，页面逻辑是根据输入的内容推荐匹配的下拉菜单
        Thread.sleep(8000);
        uomElement.sendKeys(Keys.SPACE);
        Thread.sleep(2000);
        System.out.println("计量单位录入完毕");

        //价格列表的grid可能会没有显示在屏幕中，需要下拉滚动条到页中
        Actions actions = new Actions(Browser.driver);
        actions.sendKeys(grids.get(0), Keys.PAGE_DOWN).perform();

        //查找价格grid并且对指定价格赋值
        DataGrid.inputPriceGridValue(priceGrid, map);

        //保存商品
        ElementBase.findElement(By.xpath(ProductPageAbst.saveBtn), 10, false).click();
        Thread.sleep(2000);
        System.out.println("saving Product is successful"  + df.format(new Date()));
        return true;
    }
}
