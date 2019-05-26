package com.qa.iFramework.UI.common.Elements;

//import com.jprotractor.NgWebElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ElementBase {
    private static Logger log = LogManager.getLogger(ElementBase.class);
    public ElementBase()
    {
        //TODO
    }

    /// <summary>
    /// 实现查找一个控件的机制，不用等待直接返回。
    /// </summary>
    /// <param name="by"></param>
    /// <returns></returns>
//    public static NgWebElement FindElementNoWait(By by)
//    {
//        NgWebElement element = null;
//        try
//        {
//            Browser.ngDriver.manage().Timeouts().ImplicitWait = new TimeSpan(0, 0, 1);
//            element = Browser.ngDriver.findElement(by);
//            return element;
//        }
//        catch
//        {
//            return element;
//        }
//            finally
//        {
//            Browser.ngDriver.manage().Timeouts().ImplicitWait = new TimeSpan(0, 0, 10);
//        }
//    }

    /// <summary>
    /// 实现查找多个控件的机制，不用等待直接返回。
    /// </summary>
    /// <param name="by"></param>
    /// <returns></returns>
//    public static List<NgWebElement> FindElementsNoWait(By by)
//    {
//        List<NgWebElement> elementList = null;
//        try
//        {
//            Browser.ngDriver.manage().Timeouts().ImplicitWait = new TimeSpan(0, 0, 1);
//            elementList = Browser.ngDriver.findElements(by);
//            return elementList;
//        }
//        catch
//        {
//            return elementList;
//        }
//            finally
//        {
//            Browser.ngDriver.manage().Timeouts().ImplicitWait = new TimeSpan(0, 0, 10);
//        }
//    }

    public static WebElement findElement(By by){
        return findElement(by, 10, false);
    }

    /// <summary>
    /// 实现查找单个控件的重试和等待机制，10000超时。
    /// </summary>
    /// <param name="by"></param>
    /// <returns></returns>
    public static WebElement findElement(By by, int count, boolean IgnoreEnableAndDisplay)
    {
        if(0 == count){
            count = 10;
        }
        WebElement control = null;
        for (int i = 1; i <= count; i++)
        {
            try
            {
                Thread.sleep(500);
                if (i % 2 != 0)
                {
                    control = Browser.driver.findElement(by);
                }
                else
                {
//                    Browser.ngDriver.ignoreSynchronization = true;
                    control = Browser.driver.findElement(by);
//                    Browser.ngDriver.ignoreSynchronization = false;
                }

                if (IgnoreEnableAndDisplay && control != null)
                {
                    break;
                }
                if (control.isEnabled() && control.isDisplayed())
                {
                    break;
                }
            }
            catch(Exception ex)
            {

            }
        }
        return control;
    }

    public static List<WebElement> FindElements(WebElement webElement, By by, int count) throws Exception{
        List<WebElement> result = null;
        if(0 == count){
            count = 10;
        }
        for(int i = 0; i < count; i++){
            Thread.sleep(500);
            try{
                result = webElement.findElements(by);
                break;
            } catch (Exception ex){
                log.error(ex);
            }
        }
        return result;
    }

    /// <summary>
    /// 实现查找多个控件的重试和等待机制，10000超时。
    /// </summary>
    /// <param name="by"></param>
    /// <returns></returns>
    public static List<WebElement> FindElements(By by, int count) throws Exception {
        if(0 == count){
            count = 10;
        }
        //Thread.sleep(500);
        List<WebElement> controlList = null;
        for (int i = 0; i < count; i++)
        {
            try {
                Thread.sleep(500);
                controlList = Browser.driver.findElements(by);
                break;
            }
            catch(Exception ex)
            {
                log.error(ex);
            }
        }
        return controlList;
    }

    /// <summary>
    /// 判断指定的控件是否出现
    /// </summary>
    /// <param name="by"></param>
    /// <returns></returns>
//    public static boolean isElementPresent(By by)
//    {
//        try
//        {
//            Browser.ngDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//            NgWebElement element = Browser.ngDriver.findElement(by);
//            if (element != null)
//            {
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        catch(Exception ex)
//        {
//            return false;
//        }
//        finally
//        {
//            Browser.ngDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//        }
//    }

    /// <summary>
    /// 在指定的Container下 查找某个控件
    /// </summary>
    /// <param name="ContainerBy"></param>
    /// <param name="SubBy"></param>
    /// <param name="count"></param>
    /// <returns></returns>
//    public static NgWebElement FindElementInContainer(By ContainerBy, By SubBy, int count = 10) throws Exception
//    {
//        NgWebElement control = null;
//        for (int i = 0; i < count; i++)
//        {
//            try
//            {
//                control = Browser.ngDriver.findElement(ContainerBy).findElement(SubBy);
//                if (control != null)
//                {
//                    break;
//                }
//            }
//            catch (Exception e)
//            {
//                i++;
//                Thread.sleep(500);
//            }
//        }
//        return control;
//    }

    /// <summary>
    /// 在指定的Container下查找符合条件的一组控件
    /// </summary>
    /// <param name="ContainerBy"></param>
    /// <param name="SubRepeaterBy"></param>
    /// <param name="count"></param>
    /// <returns></returns>
//    public static List<NgWebElement> FindElementsInContainer(By ContainerBy, By SubRepeaterBy, int count = 10) throws Exception
//    {
//        List<NgWebElement> controlList = null;
//        for (int i = 0; i < count; i++)
//        {
//            try
//            {
//                controlList = Browser.ngDriver.findElement(ContainerBy).FindElements(SubRepeaterBy);
//                break;
//            }
//            catch
//            {
//                i++;
//                Thread.sleep(500);
//            }
//        }
//        return controlList;
//    }
}
