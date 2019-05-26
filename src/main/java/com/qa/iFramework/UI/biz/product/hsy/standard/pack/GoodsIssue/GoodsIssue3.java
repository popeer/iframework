package com.qa.iFramework.UI.biz.product.hsy.standard.pack.GoodsIssue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.SaleOrderAbst;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder.SaleOrderPack.save;

public class GoodsIssue3 extends GoodsIssueBasePack {
        public GoodsIssue3(String prefix){
            super(prefix);
        }

    /*
        case2：销货单选单流转销售订单
        前提条件：
        0. 创建商品分类A
        1. 在商品分类A下创建三个商品A，B，C
        2. A、B库存期初20，商品C的库存期初为0
        3. 创建客户K
        4. 创建仓库Cang
        5. 系统设置，销售订单加审核环节。
        6. 创建销售订单1，商品AB，数量为10，报价为100，未审核
        7. 创建销售订单2，商品C，数量为10，报价为100，并审核

        执行步骤：
        1. 新建销货单，选择客户K，选单
        2. 点击选单
        3. 查询，检查结果是否为0条
        4. 去掉勾选“库存大于0”
        5. 检查订单2是否出现在单据表头列表里结果表里。期望出现。
        6. 用接口审核通过销货订单1。
        7. 用接口给商品C增加库存20个
        8. 选单
        9. 点击查询
        10. 查看单据表头列表里销售订单1和2是否出现在结果表里。期望出现。
     */
    public String goodsIssueToSaleOrder(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try{
            Browser.driver.get(goodsIssueAbst.getPageUrl());
            System.out.println("打开销货单成功"  + df.format(new Date()));
            Thread.sleep(10000);
            setSaveButtonLocalStorage(data, "goods");

            //设置客户、仓库、单据类型
            preStepCreateGoodsIssue(data);
            //选单
            selectOrder();
            //查询
            WebElement topSearchBtn = ElementBase.findElement(By.xpath("//div[@class='tosearch']"));
            topSearchBtn.click();
            Thread.sleep(4000);
            //检查结果是否为0条
            WebElement gridHeader = ElementBase.findElement(By.xpath("//div[@class='voucher-select-master-grid']/div/div/div/div/div[2]/div/div/div[2]/div"));
            //当前grid元素下没有任何子元素即为0条记录
            List<WebElement> cells = ElementBase.FindElements(gridHeader, By.xpath("./div"), 1);
            //检查结果是否为0条
            if(null != cells && 0 < cells.size()){
                resultSummary.append(";不应该查询到销售订单");
            }

            //去掉勾选“库存大于0”,默认是勾选
            WebElement stockOver = ElementBase.findElement(By.xpath("//div[@class='voucher-select-master-bar-customed']/span/div/div/span/input"));
            stockOver.click();
            Thread.sleep(2000);
            //检查订单2是否出现在单据表头列表里结果表里。期望出现。
            cells = search(data);
            Thread.sleep(2000);
            boolean isArchived = false;
            for(WebElement we : cells){
                String fieldName = we.getAttribute("data-col-field");
                if(data.containsKey(fieldName)){
                    if(data.getString(fieldName).equalsIgnoreCase(we.getText())){
                        isArchived = true;
                        break;
                    }
                }
            }

            if(!isArchived){
                resultSummary.append(";没有找到期望的订单2");
            }
            //完成退出.do nothing here.

        } catch (Exception ex){
            resultSummary.append(ex);
        }
        return resultSummary.toString();
    }

