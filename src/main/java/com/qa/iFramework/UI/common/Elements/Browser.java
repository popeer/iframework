package com.qa.iFramework.UI.common.Elements;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.qa.iFramework.common.processor.Driver.sysPro;

//import com.jprotractor.NgWebDriver;

public class Browser {
    public static WebDriver driver;

    public static RemoteWebDriver remoteWebDriver;

//    public static NgWebDriver ngDriver;

    public static void LaunchBrowser(String type, String url){
        switch (type.toLowerCase().trim()){
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
//                chromeOptions.addArguments("--headless");
//                driver = new ChromeDriver();
//                driver.manage().window().maximize();
//                driver.navigate().to(url);
//                ngDriver = new NgWebDriver(driver);
                try{
                    remoteWebDriver = new RemoteWebDriver(new URL(sysPro.getProperty("hub")), DesiredCapabilities.chrome());
                    driver = new Augmenter().augment(remoteWebDriver);
//                    getComputerNameOfNode(driver);
                    driver.manage().window().maximize();
                    driver.navigate().to(url);
//                    ngDriver = new NgWebDriver(driver);
                } catch (MalformedURLException ex){
                    ex.printStackTrace();
                }
                break;
                default:
                    break;

        }
    }

    public static void getComputerNameOfNode(WebDriver driver){
        String hub = "SZAUTOTEST1";
        int port = 4444;
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        String sessionUrl = "http://" + hub + ":" + port+ "/grid/api/testsession?session="+((RemoteWebDriver) driver).getSessionId();
        HttpPost httpPost = new HttpPost(sessionUrl);
        System.out.println("Http post request is : "+httpPost.getRequestLine());
        try{
            //Execute HTTP request
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            //Get HTTP response
            HttpEntity entity = httpResponse.getEntity();
            //Response status
            System.out.println("HTTP status:" + httpResponse.getStatusLine());
            //Check if response is null
            if (entity != null) {
                System.out.println("Content encoding:" + entity.getContentEncoding());
                String jsonString = EntityUtils.toString(entity);
                System.out.println("Response content:" + jsonString);
                JSONObject jsonObject = JSON.parseObject(jsonString);
                String proxyID=jsonObject.getString("proxyId");
                String node = (proxyID.split("//")[1].split(":")[0]);
                System.out.println("The case is running on this node :" + node);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                closeableHttpClient.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
