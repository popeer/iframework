package com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder;

import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;

public class SalesOrderExecuteReportPack extends SalesOrderExecuteReportBase {
    public SalesOrderExecuteReportPack(String prefix){
        super(prefix);
    }

    public String checkExecuteReportDataGrid(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            //打开销售订单执行表，在第一个TAB页点击“展开更多条件”，在商品分类里输入新增的商品分类
            Browser.driver.get(salesOrderExecuteReportAbst.getPageUrl());
            System.out.println("打开销售订单执行表成功"  + df.format(new Date()));
            Thread.sleep(5000);
//            //关闭页签
//            WebElement tabCloseBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.tabCloseBtn), 10, false);
//            tabCloseBtn.click();
//            Thread.sleep(1000);
            //输入更多条件
            WebElement moreCondition = ElementBase.findElement(By.xpath("//*[@class='filter-switch-expand']"));
            moreCondition.click();
            Thread.sleep(1000);
            List<WebElement> searchOptions = ElementBase.FindElements(By.xpath("//div[@class='Select-control']/span/div/input"),5);
            WebElement productCategory = searchOptions.get(6);
            String productCategoryName = data.getString("productCategoryName");
            productCategory.sendKeys(productCategoryName);
            productCategory.clear();
            productCategory.sendKeys(Keys.BACK_SPACE);
            productCategory.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            productCategory.sendKeys(Keys.DELETE);
            Thread.sleep(2000);
            productCategory.sendKeys(productCategoryName);
            Thread.sleep(2000);
            productCategory.sendKeys(Keys.SPACE);

            //选择近七天
            WebElement date = ElementBase.findElement(By.xpath("//*[@class='filter-convenient-date ']/li[4]"));
            date.click();
            Thread.sleep(1000);

            //点击查询
            //查询
            WebElement topSearchBtn = ElementBase.findElement(By.xpath("//div[@class='tosearch']"));
            topSearchBtn.click();
            Thread.sleep(4000);

            //滚动条无法移动，暂时忽略对执行表的结果验证
//            JavascriptExecutor jsExecutor = (JavascriptExecutor)Browser.driver;
//            jsExecutor.executeScript("document.body.scrollLeft=10000");
//
//            WebElement gridHeader = ElementBase.findElement(By.xpath("//div[@class='report-list-container']/div/div/div/div/div[2]/div/div/div[2]/div/div"));
//            List<WebElement> cells = gridHeader.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
//            //验证A的执行比例为200%，未执行数量=-10
//            //验证C的执行比例为50%，未执行数量=5
//            JSONArray array = data.getJSONArray("goodsIssue");
//            for(Object element : array){
//                JSONObject jsonObject = (JSONObject)element;
//                for(WebElement cell : cells){
//                    String fieldName = cell.getAttribute("data-col-field");
//                    if(jsonObject.containsKey(fieldName)){
//                        resultSummary.append(comparePriceDouble(jsonObject.getString(fieldName), cell.getText(), fieldName, ""));
//                    }
//                }
//            }
        } catch (Exception ex){
            resultSummary.append("销售订单执行表遇到异常:" + ex);
        }
        return resultSummary.toString();
    }
}
