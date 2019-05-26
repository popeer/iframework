package com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.BasePack;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.DateUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by haijia on 2019/3/21.
 *      productId		商品
 *      data-row-index="1"
 *      netPriceWithoutTax  无税单价
 *      netPriceWithTax		单价
 *      netDiscount	折扣
 *      transQty	数量
 *      listPrice	报价
 *      taxPct	税率
 *      netAmountWithoutTax	无税金额
 *      netTax   税额
 *      netAmountWithTax   金额
 *      priceWithoutTax   优惠前无税单价
 *      priceWithTax    优惠前单价
 *      amountWithoutTax     优惠前无税金额
 *      amountWithTax   优惠前金额
 *      costPrice   成本单价
 *      costAmount   成本金额
 *      promoDiscount     优惠卷分摊
 *      extraDiscount  整单优惠
 *      vGrossMargin   毛利
 *      vGrossMarginPct  毛利率
 *      retailPrice  零售价
 */
public class GoodsIssueBasePack extends BasePack {
    protected GoodsIssueAbst goodsIssueAbst = new GoodsIssueAbst();

    public GoodsIssueBasePack(){

    }

    public GoodsIssueBasePack(String prefix){
        String url = prefix +
                GoodsIssueAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                GoodsIssueAbst.postfixURL;
        goodsIssueAbst.setPageUrl(url);
    }

    public static boolean inputProductDetailGridValue(JSONArray jsonArray) throws Exception{
        //找到商品明细grid
        List<WebElement> grids = ElementBase.FindElements(By.xpath(GoodsIssueAbst.detailGrid), 20);
        if(null == grids && 0 == grids.size()){
            return false;
        }
        WebElement gridHeader = ElementBase.findElement(By.className("voucher-header"), 10, true);
        //新建销货单功能，取第一个；第二个是推荐商品里的grid
        //若是其他场景，可以考虑传参方式来指定
        WebElement detailGrid = grids.get(0);

        List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
        Thread.sleep(1000);

        for(WebElement we : cells) {
            String fieldName = we.getAttribute("data-col-field");
            String dataRowIndex = we.getAttribute("data-row-index");
            if(jsonArray.size() <= Integer.parseInt(dataRowIndex)){
                break;
            }
            JSONObject entry = (JSONObject) jsonArray.get(Integer.parseInt(dataRowIndex));

            //录入商品
            if (fieldName.toLowerCase().trim().equals("productid")) {
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
                gridHeader.click();
                Thread.sleep(1000);
                continue;
            }

            if(entry.containsKey(fieldName)){
                Thread.sleep(1000);
                we.click();
                Thread.sleep(1000);
                WebElement webElement = Browser.driver.switchTo().activeElement();
                webElement.sendKeys(entry.getString(fieldName));
                webElement.sendKeys(Keys.ESCAPE);
                gridHeader.click();
                Thread.sleep(1000);
                continue;
            }
        }

        detailGrid.click();
        System.out.println("click done");

        return true;
    }

    public static void preStepCreateGoodsIssue(JSONObject data) throws InterruptedException {
        //输入客户
        WebElement customer = ElementBase.findElement(By.xpath(GoodsIssueAbst.customer), 10 ,false);
        customer.clear();
        customer.sendKeys(Keys.BACK_SPACE);
        customer.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        customer.sendKeys(Keys.DELETE);
        Thread.sleep(1000);
        customer.sendKeys(data.getString(GoodsIssueAbst.customerKey));
        Thread.sleep(2000);
        customer.sendKeys(Keys.SPACE);

        try{
            WebElement dailog = ElementBase.findElement(By.xpath("//*[@class='chanjet-nova-ui-base-dialog']/div[2]/div/div/div/div/div/div/div/button[1]"), 10, true);
            dailog.click();
            Thread.sleep(1000);
        } catch (Exception ex){
            System.out.println(ex);
        }

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
        WebElement billType = ElementBase.findElement(By.xpath(GoodsIssueAbst.billType), 10 ,false);
        billType.clear();
        billType.sendKeys(Keys.BACK_SPACE);
        billType.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        billType.sendKeys(Keys.DELETE);
        Thread.sleep(1000);
        billType.sendKeys("增值税普通发票");
        Thread.sleep(2000);
        billType.sendKeys(Keys.SPACE);
    }
}
