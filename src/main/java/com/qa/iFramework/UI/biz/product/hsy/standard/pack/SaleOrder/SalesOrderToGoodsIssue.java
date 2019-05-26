package com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.SaleOrderAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue.GoodsIssueBasePack;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.DateUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;

import static com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue.GoodsIssuePack.clickGoodIssue;
import static com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder.SaleOrderPack.save;

public class SalesOrderToGoodsIssue extends SaleOrderBasePack {
    public SalesOrderToGoodsIssue(String prefix){
        String url = prefix +
                SaleOrderAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                SaleOrderAbst.postfixURL;
        saleOrderAbst.setPageUrl(url);
    }

    /*
        Case 3： 销售订单流转销货单状态
        前提条件：
        1) 新建商品A,库存期初=20
        2) 选定客户K
        3) 指定仓库,
        4) 销售订单和销货单都有审核环节
        5) 出库确认
        UI自动化步骤：
        1. 销售订单，商品数量为10，审核，保存
        2. 流转销货单1，关闭页签，首次流转4个，保存并审核
        3. 再次开销货单，关闭页签，选单，选择此销售订单，
        4. 验证明细表中，此商品明细的累计出库数量=0，累计通知发货量=4
        5. 点击确定
        6. 验证销货单中的商品数量为10-4=6，保存并审核
        7. 出库销货单1。
        8. 再次开销货单，关闭页签，选单，查看数据为空
        9. 去勾选“不显示已出库商品”，销售订单出现
        10.检查明细中累计出库数量=4，累计发货数量=10
     */
    public String salesOrderToGoodsIssue(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(saleOrderAbst.getPageUrl());
            System.out.println("打开销售订单成功"  + df.format(new Date()));
            Thread.sleep(10000);

            setSaveButtonLocalStorage(data, "sales");

            //输入客户
            WebElement customer = ElementBase.findElement(By.xpath(SaleOrderAbst.customer), 100, false);
            customer.sendKeys(data.getString(SaleOrderAbst.customerKey));
            Thread.sleep(2000);
            customer.sendKeys(Keys.ENTER);

            //设置明细的数据
            Object detail = data.get(SaleOrderAbst.detailDataKey);
            JSONArray jsonArray = JSONArray.parseArray(detail.toString());
            GoodsIssueBasePack.inputProductDetailGridValue(jsonArray);
            clickGoodIssue(data, false);
            Thread.sleep(3000);
            //点击审核按钮
            save(SaleOrderAbst.goodsIssueButtonAreaClass, SaleOrderAbst.reviewBtn, 2000);
            WebElement ensureButton = ElementBase.findElement(By.xpath(SaleOrderAbst.ensureBtn), 10, false);
            ensureButton.click();
            Thread.sleep(3000);
            WebElement generateBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.generateBtn), 10, false);
            generateBtn.click();
            Thread.sleep(1000);
            //流转销货单
            WebElement generateGoodsIssueBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.generateGoodsIssueBtn));
            if(null == generateGoodsIssueBtn || !generateGoodsIssueBtn.getCssValue("Color").equalsIgnoreCase("rgb(51, 51, 51)")){
                resultSummary.append("审核通过的销售订单的生成销货单按钮期望可点击，但点击无效!");
                return resultSummary.toString();
            }
            generateGoodsIssueBtn.click();
            Thread.sleep(5000);
            //关闭页签
            WebElement tabCloseBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.tabCloseBtn), 10, false);
            tabCloseBtn.click();
            Thread.sleep(1000);
            //首次流转4个，保存并审核
            List<WebElement> grids = ElementBase.FindElements(By.xpath(GoodsIssueAbst.detailGrid), 20);
            if(null == grids && 0 == grids.size()){
                return resultSummary.append("没有找到生成销货单的GRID").toString();
            }
            WebElement gridHeader = ElementBase.findElement(By.className("voucher-header"), 10, true);
            WebElement detailGrid = grids.get(0);

            List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
            Thread.sleep(1000);

            for(WebElement we : cells) {
                String fieldName = we.getAttribute("data-col-field");
                if (fieldName.toLowerCase().trim().equals("transQty")) {
                    gridHeader.click();
                    we.click();
                    Thread.sleep(2000);
                    we.sendKeys("4");
                    gridHeader.click();
                    Thread.sleep(1000);
                    break;
                }
            }
            //点击审核按钮
            save(SaleOrderAbst.goodsIssueButtonAreaClass, SaleOrderAbst.reviewBtn, 2000);
            //再次开销货单，关闭页签，选单，选择此销售订单，
            Browser.driver.get(saleOrderAbst.getPageUrl());
            System.out.println("打开销售订单成功"  + df.format(new Date()));
            Thread.sleep(10000);
            //选单
            selectOrder();
            //查询
            WebElement topSearchBtn = ElementBase.findElement(By.xpath("//div[@class='tosearch']"));
            topSearchBtn.click();
            Thread.sleep(4000);
            cells = search(data);
           if(null == cells || 0 == cells.size()){
               resultSummary.append("销货单选单没有查到上步的销售订单");
               return resultSummary.toString();
           }
           cells.get(0).click();
           Thread.sleep(1000);
            //点击确认按钮
            WebElement conformBtn = ElementBase.findElement(By.className("SalesOrder-voucherSelect"));
            conformBtn.click();
            Thread.sleep(4000);

            //验证明细表中，此商品明细的累计出库数量=0，累计通知发货量=4


        } catch (Exception ex){
            resultSummary.append(ex);
        }

        return resultSummary.toString();
    }
}
