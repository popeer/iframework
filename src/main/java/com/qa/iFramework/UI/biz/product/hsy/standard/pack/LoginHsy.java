package com.qa.iFramework.UI.biz.product.hsy.standard.pack;

import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.UI.biz.product.hsy.standard.abst.LoginAbst;
import com.qa.iFramework.UI.common.Elements.Browser;
import com.qa.iFramework.UI.common.Elements.ElementBase;
import com.qa.iFramework.common.Util.StringUtils;
import org.openqa.selenium.By;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LoginHsy {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    public LoginHsy(){

    }

    public LoginHsy(String abc){

    }

    public Boolean login(Map<String, String> map) throws Exception{
        Browser.driver.get("http://www.chanjet.com/");
        Thread.sleep(3000);
        ElementBase.findElement(By.id(LoginAbst.navLoginBtn), 10, false).click();
        Thread.sleep(1000);
        ElementBase.findElement(By.id(LoginAbst.loginNameInput), 10, false).click();
        ElementBase.findElement(By.id(LoginAbst.loginNameInput), 10, false).sendKeys(map.get("uid"));
        ElementBase.findElement(By.id(LoginAbst.loginPwdInput), 10, false).click();
        ElementBase.findElement(By.id(LoginAbst.loginPwdInput), 10, false).sendKeys(map.get("pwd"));
        ElementBase.findElement(By.id(LoginAbst.confirmLoginBtn), 10, false).click();
        Thread.sleep(2000);
        System.out.println("login is successful" + df.format(new Date()));
        return true;
    }

    public Boolean login2(String domain, String username, String password) throws Exception{
        if(StringUtils.isEmptyOrSpace(domain)){
            return false;
        }

        if(domain.contains("inte-")){
            domain = "http://inte-www.qa.com";
        } else if (domain.contains("test-")){
            domain = "http://test-www.qa.com";
        } else {
            domain = "http://www.qa.com";
        }
        Browser.driver.get(domain);
        Thread.sleep(3000);
        ElementBase.findElement(By.id(LoginAbst.navLoginBtn), 10, false).click();
        Thread.sleep(1000);
        ElementBase.findElement(By.id(LoginAbst.loginNameInput), 10, false).click();
        ElementBase.findElement(By.id(LoginAbst.loginNameInput), 10, false).sendKeys(username);
        ElementBase.findElement(By.id(LoginAbst.loginPwdInput), 10, false).click();
        ElementBase.findElement(By.id(LoginAbst.loginPwdInput), 10, false).sendKeys(password);
        ElementBase.findElement(By.id(LoginAbst.confirmLoginBtn), 10, false).click();
        Thread.sleep(2000);
        System.out.println("login is successful" + df.format(new Date()));
        return true;
    }

    public JSONObject summary(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deptId", "faked");
        return jsonObject;
    }

}
