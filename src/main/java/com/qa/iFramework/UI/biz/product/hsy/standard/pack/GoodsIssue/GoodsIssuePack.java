package com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.DateUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Date;
import java.util.List;

/**
 * Created by haijia on 2019/3/21.
 */
public class GoodsIssuePack extends GoodsIssueBasePack {
    public GoodsIssuePack(String prefix){
        String url = prefix +
                GoodsIssueAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                GoodsIssueAbst.postfixURL;
        goodsIssueAbst.setPageUrl(url);
    }

    public String clickSaveButtonGetWarn(JSONObject data) throws Exception{
        StringBuilder resultSummary = new StringBuilder();
        Browser.driver.get(goodsIssueAbst.getPageUrl());
        System.out.println("打开销货单成功"  + df.format(new Date()));
        Thread.sleep(10000);

        setSaveButtonLocalStorage(data, "goods");

        //输入客户
        WebElement customer = ElementBase.findElement(By.xpath(GoodsIssueAbst.customer), 100, false);
        customer.sendKeys(data.getString(GoodsIssueAbst.customerKey));
        Thread.sleep(2000);
        customer.sendKeys(Keys.SPACE);

        //输入仓库
        WebElement warseHourse = ElementBase.findElement(By.xpath(GoodsIssueAbst.wareseHourse), 10, false);
        warseHourse.clear();
        warseHourse.sendKeys(Keys.BACK_SPACE);
        warseHourse.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        warseHourse.sendKeys(Keys.DELETE);
        Thread.sleep(1000);
        warseHourse.sendKeys(data.getString(GoodsIssueAbst.wareseHourseKey));
        Thread.sleep(2000);
        warseHourse.sendKeys(Keys.SPACE);

        //设置票据类型，如果是未开票，则不显示无税金额列
        WebElement billType = ElementBase.findElement(By.xpath(GoodsIssueAbst.billType), 10, false);
        billType.clear();
        billType.sendKeys(Keys.BACK_SPACE);
        billType.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        billType.sendKeys(Keys.DELETE);
        Thread.sleep(1000);
        billType.sendKeys("增值税普通发票");
        Thread.sleep(2000);
        billType.sendKeys(Keys.SPACE);

        //设置明细的数据
        Object detail = data.get(GoodsIssueAbst.detailDataKey);
        JSONArray jsonArray = JSONArray.parseArray(detail.toString());

        // 输入商品明细grid
        // 查找价格grid并且对指定价格赋值
        try{
            inputProductDetailGridValue(jsonArray);
            setPreferential(data,false);
            setReceivables(data, false);
            setLogistic(data, false);
            clickGoodIssue(data, false);
        } catch (Exception ex){
            System.out.println(ex);
            resultSummary.append(ex);
        }

        return resultSummary.toString();
    }

    /*
    保存单据
     */
    public static String clickGoodIssue(JSONObject jsonObject, Boolean isVerify) throws Exception{
        StringBuilder saveGoodsIssueBuilder = new StringBuilder();
        try{
            //保存按钮class
            WebElement mainArea = Browser.driver.findElement(By.className(GoodsIssueAbst.goodsIssueButtonAreaClass));
            //保存按钮xpath
            WebElement saveButton = mainArea.findElement(By.xpath(GoodsIssueAbst.saveButton));
            saveButton.click();
            Thread.sleep(3000);
        } catch (Exception ex){
            return saveGoodsIssueBuilder.append(" 保存单据 遇到异常:" + ex.getMessage()).toString();
        }
        return "保存单据 完成!";
    }

    /*
    设置物流信息
     */
    private String setLogistic(JSONObject jsonObject, Boolean isVerify) throws Exception{
        StringBuilder logisticBuilder = new StringBuilder();
        try {
            //物流信息
            WebElement mainArea = ElementBase.findElement(By.className(GoodsIssueAbst.logisticInfoClass), 10 ,false);
            //物流选项
            WebElement logisticWayCheckBox = mainArea.findElement(By.xpath(GoodsIssueAbst.logisticWayCheckBox));
            logisticWayCheckBox.click();
            Thread.sleep(2000);
            Actions actions = new Actions(Browser.driver);
            actions.sendKeys(logisticWayCheckBox, Keys.PAGE_DOWN).perform();
            Thread.sleep(1000);
            //物流公司
            WebElement logisticCompany = mainArea.findElement(By.xpath(GoodsIssueAbst.logisticCompany));
            logisticCompany.sendKeys(jsonObject.getString(GoodsIssueAbst.logisticCompanyDataKey));
            Thread.sleep(500);
            logisticCompany.sendKeys(Keys.ENTER);
            //物流单号
            WebElement logisticNumber = mainArea.findElement(By.xpath(GoodsIssueAbst.logisticNumber));
            logisticNumber.sendKeys(jsonObject.getString(GoodsIssueAbst.logisticNumberDataKey));
            //物流费用承担方
            WebElement logisticFeeOwner = mainArea.findElement(By.xpath(GoodsIssueAbst.logisticFeeOwner));
            logisticFeeOwner.sendKeys(jsonObject.getString(GoodsIssueAbst.logisticFeeOwnerDataKey));
            Thread.sleep(500);
            logisticFeeOwner.sendKeys(Keys.SPACE);

            //物流客户地址
//        WebElement logisticAddress = mainArea.findElement(By.xpath(GoodsIssueAbst.logisticAddress));
//        logisticAddress.sendKeys();

            if (isVerify) {

            }
        } catch (Exception ex){
            return "设置物流信息遇到异常:" + ex.getMessage();
        }
        return "设置物流信息完成";
    }

