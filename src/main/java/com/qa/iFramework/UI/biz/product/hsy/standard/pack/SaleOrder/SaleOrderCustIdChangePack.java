package com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.SaleOrderAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.BasePack;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.DateUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SaleOrderCustIdChangePack extends SaleOrderBasePack {
    public SaleOrderCustIdChangePack(String prefix){
        String url = prefix +
                SaleOrderAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                SaleOrderAbst.postfixURL;
        saleOrderAbst.setPageUrl(url);
    }

    /*
    Case1. 基本测试，默认项及选择客户
        前置条件：
        -1.设置销售订单单据模板，显示业务员、分管部门、分管人员、结算客户
        0. 新建部门A。
        1. 新建员工，部门是总部
        1. 新建员工，部门是部门
        2. 新建往来单位1
        3. 新建往来单位2，分管人员是新建的员工，分管部门是总部，结算单位是新建的往来单位1
        4. 新建往来单位3
        5. 新建往来单位4，分管人员是新建的员工，分管部门是新建的部门A，结算单位是新建的往来单位3

        用例步骤：
        1. 入口验证，菜单栏->销售->销售订单，打开新建销售订单页面
        2.单据日期默认今日，单据编号按照今天，自增
        3.将单据日期改成明日，查看单据号随日期改动的变动
        4.选择客户（有分管部门和分管人员），查看业务员和部门联动带入
        5.选择三条商品明细，录入商品A数量1、报价10，录入商品A数量10、报价30，录入商品A数量100、报价30、设为赠品
        6.选择切换一个有结算单位的客户（如，畅捷通，结算单位：用友集团）
        7.验证弹出提示，切换客户会重新取价。
        8.点击“毛利预估”
        9.毛利为330元，整单毛利率100%，商品C是赠品，不算毛利
        10.验证商品A的毛利是100%，商品B的毛利是100%

     */
    public String customerChangeCheck(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(saleOrderAbst.getPageUrl());
            System.out.println("打开销售订单成功"  + df.format(new Date()));
            Thread.sleep(10000);

            setSaveButtonLocalStorage(data, "sales");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String match = "SO-" + dateFormat.format(new Date()) + "-";
            //单据日期
            WebElement voucherDateNoDisplay = ElementBase.findElement(By.xpath("//*[@class='voucherDateNo']/div/div[2]/span[2]"));
            voucherDateNoDisplay.click();
            if(!voucherDateNoDisplay.getText().startsWith(match)){
                resultSummary.append(voucherDateNoDisplay.getText() + " 日期不符合要求");
            }

            //修改单据日期
            WebElement voucherDateNoInput = ElementBase.findElement(By.xpath("//*[@class='voucherDateNo']/div/div/div/span/input"));
            voucherDateNoInput.clear();
            Thread.sleep(1000);
            long newDate = DateUtil.getTimestampBeforDays(2);
            String later2Days = dateFormat.format(newDate);
            voucherDateNoInput.sendKeys(later2Days);
            Thread.sleep(2000);
            match = "SO-" + later2Days + "-";
            voucherDateNoDisplay = ElementBase.findElement(By.xpath("//*[@class='voucherDateNo']/div/div[2]/span[2]"));
            voucherDateNoDisplay.click();
            if(!voucherDateNoDisplay.getText().startsWith(match)){
                resultSummary.append(voucherDateNoDisplay.getText() + " 日期不符合要求");
            }

            //设置用户，检查用户带出的业务员、结算单位、分管人员、分管部门
            setCustomerCheckEmployeeDepartAdminBillToCustId(data, "customer1", "employee1",
                    "depart1", "admin1", "billtoCustVendorId1", resultSummary);

            //选择3条商品明细，录入数量和报价
            //设置明细的数据
            Object detail = data.get(SaleOrderAbst.detailDataKey);
            JSONArray jsonArray = JSONArray.parseArray(detail.toString());
            Map<String, String > elementLocation = BasePack.setProductDetailGridElementLocation();
//            elementLocation.put()
            BasePack.inputProductDetailGridValue(jsonArray, elementLocation);
            //选择切换一个有结算单位的客户（如畅捷通，结算单位：用友集团）
            setCustomerCheckEmployeeDepartAdminBillToCustId(data, "customer2", "employee2",
                    "depart2", "admin2", "billtoCustVendorId2", resultSummary);

            // 点击“毛利预估”
            WebElement grossProfitEstimate = ElementBase.findElement(By.xpath("//div[@class='voucher-menu-box']/span[3]/button"));
            grossProfitEstimate.click();
            Thread.sleep(4000);
            // 毛利为20元，整单毛利率20/170=11.75%，商品C是赠品，不算毛利
            WebElement amount = ElementBase.findElement(By.xpath("//div[@class='toolbar-right']/span/span"));
            resultSummary.append(comparePriceDouble(data.getString("amount"), amount.getText(), "整单毛利"));
            WebElement discount = ElementBase.findElement(By.xpath("//div[@class='toolbar-right']/span[2]/span"));
            resultSummary.append(comparePriceDouble(data.getString("discount"),discount.getText(),"整单毛利率"));
            // 验证商品A的毛利是100%，商品B的毛利是100%
            WebElement grossEstimateGrid = getGridCells("//div[@class='gross-margin-dialog']/div/div[2]/div/div/div/div/div[2]/div/div/div[2]/div/div",0);
            List<WebElement> cells = grossEstimateGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
            if(null == cells || 0 == cells.size()){
                return resultSummary.append("毛利预估查询结果为空! 失败!").toString();
            }
            Object detail2 = data.get("grossMarginPct");
            JSONArray jsonArray2 = JSONArray.parseArray(detail2.toString());
            int size = jsonArray2.size();
            for(WebElement cell : cells){
                if(cell.getAttribute("data-col-field").equalsIgnoreCase("grossMarginPct")){
                    size++;
                    if(!jsonArray2.contains(cell.getText())){
                        resultSummary.append("Error: 期望的毛利率不包含该值：" + cell.getText());
                    }
                }
            }
            if(2 != size){
                resultSummary.append("期望两条数据，但实际" + size +"条数据");
            }

            //验证完毕，无需保存，可直接退出
            //保存
            //save(GoodsIssueAbst.goodsIssueButtonAreaClass, GoodsIssueAbst.saveButton, 6000);
        } catch (Exception ex){
            resultSummary.append(ex);
        }
        return resultSummary.toString();
    }

    /*
        设置用户，检查用户带出的业务员、结算单位、分管人员、分管部门
     */
    public static void setCustomerCheckEmployeeDepartAdminBillToCustId(JSONObject data, String customerKey,
                                                                       String employeeKey, String departKey,
                                                                       String adminKey, String billtoCustVendorIdKey,
                                                                       StringBuilder resultSummary) throws InterruptedException {
        //输入客户
        WebElement customer = ElementBase.findElement(By.xpath(SaleOrderAbst.customer), 100, false);
        customer.clear();
        customer.sendKeys(Keys.BACK_SPACE);
        customer.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        customer.sendKeys(Keys.DELETE);
        Thread.sleep(1000);
        customer.sendKeys(data.getString(customerKey));
        Thread.sleep(2000);
        customer.sendKeys(Keys.SPACE);
        Thread.sleep(1000);

        //验证弹出提示，切换客户会重新取价
        try{
            WebElement ensureButton = ElementBase.findElement(By.xpath(SaleOrderAbst.cancleBtn), 10, false);
            if(null != ensureButton){
                ensureButton.click();
                Thread.sleep(3000);
            }
        } catch (Exception ex){

        }

        //检查业务员
        WebElement employee = ElementBase.findElement(By.xpath("//*[@id=\"react-select-4--value\"]/div/input"), 100, false);
        employee.click();
        if(!employee.getAttribute("value").equalsIgnoreCase(data.getString(employeeKey))){
            resultSummary.append("业务员没有带出正确！期望带出：" + data.getString(employeeKey) + " ;实际带出：" + employee.getText());
        }
        //检查分管部门
        WebElement department = ElementBase.findElement(By.xpath("//*[@id=\"react-select-5--value\"]/div/input"), 100, false);
        if(!department.getAttribute("value").equalsIgnoreCase(data.getString(departKey))){
            resultSummary.append("分管部门没有带出正确！期望带出：" + data.getString(departKey) + " ;实际带出：" + department.getText());
        }
        //分管人员
        WebElement admin = ElementBase.findElement(By.xpath("//*[@id=\"react-select-6--value\"]/div/input"), 100, false);
        if(!admin.getAttribute("value").equalsIgnoreCase(data.getString(adminKey))){
            resultSummary.append("分管人员没有带出正确！期望带出：" + data.getString(adminKey) + " ;实际带出：" + admin.getText());
        }
        //检查结算客户
        WebElement billtoCustVendorId = ElementBase.findElement(By.xpath("//*[@id=\"react-select-7--value\"]/div/input"), 100, false);
        if(!billtoCustVendorId.getAttribute("value").equalsIgnoreCase(data.getString(billtoCustVendorIdKey))){
            resultSummary.append("结算客户没有带出正确！期望带出：" + data.getString(billtoCustVendorIdKey) + " ;实际带出：" + billtoCustVendorId.getText());
        }
    }
}
