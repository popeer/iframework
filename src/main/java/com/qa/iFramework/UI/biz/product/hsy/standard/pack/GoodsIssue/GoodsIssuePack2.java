package com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.common.Util.DateUtil;
import com.qa.iFramework.common.Util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;

/**
 * Created by haijia on 2019/3/21.
 */
public class GoodsIssuePack2 extends GoodsIssueBasePack {
    public GoodsIssuePack2(String prefix){
        String url = prefix +
                GoodsIssueAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                GoodsIssueAbst.postfixURL;
        goodsIssueAbst.setPageUrl(url);
    }
    /*
    验证数量金额价格算法数据推演
     */
    public String algorithmicDataDeduction(JSONObject data) {
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(goodsIssueAbst.getPageUrl());
            System.out.println("打开销货单成功"  + df.format(new Date()));
            Thread.sleep(10000);

            setSaveButtonLocalStorage(data, "goods");

            //设置客户、仓库、单据类型
            preStepCreateGoodsIssue(data);

            Object initData = data.get(GoodsIssueAbst.initDataKey);
            JSONObject initJsonObject = (JSONObject)initData;

            Object deduction = data.get(GoodsIssueAbst.deductionDataKey);
            JSONArray jsonArray = JSONArray.parseArray(deduction.toString());

            // 输入商品明细grid
            // 查找价格grid并且对指定价格赋值
            try{
                for(Object element : jsonArray){
                    JSONObject row = (JSONObject)element;
                    JSONObject updatedJson = JSONObject.parseObject(row.getString(GoodsIssueAbst.updateFieldDataKey));
                    JSONObject expectJson = JSONObject.parseObject(row.getString(GoodsIssueAbst.expectedFieldDataKey));
                    String stepDesc = row.getString(GoodsIssueAbst.stepDescDataKey);
                    //初始化数据
                    initGridValue(initJsonObject);

                    WebElement detailGrid = getGridCells(GoodsIssueAbst.detailGrid,0);
                    List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
                    for(WebElement we : cells){
                        String fieldName = we.getAttribute("data-col-field");
                        String dataRowIndex = we.getAttribute("data-row-index");
                        //只设置一行数据
                        if(0 < Integer.valueOf(dataRowIndex)){
                            break;
                        }
                        //修改数据
                        if(updatedJson.containsKey(fieldName)){
                            Thread.sleep(1000);
                            we.click();
                            Thread.sleep(1000);
                            WebElement webElement = Browser.driver.switchTo().activeElement();
                            webElement.sendKeys(updatedJson.getString(fieldName));
                            webElement.sendKeys(Keys.ESCAPE);
                            detailGrid.click();
                            Thread.sleep(1000);
                            continue;
                        }
                    }
                    //验证数据
                    for(WebElement we : cells){
                        String fieldName = we.getAttribute("data-col-field");
                        String dataRowIndex = we.getAttribute("data-row-index");
                        //只设置一行数据
                        if(0 < Integer.valueOf(dataRowIndex)){
                            break;
                        }

                        if(!expectJson.containsKey(fieldName)){
                            continue;
                        }
                        resultSummary.append(comparePriceDouble(expectJson.getString(fieldName), we.getText(),fieldName, stepDesc));
                    }
                }

            } catch (Exception ex){
                resultSummary.append(ex);
                return resultSummary.toString();
            }
        } catch (Exception ex){
            resultSummary.append(ex);
            return resultSummary.toString();
        }

        return resultSummary.toString();
    }

    private static boolean initGridValue(JSONObject entry) throws Exception{
        WebElement detailGrid = getGridCells(GoodsIssueAbst.detailGrid,0);
        List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
        for(WebElement we : cells) {
            String fieldName = we.getAttribute("data-col-field");
            String dataRowIndex = we.getAttribute("data-row-index");
            //只设置一行数据
            if(0 < Integer.valueOf(dataRowIndex)){
                break;
            }
            //录入商品
            if (fieldName.toLowerCase().trim().equals("productid")) {
                //第一行商品有值了就不再赋值
                if(StringUtils.isEmptyOrSpace(we.getText())){
                    we.click();
                    Thread.sleep(2000);
                    WebElement combb = Browser.driver.switchTo().activeElement();
                    WebElement comboButton = combb.findElement(By.xpath("../../../span/button"));
                    comboButton.click();
                    Thread.sleep(4000);
                    //搜索文本框
                    System.out.println("搜索文本框");
                    WebElement searchInput = detailGrid.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div/div/div[1]/div[2]/div/div[1]/div[2]/div/div/div[1]/div[1]/div[2]/span/input"));
                    //输入商品ID
                    System.out.println("输入商品ID");
                    searchInput.sendKeys(entry.getString("productId"));
                    Thread.sleep(4000);
                    //点击搜索按钮
                    System.out.println("点击搜索按钮");
                    detailGrid.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div/div/div[1]/div[2]/div/div[1]/div[2]/div/div/div[1]/div[1]/div[2]/span/span/span/div/button")).click();
                    Thread.sleep(4000);
                    //勾选商品
                    System.out.println("勾选商品");
                    detailGrid.findElement(By.xpath("//*[@id=\"datagrid-title-btn-wrap\"]/div/div[1]/div/div[2]/div[1]/div[1]/div[1]/div/div[2]/div/div/div/div/span/input")).click();
                    Thread.sleep(4000);
                    //确认
                    System.out.println("确认");
                    detailGrid.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/div/div/div/div[1]/div[3]/button[2]")).click();
                    Thread.sleep(4000);
                    detailGrid.click();
                    Thread.sleep(1000);
                }

                continue;
            }

            if(entry.containsKey(fieldName)){
                we.click();
                Thread.sleep(1000);
                WebElement webElement = Browser.driver.switchTo().activeElement();
                webElement.sendKeys(entry.getString(fieldName));
                webElement.sendKeys(Keys.ESCAPE);
                continue;
            }
        }

        detailGrid.click();
        return true;
    }
}