    /*
    设置销货单优惠
     */
    private String setPreferential(JSONObject jsonObject, Boolean isVerify) throws Exception{
        StringBuilder containerBuilder = new StringBuilder();
        try {
            //金额合计
            WebElement mainArea = ElementBase.findElement(By.className(GoodsIssueAbst.priceSumClass), 10, false);
            WebElement priceSumElement = mainArea.findElement(By.xpath(GoodsIssueAbst.priceSum));

            //优惠券匹配
            WebElement matchCouponButtonElement = mainArea.findElement(By.xpath(GoodsIssueAbst.matchCouponButton));
            matchCouponButtonElement.click();
            //整单优惠
            WebElement discountInputElement = mainArea.findElement(By.xpath(GoodsIssueAbst.discountInput));
            discountInputElement.sendKeys(jsonObject.getString(GoodsIssueAbst.discountInputDataKey));
            //结算方式
//        WebElement priceSumElement = mainArea.findElement(By.xpath(GoodsIssueAbst.priceSum));

            //成交金额
            WebElement turnoverAmountElement = mainArea.findElement(By.xpath(GoodsIssueAbst.turnoverAmount));

            if (isVerify) {
                //验证金额合计
                containerBuilder.append(comparePriceDouble(
                        jsonObject.getString(GoodsIssueAbst.priceSumDataKey),
                        priceSumElement.getText(), "金额合计"));
                //验证成交金额
                containerBuilder.append(comparePriceDouble(
                        jsonObject.getString(GoodsIssueAbst.turnoverAmountDataKey),
                        turnoverAmountElement.getText(), "成交金额"));
            }
        } catch (Exception ex){
            return "设置销货单优惠遇到异常:" + ex.getMessage();
        }
        return "设置销货单优惠:" + containerBuilder.toString();
    }

    /*
        设置收款方式
     */
    public static String setReceivables(JSONObject jsonObject, Boolean isVerify) throws Exception{
        StringBuilder receivableBuilder = new StringBuilder();
        try {
            //记录线下收款
            WebElement paymentOffline = ElementBase.findElement(By.className(GoodsIssueAbst.paymentOfflineClass), 10, false);
            WebElement paymentOfflineCheckBox = paymentOffline.findElement(By.xpath(GoodsIssueAbst.paymentOfflineXpath));
//        paymentOfflineCheckBox.sendKeys(Keys.PAGE_DOWN);
            Actions actions = new Actions(Browser.driver);
            actions.sendKeys(paymentOfflineCheckBox, Keys.PAGE_DOWN).perform();
            Thread.sleep(1000);
            paymentOfflineCheckBox.click();
            Thread.sleep(1000);

            //收款方式
            WebElement paymentWayClass = ElementBase.findElement(By.className(GoodsIssueAbst.paymentWayClass), 10, false);
            WebElement paymentWay = paymentWayClass.findElement(By.xpath(GoodsIssueAbst.paymentWayInput));
            paymentWay.sendKeys(jsonObject.getString(GoodsIssueAbst.paymentWayDataKey));
            Thread.sleep(2000);
            paymentWay.sendKeys(Keys.SPACE);
            //收款金额
            WebElement paymentAmount = ElementBase.findElement(
                    By.className(GoodsIssueAbst.paymentAmountClass), 10, false).findElement(
                    By.xpath(GoodsIssueAbst.paymentAmountXpath));
            paymentAmount.sendKeys(jsonObject.getString(GoodsIssueAbst.paymentAmountDataKey));

            if (isVerify) {

            }
        } catch (Exception ex){
            return "设置收款方式遇到异常:" + ex.getMessage();
        }
        return "设置收款方式完成";
    }

    public String verifyNewGoodsIssue(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        JSONObject resultJson = new JSONObject();

        try{
            Thread.sleep(5000);
            //找到商品明细grid
            List<WebElement> grids = Browser.driver.findElements(By.xpath(GoodsIssueAbst.detailGrid));
            if(null == grids && 0 == grids.size()){
                resultJson.put("result", false);
                resultJson.put("message", resultSummary.append("not found GoodsIssue's price grid"));
                return resultJson.toJSONString();
            }
            //新建销货单功能，取第一个；第二个是推荐商品里的grid
            //若是其他场景，可以考虑传参方式来指定
            WebElement detailGrid = grids.get(0);

            List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
            Thread.sleep(1000);
            JSONArray priceArray = data.getJSONArray(GoodsIssueAbst.detailDataKey);

            if(null == cells || 0 == cells.size()){
                resultSummary.append("cells of grid found 0!").append('\n');
            }

            for(WebElement we : cells){
                String fieldName = we.getAttribute("data-col-field");
                String dataRowIndex = we.getAttribute("data-row-index");
                try{
                    if(priceArray.size() <= Integer.parseInt(dataRowIndex)){
                        break;
                    }
                    JSONObject entry = (JSONObject) priceArray.get(Integer.parseInt(dataRowIndex));

                    if(!entry.containsKey(fieldName)){
                        continue;
                    }
                    resultSummary.append(comparePriceDouble(entry.getString(fieldName), we.getText(), fieldName, dataRowIndex));;
                } catch (Exception ex){
                    resultSummary.append("FieldName: " + fieldName + " in DataRowIndex: " +
                            dataRowIndex + " encountering an Exception:" + ex.getMessage()).append('\n');
                }
            }

            //检查成交金额和金额合计
//            resultSummary.append(setPreferential(driver, jsonObject,true));
        } catch (Exception ex){
            resultJson.put("result", false);
            resultJson.put("message", resultSummary.append(ex));
            return resultJson.toJSONString();
        }

        resultSummary.append("verifyNewGoodsIssue is finished");
        resultJson.put("result", true);
        resultJson.put("message", resultSummary.toString());
        return resultJson.toJSONString();
    }

}
