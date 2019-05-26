package com.qa.iFramework.UI.biz.product.hsy.standard.pack.Marketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.PriceExtractAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.BasePack;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue.GoodsIssueBasePack;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.DateUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;

public class PriceExtractSettingPack extends BasePack {
    protected PriceExtractAbst priceExtractAbst = new PriceExtractAbst();
    protected GoodsIssueAbst goodsIssueAbst = new GoodsIssueAbst();

    public PriceExtractSettingPack(String prefix){
//        String url = prefix + PriceExtractAbst.postfixURL;
//        priceExtractAbst.setPageUrl(url);
        String goodsIssueUrl = prefix +
                GoodsIssueAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                GoodsIssueAbst.postfixURL;
        goodsIssueAbst.setPageUrl(goodsIssueUrl);
    }

    /*
    前置条件：
        1. 创建往来单位1，定价方法勾选价格等级，客户等级价格为价格等级1；
        2  创建往来单位2.
        3. 创建仓库1.
        4.创建4个商品，
        商品A,价格都为0；
        商品B，等级1价格11，等级价格12；
        商品C 零售价60，其他价格为0，
        商品D, 基准批发价80。税率都是16%。
        2. 报价含税
        3. 创建一个销货单，使用往来单位1，包含商品A。报价100，数量1.
        4. 再创建一个销货单，使用往来单位2，包含商品D。报价100，数量1.

        UI自动化步骤：
        1.调整顺序为客户最近交易价、商品基准批发价，客户等级价或客户折扣、零售价，点击保存
        2. 新建销货单，往来单位是1，录入4个商品，数量1。
        3. 检查商品A的金额100、报价100、无税单价86.21，单价100（历史），无税金额86.21，，税额13.79
        4. 检查商品B的金额11、报价11、无税单价9.48，单价11（协议），无税金额9.48，税额1.52
        5. 检查商品C的金额60、报价60、无税单价51.72,单价60（零售），无税金额51.72，税额8.28
        6. 检查商品D的金额80、报价80、无税单价68.97，单价80（批发），无税金额68.97，税额11.03，
        7. 清空明细表格里所有商品，更换其他客户2，重新录入4个商品，
        8. 调整顺序为商品最新售价、客户最近交易价、商品基准批发价、客户等级价或商品折扣、零售价
        9. 不保存单据，直接关闭

        验证商品A  单价100（最新）
        商品B，单价11（由于更换往来单位，协议标签为空，报价为0）
        商品C 单价60（零售）
        商品D 单价100（最新）
     */
    public String recentTradingPrice(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(goodsIssueAbst.getPageUrl());
            System.out.println("打开售价提取策略成功"  + df.format(new Date()));
            Thread.sleep(10000);
            setSaveButtonLocalStorage(data, "goods");

            //设置客户、仓库、单据类型
            GoodsIssueBasePack.preStepCreateGoodsIssue(data);
            //取明细的期望数据
            Object detail = data.get(GoodsIssueAbst.detailDataKey);
            JSONArray jsonArray = JSONArray.parseArray(detail.toString());
            resultSummary.append(inputProductAndVerifyPriceList(jsonArray));
            resultSummary.append(" 售价提取策略1验证最近交易价提取用例执行完毕!");
            return resultSummary.toString();

        } catch (Exception ex){
            resultSummary.append(ex);
            return resultSummary.toString();
        }
    }

    protected static String inputProductAndVerifyPriceList(JSONArray jsonArray) throws Exception{
        StringBuilder resultSummary = new StringBuilder();
        //找到商品明细grid
        List<WebElement> grids = Browser.driver.findElements(By.xpath(GoodsIssueAbst.detailGrid));
        if(null == grids && 0 == grids.size()){
            return " 没有找到销货单明细Grid! ";
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
            //前置条件里用接口创建销货单时两个商品的数据是一样的，也为了避免先循环JsonArray再循环Cells造成的时间浪费
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

            //输入数量才会有金额、无税金额、税额，故需要输入数量。数量列在其他金额之前。
            if (fieldName.toLowerCase().trim().equals("transqty")){
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

            if(!entry.containsKey(fieldName)){
                continue;
            }

            //由于金额有标签，需要验证标签
            if (fieldName.toLowerCase().trim().equals("netpricewithtax")) {
                WebElement flag = null;
                try{
                    flag = we.findElement(By.xpath("./div/div/div/span[2]"));
                    //比较标签
                    if(!entry.getString("flag").equalsIgnoreCase(flag.getText())){
                        resultSummary.append("第" + dataRowIndex + "行,字段名为" + fieldName + "的标签期望是" +
                                entry.getString("flag") + " 但实际是" + flag.getText());
                    }
                    //比较数值
                    WebElement netPriceWithTax = we.findElement(By.xpath("./div/div/div/span[1]"));
                    resultSummary.append(comparePriceDouble(entry.getString(fieldName), netPriceWithTax.getText(), fieldName, dataRowIndex));
                } catch (Exception ex){
                    //当没有标签时候比较数值
                    flag = we.findElement(By.xpath("./div/div/div"));
                    resultSummary.append(comparePriceDouble(entry.getString(fieldName), flag.getText(), fieldName, dataRowIndex));
                }

                continue;
            }
            resultSummary.append(comparePriceDouble(entry.getString(fieldName), we.getText(), fieldName, dataRowIndex));
        }

        detailGrid.click();
        return resultSummary.toString();
    }
}
