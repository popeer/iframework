package com.qa.iFramework.UI.biz.product.hsy.standard.pack;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.NumberUtil;
import com.qa.iFramework.common.Util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 2019/3/21.
 */
public class BasePack {
    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    //销货单选单操作
    public static void selectOrder() throws Exception{
        WebElement chooseBtn = ElementBase.findElement(By.xpath("//div[@class='voucher-menu-box']/button"));
        chooseBtn.click();
        WebElement subChooseBtn = ElementBase.findElement(By.xpath("//div[@class='voucher-menu-dropdown-item']/button"));
        subChooseBtn.click();
        Thread.sleep(3000);
    }

    //销货单选单页面里的查询操作
    public static List<WebElement> search(JSONObject data) throws Exception{
        //查询
        WebElement topSearchBtn = ElementBase.findElement(By.xpath("//div[@class='tosearch']"));
        topSearchBtn.click();
        Thread.sleep(4000);
        //检查结果是否为0条
        WebElement gridHeader = ElementBase.findElement(By.xpath("//div[@class='voucher-select-master-grid']/div/div/div/div/div[2]/div/div/div[2]/div/div"));
        List<WebElement> cells = gridHeader.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
        Thread.sleep(1000);
        return cells;
    }

    public static Map<String, String > setProductDetailGridElementLocation() {
        Map<String, String> elementLocation = new HashMap<>();
        elementLocation.put("voucher-body-head","voucher-body-head");
        elementLocation.put("voucher-title","voucher-title");
        elementLocation.put("productComboBtn","../../../span/button");
        elementLocation.put("productSearchInput","/html/body/div[2]/div/div[2]/div/div/div/div/div/div[1]/div[2]/div/div[1]/div[2]/div/div/div[1]/div[1]/div[2]/span/input");
        elementLocation.put("searchBtn","/html/body/div[2]/div/div[2]/div/div/div/div/div/div[1]/div[2]/div/div[1]/div[2]/div/div/div[1]/div[1]/div[2]/span/span/span/div/button");
        elementLocation.put("checkProduct","//*[@id=\"datagrid-title-btn-wrap\"]/div/div[1]/div/div[2]/div[1]/div[1]/div[1]/div/div[2]/div/div/div/div/span/input");
        elementLocation.put("confirmBtn","/html/body/div[2]/div/div[2]/div/div/div/div/div/div[1]/div[3]/button[2]");
        return elementLocation;
    }

    public static boolean inputProductDetailGridValue(JSONArray jsonArray, Map<String, String> elementLocation) throws Exception{
        //找到商品明细grid
        List<WebElement> grids = ElementBase.FindElements(By.xpath(GoodsIssueAbst.detailGrid), 20);
        if(null == grids && 0 == grids.size()){
            return false;
        }
        WebElement gridHeader = ElementBase.findElement(By.className(elementLocation.get("voucher-title")), 10, true);
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
                WebElement comboButton = combb.findElement(By.xpath(elementLocation.get("productComboBtn")));
                comboButton.click();
                Thread.sleep(4000);
                //搜索文本框
                System.out.println("搜索文本框");
                WebElement searchInput = detailGrid.findElement(By.xpath(elementLocation.get("productSearchInput")));
                //输入商品ID
                System.out.println("输入商品ID");
                searchInput.sendKeys(entry.getString("productId"));
                Thread.sleep(4000);
                //点击搜索按钮
                System.out.println("点击搜索按钮");
                detailGrid.findElement(By.xpath(elementLocation.get("searchBtn"))).click();
                Thread.sleep(4000);
                //勾选商品
                System.out.println("勾选商品");
                detailGrid.findElement(By.xpath(elementLocation.get("checkProduct"))).click();
                Thread.sleep(4000);
                //确认
                System.out.println("确认");
                detailGrid.findElement(By.xpath(elementLocation.get("confirmBtn"))).click();
                Thread.sleep(4000);
                gridHeader.click();
                Thread.sleep(1000);
                continue;
            }

            //勾选
            if (fieldName.trim().equals("isFreeGift")){
                Thread.sleep(1000);
                we.click();
                Thread.sleep(1000);
                WebElement webElement = Browser.driver.switchTo().activeElement();
                if(entry.getString(fieldName).equals("1")){
                    webElement.sendKeys(Keys.SPACE);
                }
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
        System.out.println("input done");

        return true;
    }

    /*
        设置新增销货单等单据的保存按钮为默认
     */
    public static void setSaveButtonLocalStorage(JSONObject data,String keyword) throws InterruptedException {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) Browser.driver;
        String saleOrderID = "";
        if(keyword.toLowerCase().trim().startsWith("goods")){
            saleOrderID = "voucherSaveButtonLableDefaultGoodsIssue" + data.getString("userID");
        }
        if(keyword.toLowerCase().trim().startsWith("sales")){
            saleOrderID = "voucherSaveButtonLableDefaultSalesOrder" + data.getString("userID");
        }


        jsExecutor.executeScript("localStorage.setItem('" + saleOrderID + "','保存')");
        Thread.sleep(2000);
        Browser.driver.navigate().refresh();
        Thread.sleep(5000);
        System.out.println("再次打开销售订单成功");
    }

    /*
        比对期望值(String类型)和实际值，并指明字段名，和描述
     */
    public static String comparePriceDouble(String expect, String actual, String fieldName, String... desc){
        if(StringUtils.isEmptyOrSpace(expect) && StringUtils.isEmptyOrSpace(actual)){
            return "";
        }
        if(StringUtils.isEmptyOrSpace(expect) || StringUtils.isEmptyOrSpace(actual)){
            return "Step " + getStepDesc(desc) + ", : FieldName : " + fieldName + " : Expect:" + expect + " or Actual: " + actual + " is EmptyOrSpace!";
        }
        if(!NumberUtil.isNumeric(expect)){
            return "Step " + getStepDesc(desc) + ", : FieldName : " + fieldName + ", Expect : " + expect + " is not numeric!";
        }

        if(!NumberUtil.isNumeric(actual)){
            return "Step " + getStepDesc(desc) + ", : FieldName : " + fieldName + ", : Actual: " + actual + " is not numeric!";
        }

        if(actual.contains("%")) {
            actual = actual.replace("%", "");
            expect = expect.replace("%", "");
        }
        actual = actual.replace(",","");
        expect = expect.replace(",","");

        //四舍五入得2位数字进行比较，避免系统设置小数位数导致对比不一致
        BigDecimal expectDecimal = new BigDecimal(expect);
        double a = expectDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal actualDecimal = new BigDecimal(actual);
        double b = actualDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if(0 != Double.compare(a, b)) {
            return "Step " + getStepDesc(desc) + ", FieldName :" + fieldName + ", Actually is " + actual + ", But its expected is " + expect + "!";
        }

        return "";
    }

    private static String getStepDesc(String[] desc){
        if(null == desc){
            return "";
        }

        if(1 == desc.length){
            return desc[0];
        }
        return "";
    }

    public static WebElement getGridCells(String xpath, int num){
        //找到商品明细grid
        List<WebElement> grids = Browser.driver.findElements(By.xpath(xpath));
        if(null == grids && 0 == grids.size()){
            return null;
        }
        //新建销货单功能，取第一个；第二个是推荐商品里的grid
        //若是其他场景，可以考虑传参方式来指定
        return grids.get(num);
    }
}
