package com.qa.iFramework.UI.common.Elements;

import com.qa.iFramework.UI.common.util.PropertiesParse;
import org.openqa.selenium.WebDriver;

import java.util.Properties;

/**
 * 提供处理页面元素的父类
 */
public class Element {
    protected WebDriver driver;
    protected String explorerType;
    protected PropertiesParse _PropertiesParse = new PropertiesParse();
    protected Properties properties =  new Properties();

    public void setPropertisParsePath(String propertieFilepath){
        _PropertiesParse.setFilePath(propertieFilepath);
    }

    public void setProperties(Properties properties){
        this.properties = properties;
    }

    public void setExplorerType(String explorerType){
        this.explorerType = explorerType;
    }

    public void setDriver(WebDriver d){
        driver = d;
    }
}
