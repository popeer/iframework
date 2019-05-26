package com.qa.iFramework.UI.biz.product.hsy.standard.pack.SaleOrder;

import com.qa.iFramework.UI.biz.product.hsy.standard.abst.SaleOrderAbst;
import com.qa.iFramework.UI.biz.product.hsy.standard.pack.BasePack;
import com.qa.iFramework.common.Util.DateUtil;

public class SaleOrderBasePack extends BasePack {
    public SaleOrderAbst saleOrderAbst = new SaleOrderAbst();
    public SaleOrderBasePack(){

    }
    public SaleOrderBasePack(String prefix){
        String url = prefix +
                SaleOrderAbst.prefixURL +
                DateUtil.getCurrentStamp() +
                SaleOrderAbst.postfixURL;
        saleOrderAbst.setPageUrl(url);
    }
}