    /*
     11. 选择销售订单1，查看单据明细表里是否出现商品A、B。期望出现。
//        12. 点击选单页面的取消按钮，再点选单。
//        13. 查询，检查结果是否含有销售订单1和2
        14. 选择订单1和2，勾选商品A、C
        15. 点击确认按钮
        16. 销货单明细表里，把商品A的数量数量改为20，商品C数量为5,
        17. 保存，审核
        18. 打开销售订单执行表，在第一个TAB页点击“展开更多条件”，在商品分类里输入新增的商品分类，
        19. 点击查询
        21. 验证A的执行比例为200%，未执行数量=-10
        22. 验证C的执行比例为50%，未执行数量=5
     */
    public String goodsIssueToSaleOrder2(JSONObject data){
        StringBuilder resultSummary = new StringBuilder();
        try {
            //选单
           List<WebElement> cells = search(data);
            //查看单据表头列表里销售订单1和2是否出现在结果表里。期望出现。
            WebElement gridHeader = ElementBase.findElement(By.className("voucher-header"), 10, true);
            JSONArray headerDetails = data.getJSONArray("headerDetail");
            JSONObject templateGridRowKey = (JSONObject) headerDetails.get(0);
            List<JSONObject> rows = new ArrayList<>();
            int rowIndex = 0;
            JSONObject currentRow = new JSONObject();
            rows.add(currentRow);
            for(WebElement we : cells){
                String fieldName = we.getAttribute("data-col-field");
                String dataRowIndex = we.getAttribute("data-row-index");
                if(Integer.valueOf(dataRowIndex) > rowIndex){
                    JSONObject newRow = new JSONObject();
                    rows.add(newRow);
                    rowIndex++;
                }

                if(templateGridRowKey.containsKey(fieldName)){
                    if(null == we || StringUtils.isEmptyOrSpace(we.getText())){
                        resultSummary.append(";" + fieldName + "字段内容为空");
                        continue;
                    }
                    rows.get(rows.size() - 1).put(fieldName, we.getText());
                }
            }

            for(JSONObject json : rows){
                if(!headerDetails.contains(json)){
                    resultSummary.append(";" + json.toJSONString() + " 和期望值不匹配");
                }
            }

            //选择订单1和2，勾选商品A、C
            cells.get(cells.size() - 1).click();
            Thread.sleep(1000);
            cells.get(1).click();
            Thread.sleep(1000);

            WebElement gridDetail = ElementBase.findElement(By.xpath("//div[@class='voucher-select-detail-grid']/div/div/div/div/div[2]/div/div/div[2]/div/div"));
            List<WebElement> gridDetails = gridDetail.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
            for(int i = 0; i <gridDetails.size(); i++){
                if(gridDetails.get(i).getAttribute("data-col-field").equalsIgnoreCase("productId.Name") ){
                    if(gridDetails.get(i).getText().equals(data.getString("unSelectProduct"))){
                        gridDetails.get(i).click();
                        break;
                    }
                }
            }

            //点击确认按钮
            WebElement conformBtn = ElementBase.findElement(By.className("SalesOrder-voucherSelect"));
            conformBtn.click();
            Thread.sleep(4000);

            //销货单明细表里，把商品A的数量数量改为20，商品C数量为5
//            List<WebElement> grids = Browser.driver.findElements(By.xpath(GoodsIssueAbst.detailGrid));
//            if(null == grids && 0 == grids.size()){
//                resultSummary.append("没有找到选单后的销货单的表单明细");
//            }
//            WebElement detailGrid = grids.get(0);
//            List<WebElement> details = detailGrid.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
//            Thread.sleep(1000);
//            for(WebElement we : details){
//                String fieldName = we.getAttribute("data-col-field");
//                if (fieldName.toLowerCase().trim().equals("productid")) {
//
//                }
//                if (fieldName.toLowerCase().trim().equals("transQty")) {
//                    we.click();
//                    Thread.sleep(1000);
//                    we.sendKeys();
//                }
//            }
            //保存，审核
            GoodsIssuePack.clickGoodIssue(data, false);
            //点击审核按钮
            save(SaleOrderAbst.goodsIssueButtonAreaClass, SaleOrderAbst.reviewBtn, 2000);
            WebElement ensureButton = ElementBase.findElement(By.xpath(SaleOrderAbst.ensureBtn), 10, false);
            ensureButton.click();
            Thread.sleep(3000);
            //完成退出
            // 关闭页签
            WebElement tabCloseBtn = ElementBase.findElement(By.xpath(SaleOrderAbst.tabCloseBtn), 10, false);
            tabCloseBtn.click();
            Thread.sleep(1000);
        } catch (Exception ex){
            resultSummary.append(ex);
        }

        return resultSummary.toString();
    }


}
