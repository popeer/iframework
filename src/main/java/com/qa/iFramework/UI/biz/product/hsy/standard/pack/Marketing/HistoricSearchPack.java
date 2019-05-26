package com.qa.iFramework.UI.biz.product.hsy.standard.pack.Marketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.HistoricSearchAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.BasePack;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;

public class HistoricSearchPack extends BasePack {
    protected HistoricSearchAbst historicSearchAbst = new HistoricSearchAbst();

    public HistoricSearchPack(String prefix){
        String url = prefix + HistoricSearchAbst.postfixURL;
        historicSearchAbst.setPageUrl(url);
    }

    /*
    前置条件：
    0）创建商品分类
    1）创建包含两个商品的销货单A；2）创建包含三个商品的销货单B,包含一个赠品，一个数量为负，一个正常.
    UI自动化步骤：
    1. 查询今天，往来客户A的结果，有两条；
    2. 查询今天，往来客户B的结果，有三条；
    3. 查询今天，往来客户B的结果，输入商品C,有一条；因为数量为负、赠品都不出现在历史价跟踪里
    4. 勾选“只看最后一次”， 查询，有一条
    5. 导出，验证chrome浏览器导出的文件是否包含sto.chanapp.chanjet.com
     */
    public String searchHistoryPrice(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(historicSearchAbst.getPageUrl());
            System.out.println("打开历史价查询成功"  + df.format(new Date()));
            Thread.sleep(10000);

            //日期选择今天
            WebElement currentDay = ElementBase.findElement(By.xpath(HistoricSearchAbst.currentDay), 10, false);
            currentDay.click();
            Thread.sleep(1000);
            //往来单位选择客户A
            WebElement customer = ElementBase.findElement(By.xpath(HistoricSearchAbst.customer), 10, false);
            customer.clear();
            customer.sendKeys(Keys.BACK_SPACE);
            customer.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            customer.sendKeys(Keys.DELETE);
            Thread.sleep(1000);
            customer.sendKeys(data.getString("customer1"));
            Thread.sleep(2000);
            customer.sendKeys(Keys.SPACE);
            Thread.sleep(1000);
            //查询
            WebElement searchBtn = ElementBase.findElement(By.xpath(HistoricSearchAbst.searchBtn), 10, false);
            searchBtn.click();
            Thread.sleep(5000);
            //验证数据
            checkGridData(data, resultSummary, 2, "result1");

            //清空往来单位，输入往来单位B
            customer.clear();
            customer.sendKeys(Keys.BACK_SPACE);
            customer.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            customer.sendKeys(Keys.DELETE);
            Thread.sleep(1000);
            customer.sendKeys(data.getString("customer2"));
            Thread.sleep(2000);
            customer.sendKeys(Keys.SPACE);
            Thread.sleep(1000);

            //查询
            searchBtn.click();
            Thread.sleep(5000);

            //结果验证
            checkGridData(data, resultSummary, 1, "result2");

            //输入商品C
            WebElement product2 = ElementBase.findElement(By.xpath(HistoricSearchAbst.product), 10, false);
            product2.sendKeys(data.getString("product"));
            Thread.sleep(2000);
            product2.sendKeys(Keys.ENTER);
            Thread.sleep(2000);
            //查询
            searchBtn.click();
            Thread.sleep(5000);
            //结果验证
            checkGridData(data, resultSummary, 1, "result2");

            //勾选只看最后一次
            WebElement lastTimeOnly = ElementBase.findElement(By.xpath(HistoricSearchAbst.lasttimeOnly), 10, false);
            lastTimeOnly.click();
            Thread.sleep(1000);
            //查询
            searchBtn.click();
            Thread.sleep(5000);
            //结果验证
            checkGridData(data, resultSummary, 1, "result2");

            //导出
//            WebElement exportBtn = ElementBase.findElement(By.xpath(HistoricSearchAbst.exportBtn), 10, false);
//            exportBtn.click();
//            Thread.sleep(10000);

//            Browser.driver.get("chrome://downloads/");
//            System.out.println("打开浏览器下载列表"  + df.format(new Date()));
//            Thread.sleep(4000);
//            checkDownloadFile(resultSummary, "//*[@id=\"file-link\"]", "herf");
        } catch (Exception ex){
            resultSummary.append(ex);
        }
        return resultSummary.toString();
    }

    public static void checkDownloadFile(StringBuilder resultSummary, String xpath, String attribut) {
        WebElement downloadFile = ElementBase.findElement(By.xpath(xpath), 10, false);
        if(null != downloadFile){
            resultSummary.append("下载文件没有找到");
        }
        String fileLink = downloadFile.getAttribute(attribut);
        if(StringUtils.isEmptyOrSpace(fileLink)){
            resultSummary.append("没有下载销售历史价文件");
        }
        if(!fileLink.contains("sto.chanapp.chanjet.com")){
            resultSummary.append("下载的销售历史价文件不正确");
        }
    }

    public static void checkGridData(JSONObject data, StringBuilder resultSummary, int number, String dataKey) {
        WebElement detailGrid = getGridCells(GoodsIssueAbst.detailGrid,0);
        List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
        if(null == cells || 0 == cells.size()){
            resultSummary.append("历史价查询结果为空! 失败!");
            return ;
        }
        if(0 >= number){
            return;
        }
        JSONArray details = data.getJSONArray(dataKey);
        int counter = 0;
        for(Object element : details){
            JSONObject row = (JSONObject) element;
            for(WebElement we : cells){
                String dataRowIndex = we.getAttribute("data-row-index");
                String fieldName = we.getAttribute("data-col-field");
                if (null == we || null == we.getText() || null == fieldName) {
                    continue;
                }

                if(fieldName.equalsIgnoreCase("productId") && row.getString(fieldName).equalsIgnoreCase(we.getText())){
                    counter++;
                    break;
                }
            }
        }

        if(number != counter){
            resultSummary.append(("检查行数不匹配，期望检查" + number + "项，实际检查" + counter + "项"));
        }
    }
}
