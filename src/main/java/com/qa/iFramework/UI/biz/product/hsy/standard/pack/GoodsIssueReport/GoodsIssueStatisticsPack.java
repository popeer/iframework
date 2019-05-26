package com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssueReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.GoodsIssueReportAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue.GoodsIssueBasePack;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.List;

public class GoodsIssueStatisticsPack extends GoodsIssueReportBasePack {
    public GoodsIssueStatisticsPack(String prefix){
        String url = prefix + "/index.html#/" + GoodsIssueReportAbst.postfixURL;
        goodsIssueReportAbst.setPageUrl(url);
    }

    /*
    销货单统计页
    前置条件：1：使用新建的5个商品分别创建2个销货单，一个含有2个商品，一个含有另外3个商品
              2：新建2个往来单位
    步骤一：打开销货单统计页，选择第一个TAB项即商品，选择当前月，录入第一个往来单位，查询
    步骤二：查询到两个商品的销货单记录，检查成交金额、赠品金额、毛利、毛利率
    步骤三：清空往来单位，录入第二个往来单位，查询
    步骤四：查询到三个商品的销货单记录，检查成交金额、赠品金额、毛利、毛利率。完毕。
     */
    public String searchInStatisstatics(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(goodsIssueReportAbst.getPageUrl());
            System.out.println("打开销货单统计页成功"  + df.format(new Date()));
            Thread.sleep(10000);

            WebElement tabProduct = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.firstTab), 10, false);
            tabProduct.click();
            Thread.sleep(1000);

            WebElement currentMonth = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.currentMonth), 10, false);
            currentMonth.click();
            Thread.sleep(1000);

            resultSummary.append(searchGoodsIssueByProduct(data, "customer1"));
            resultSummary.append(searchGoodsIssueByProduct(data, "customer2"));
            resultSummary.append("销货单统计页面searchInStatisstatics is finished");
            return resultSummary.toString();

        } catch (Exception ex){
            resultSummary.append(ex).append('\n');
            return resultSummary.toString();
        }
    }

    private String searchGoodsIssueByProduct(JSONObject data,String customerKey) {
        StringBuilder resultSummary = new StringBuilder();
        try {
            WebElement customer = ElementBase.findElement(By.xpath("//*[@class='filter-fields-container']/div/div/div[3]/div/div/div[2]/div/div/div/span[1]/div/input"), 10, false);
            customer.clear();
            customer.sendKeys(Keys.BACK_SPACE);
            customer.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            customer.sendKeys(Keys.DELETE);
            Thread.sleep(1000);
            customer.sendKeys(data.getString(customerKey));
            Thread.sleep(2000);
            customer.sendKeys(Keys.SPACE);

            WebElement moreCondition = ElementBase.findElement(By.xpath("//*[@class='filter-switch-expand']"), 10, false);
            moreCondition.click();
            Thread.sleep(1000);

            WebElement searchBtn = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.searchBtn), 10, false);
            searchBtn.click();
            Thread.sleep(10000);

            WebElement detailGrid = getGridCells(GoodsIssueAbst.detailGrid,0);
            List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
            JSONArray details = data.getJSONArray(GoodsIssueAbst.detailDataKey);

            for (Object element : details) {
                int index = -1;
                JSONObject row = (JSONObject) element;
                for (WebElement we : cells) {
                    String dataRowIndex = we.getAttribute("data-row-index");
                    if (Integer.parseInt(dataRowIndex) == index) {
                        continue;
                    }
                    String fieldName = we.getAttribute("data-col-field");

                    if (null == we || null == we.getText() || null == fieldName) {
                        continue;
                    }

                    if (fieldName.endsWith(".name") && !row.getString("name").equalsIgnoreCase(we.getText())) {
                        index = Integer.parseInt(dataRowIndex);
                        continue;
                    }

                    if (row.containsKey(fieldName)) {
                        resultSummary.append(GoodsIssueBasePack.comparePriceDouble(row.getString(fieldName), we.getText(), fieldName, " Detail " + String.valueOf(details.indexOf(element))));
                    }
                }
            }

        } catch (Exception ex) {
            resultSummary.append(ex);
            return resultSummary.toString();
        }
        return resultSummary.toString();
    }

    /*
    验证销货单统计页新增方案并查询
    前置条件：新建三个商品的销货单
    步骤一：点击+号，在新开页面点击“选择分组维度”，选择仓库
    步骤二：点击添加维度，选择商品
    步骤三：点击查询
    步骤四：录入客户1，点击查询
    步骤五：查看结果表里是否有仓库A+商品1、仓库A+商品2
    步骤六：录入客户2，点击查询
    步骤七：查看结果表里是否有仓库B+商品3、仓库B+商品4、仓库B+商品5。完成。
     */
    public String saveDeteleStatisstatics(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(goodsIssueReportAbst.getPageUrl());
            System.out.println("打开销货单统计页成功"  + df.format(new Date()));
            Thread.sleep(10000);

            WebElement tabNewSolution = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.newAddTab), 10, false);
            tabNewSolution.click();
            Thread.sleep(1000);

            WebElement addContaioner = ElementBase.findElement(By.xpath("//*[@class='row-dim-add-container']/div/div/div"), 10, false);
            addContaioner.click();
            Thread.sleep(3000);

            WebElement addWareHourse = ElementBase.findElement(By.xpath("//*[@class='fields-container']/div[3]/div[2]/div[1]"), 10, false);
            addWareHourse.click();
            Thread.sleep(2000);

            WebElement dims = ElementBase.findElement(By.xpath("//*[@class='scheme-dims']/div[2]/div[3]/div/button"), 10, false);
            dims.click();
            Thread.sleep(2000);

            WebElement addProduct = ElementBase.findElement(By.xpath("//*[@class='fields-container']/div/div[2]/div[1]"), 10, false);
            addProduct.click();
            Thread.sleep(2000);

