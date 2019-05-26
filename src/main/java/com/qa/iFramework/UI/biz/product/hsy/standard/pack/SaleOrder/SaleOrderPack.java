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

public class SaleOrderPack extends SaleOrderBasePack {
    public SaleOrderPack(String prefix){
        String url = prefix +
                SaleOrderAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                SaleOrderAbst.postfixURL;
        saleOrderAbst.setPageUrl(url);
    }

    /*
    销售订单生成销货单
    前置条件：
        1.销售订单审批-有审
        2. 创建商品A，报价100，
        3. 创建库存A，商品A库存11
        4. 创建往来单位

        执行步骤：
        1. 使用有老板角色的账号创建销售订单，使用往来单位A，仓库A，商品A，数量2，报价100
        2. 结算方式 微信，金额20
        3. 录入物流信息、备注信息
        4. 保存
        5. 查找是否有生成-》销货单按钮，应该无
        6. 在当前销货订单页面点击审核
        7. 点击生成按钮
        8. 验证表头信息：客户、仓库、业务员
        9. 验证表体信息：明细行商品、计量单位、报价
        10. 金额合计、物流、备注、收款信息
        11. 勾选“使用预收款”，查看预收款和实际应收
        12. 保存
        13. 再次验证表头表体和收款信息
     */
    public String orderGenerateGoodsIssue(JSONObject data){
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
            setReceivables(data, false);
            clickGoodIssue(data, false);
            Thread.sleep(3000);
            WebElement generateBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.generateBtn), 10, false);
            generateBtn.click();
            Thread.sleep(1000);
            //查找未审核的销售订单是否可点击生成销货单按钮
            WebElement generateGoodsIssueBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.generateGoodsIssueBtn), 10, false);
            if(null != generateGoodsIssueBtn && !generateGoodsIssueBtn.getCssValue("Color").equalsIgnoreCase("rgb(187, 187, 187)")){
                resultSummary.append("未审核的销售订单的生成销货单按钮不应该可点击");
            }
            //点击审核按钮
            save(SaleOrderAbst.goodsIssueButtonAreaClass, SaleOrderAbst.reviewBtn, 2000);
            WebElement ensureButton = ElementBase.findElement(By.xpath(SaleOrderAbst.ensureBtn), 10, false);
            ensureButton.click();
            Thread.sleep(3000);
            generateBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.generateBtn), 10, false);
            generateBtn.click();
            Thread.sleep(1000);
            generateGoodsIssueBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.generateGoodsIssueBtn), 10, false);
            if(null == generateGoodsIssueBtn || !generateGoodsIssueBtn.getCssValue("Color").equalsIgnoreCase("rgb(51, 51, 51)")){
                resultSummary.append("审核通过的销售订单的生成销货单按钮期望可点击，但点击无效!");
            }
            generateGoodsIssueBtn.click();
            Thread.sleep(5000);

            WebElement preCash = ElementBase.findElement(By.xpath(SaleOrderAbst.preCash), 10, false);
            preCash.click();
            Thread.sleep(2000);
            WebElement tabCloseBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.tabCloseBtn), 10, false);
            tabCloseBtn.click();
            Thread.sleep(1000);
            setSaveButtonLocalStorage(data, "goods");
            save(GoodsIssueAbst.goodsIssueButtonAreaClass, GoodsIssueAbst.saveButton, 6000);
            List<WebElement> checkInfos = ElementBase.FindElements(By.xpath(SaleOrderAbst.checkInfos), 10);
            if(16 == checkInfos.size()){
                //金额合计
                if(checkInfos.get(0).getText().equalsIgnoreCase(data.getString("amountSum"))){

                } else{
                    resultSummary.append("金额合计: " + checkInfos.get(0).getText());
                }
                //现结信息
                if(checkInfos.get(1).getText().equalsIgnoreCase(data.getString("cashInfo"))){

                } else{
                    resultSummary.append("现结信息: " + checkInfos.get(1).getText());
                }
                //优惠券抵扣
                if(checkInfos.get(2).getText().equalsIgnoreCase(data.getString("CouponDeduction"))){

                } else{
                    resultSummary.append("优惠券抵扣: " + checkInfos.get(2).getText());
                }
                //手续费
                if(checkInfos.get(3).getText().equalsIgnoreCase(data.getString("fee"))){

                } else{
                    resultSummary.append("手续费: " + checkInfos.get(3).getText());
                }
                //整单优惠
                if(checkInfos.get(4).getText().equalsIgnoreCase(data.getString("discount"))){

                } else{
                    resultSummary.append("整单优惠: " + checkInfos.get(4).getText());
                }
                //累计收款
                if(checkInfos.get(5).getText().trim().startsWith(data.getString("sumReceipts"))){

                } else{
                    resultSummary.append("累计收款: " + checkInfos.get(5).getText());
                }
                //成交金额
                if(checkInfos.get(6).getText().equalsIgnoreCase(data.getString("turnoverAmount"))){

                } else{
                    resultSummary.append("成交金额: " + checkInfos.get(6).getText());
                }
                //本单欠款
                if(checkInfos.get(7).getText().trim().startsWith(data.getString("debt"))){

                } else{
                    resultSummary.append("本单欠款: " + checkInfos.get(7).getText());
                }
                //交货方式
                if(checkInfos.get(8).getText().trim().equalsIgnoreCase(data.getString("delivery"))){

                } else{
                    resultSummary.append("交货方式: " + checkInfos.get(8).getText());
                }
                //客户地址
                if(checkInfos.get(9).getText().equalsIgnoreCase(data.getString("logisticAddress"))){

                } else{
                    resultSummary.append("客户地址: " + checkInfos.get(9).getText());
                }
                //运费承担方
                if(checkInfos.get(10).getText().equalsIgnoreCase(data.getString("logisticFeeOwner"))){

                } else{
                    resultSummary.append("运费承担方: " + checkInfos.get(10).getText());
                }
                //运费
                if(checkInfos.get(11).getText().equalsIgnoreCase(data.getString("freight"))){

                } else{
                    resultSummary.append("运费: " + checkInfos.get(11).getText());
                }
                //交货备注
                if(checkInfos.get(12).getText().equalsIgnoreCase(data.getString("comments"))){

                } else{
                    resultSummary.append("交货备注: " + checkInfos.get(12).getText());
                }
                //票据类型
                if(checkInfos.get(13).getText().equalsIgnoreCase(data.getString("billType"))){

                } else{
                    resultSummary.append("票据类型: " + checkInfos.get(13).getText());
                }
                //发票号
                if(checkInfos.get(14).getText().equalsIgnoreCase(data.getString("invoiceNumber"))){

                } else{
                    resultSummary.append("发票号: " + checkInfos.get(14).getText());
                }
                //发票金额
                if(checkInfos.get(15).getText().equalsIgnoreCase(data.getString("invoiceAmount"))){

                } else{
                    resultSummary.append("发票金额: " + checkInfos.get(15).getText());
                }

            } else{
                resultSummary.append("生成的销货单的信息比对项目少于期望的16个，请检查页面收款、物流、发票信息");
            }
        } catch (Exception ex){
            resultSummary.append(ex);
        }
        return resultSummary.toString();
    }

    public static String setReceivables(JSONObject jsonObject, Boolean isVerify) throws Exception{
        StringBuilder receivableBuilder = new StringBuilder();
        try {
            //收款方式
            WebElement paymentWayInput = ElementBase.findElement(By.xpath(SaleOrderAbst.paymentWayId), 10, false);
            paymentWayInput.sendKeys(jsonObject.getString(SaleOrderAbst.paymentWayDataKey));
            Thread.sleep(2000);
            paymentWayInput.sendKeys(Keys.ENTER);
            Thread.sleep(1000);
            //收款金额
            WebElement paymentAmount = ElementBase.findElement(
                    By.xpath(SaleOrderAbst.paymentWay), 10, false);
            paymentAmount.sendKeys(jsonObject.getString(SaleOrderAbst.paymentAmountDataKey));
            Thread.sleep(1000);
            if (isVerify) {

            }
        } catch (Exception ex){
            return "设置收款方式遇到异常:" + ex.getMessage();
        }
        return "设置收款方式完成";
    }

    /*
        保存单据
   */
    public static String save(String mainAreaClassName, String xpath, Integer number) throws Exception{
        StringBuilder saveGoodsIssueBuilder = new StringBuilder();
        try{
            //保存按钮class
            WebElement mainArea = Browser.driver.findElement(By.className(mainAreaClassName));
            //保存按钮xpath
            WebElement saveButton = mainArea.findElement(By.xpath(xpath));
            saveButton.click();
            Thread.sleep(number);
        } catch (Exception ex){
            return saveGoodsIssueBuilder.append(" 保存单据 遇到异常:" + ex.getMessage()).toString();
        }
        return "保存单据 完成!";
    }
}