//            WebElement saveSolution = ElementBase.findElement(By.xpath("//*[@class='scheme-name-container']/div/div/span/input"), 10, false);
//            saveSolution.click();
//            Thread.sleep(2000);

            WebElement searchBySolution = ElementBase.findElement(By.xpath("//*[@class='buttons-container']/div/button[2]"), 10, false);
            searchBySolution.click();
            Thread.sleep(4000);

            searchByHouseProductCustomerInGoodsIssueStatistatics(data, resultSummary, "customer1", 2);
            searchByHouseProductCustomerInGoodsIssueStatistatics(data, resultSummary, "customer2", 3);

//            WebElement closeBtn = ElementBase.findElement(By.xpath("//*[@class='TabLabelCloseButton']/span/svg"), 10, false);
//            closeBtn.click();
//            Thread.sleep(2000);

//            Browser.driver.get(goodsIssueReportAbst.getPageUrl());
//            System.out.println("再次打开销货单统计页成功"  + df.format(new Date()));
//            Thread.sleep(10000);

//            WebElement tabfirstSolution = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.firstTab), 10, false);
//            if(!tabfirstSolution.getAttribute("title").equalsIgnoreCase("按仓库+商品")){
//                resultSummary.append("新开保存的方案打开验证失败");
//            }
            resultSummary.append("查询方案完成");

//            WebElement tabSecondSolution = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.secondTab), 10, false);
//            tabSecondSolution.click();
//            Thread.sleep(2000);

//            WebElement tabSetting = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.settingTab), 10, false);
//            tabSetting.click();
//            Thread.sleep(2000);

//            WebElement deleteSolution = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.deleteBtn), 10, false);
//            deleteSolution.click();
//            Thread.sleep(2000);

        } catch (Exception ex){
            resultSummary.append(ex);
            return resultSummary.toString();
        }
        return  resultSummary.toString();
    }

    public void searchByHouseProductCustomerInGoodsIssueStatistatics(JSONObject data, StringBuilder resultSummary, String customerKey, int number) throws InterruptedException {
        WebElement customer = ElementBase.findElement(By.xpath("//*[@class='filter-fields-container']/div/div/div[3]/div/div/div[2]/div/div/div/span[1]/div/input"), 10, false);
        customer.clear();
        customer.sendKeys(Keys.BACK_SPACE);
        customer.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        customer.sendKeys(Keys.DELETE);
        Thread.sleep(1000);
        customer.sendKeys(data.getString(customerKey));
        Thread.sleep(2000);
        customer.sendKeys(Keys.SPACE);

        //TO DO LIST:注意，class后面多一个空格
        WebElement date = ElementBase.findElement(By.xpath("//*[@class='filter-convenient-date ']/li[2]"));
        date.click();
        Thread.sleep(1000);

        WebElement searchBtn = ElementBase.findElement(By.xpath(GoodsIssueReportAbst.searchBtn), 10, false);
        searchBtn.click();
        Thread.sleep(5000);

        checkGridData(data, resultSummary, number);
    }

    public static void checkGridData(JSONObject data, StringBuilder resultSummary, int number) {
        WebElement detailGrid = getGridCells(GoodsIssueAbst.detailGrid,0);
        List<WebElement> cells = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
        if(null == cells || 0 == cells.size()){
            resultSummary.append("新增查询方案查询结果为空! 失败!");
            return ;
        }
        JSONArray details = data.getJSONArray(GoodsIssueAbst.detailDataKey);
        int counter = 0;
        for(Object element : details){
            JSONObject row = (JSONObject) element;
            for(WebElement we : cells){
                String dataRowIndex = we.getAttribute("data-row-index");
                String fieldName = we.getAttribute("data-col-field");
                if (null == we || null == we.getText() || null == fieldName) {
                    continue;
                }
                if (!fieldName.endsWith(".name")) {
                    continue;
                }

                if(row.getString("product").equalsIgnoreCase(we.getText())){
                    resultSummary.append(we.getText() + "匹配成功");
                    counter++;
                }
            }
        }

        if(number != counter){
            resultSummary.append("通过客户" + data.getString("customer1") + "查看匹配仓库商品失败");
        }
    }


}
