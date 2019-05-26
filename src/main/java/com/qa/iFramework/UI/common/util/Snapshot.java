package com.qa.iFramework.UI.common.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Snapshot {

	/**
	 * 功能概述：抽取字符串中的数字，返回数字字符串
	 * @param s
	 * @return
	 */
	public static void takesnap(WebDriver arg1) {
		
		PropertiesParse pp = new PropertiesParse();
		String filename = CommonUtil.generateRandomFilename()+".png";
		String filepath = pp.getPropertieValue("config/agent.properties", "jietuLujing") +filename;
		System.out.println(filepath);
		try {
		    File screenShotFile = ((TakesScreenshot)arg1).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenShotFile, new File( filepath));
		} catch (IOException e) {
			System.out.println("没有输出图片");
			e.printStackTrace();
		}
		
	}
	
	 public static void Capture(int x,int y,int height,int width){
			try {
				Robot robot = new Robot();
				// 根据指定的区域(100,100)抓取屏幕的指定区域 
				BufferedImage bi=robot.createScreenCapture(new Rectangle(x,y,height,width));
				//把抓取到的内容写入到一个jpg文件中
				ImageIO.write(bi, "png", new File("E:/bushu/pageimage/imageTest.png"));
				} catch (AWTException e) {e.printStackTrace();}
				  catch (IOException e) {e.printStackTrace();}
		
		}
	 
	 
	 public static void Capture(String Prefix){
		 PropertiesParse _PropertiesParse = new PropertiesParse();
			try {
				Robot robot = new Robot();
				// 根据指定的区域(100,100)抓取屏幕的指定区域 
				BufferedImage bi=robot.createScreenCapture(new Rectangle(0,0,1400,800));
				String filename = Prefix+CommonUtil.generateRandomFilename()+".png";
				String filepath = _PropertiesParse.getValue("jietuLujing") +filename;
				String lianjiePath = _PropertiesParse.getValue("tupianLianjieLujing")+filename;
				ImageIO.write(bi, "png", new File(filepath));
				Reporter.log("<a href='"+lianjiePath+"' target='_blank'> "+ lianjiePath+"</a><br>");
				} catch (AWTException e) {e.printStackTrace();}
				  catch (IOException e) {e.printStackTrace();}
		
		}
	 
	 public static void PressKey(String mykey){
			try {
				Robot robot = new Robot();
				if(mykey.equals("F5")){robot.keyPress(KeyEvent.VK_F5);}
				if(mykey.equals("F11")){robot.keyPress(KeyEvent.VK_F11);}
				} catch (AWTException e) {e.printStackTrace();}
		
		}
	 
	public static int[] getPosition(WebDriver driver){
		String xpathscript="function addXpathscript(){"+
				"var elscript = document.createElement('script');"+
				"elscript.src='http://svn.coderepos.org/share/lang/javascript/javascript-xpath/trunk/release/javascript-xpath-latest.js';"+
				"elscript.type='text/javascript';"+
				" document.getElementsByTagName('head')[0].appendChild(elscript);"+
				"};addXpathscript();"+
				"function getElemPos(obj){"+
				"var pos = {'top':0, 'left':0};"+
					"if (obj.offsetParent){"+
			          " while (obj.offsetParent){"+
			            " pos.top += obj.offsetTop;"+
			            " pos.left += obj.offsetLeft;"+
			            " obj = obj.offsetParent;"+
			          " }"+
			         "}else if(obj.x){"+
			         "  pos.left += obj.x;"+
			         "}else if(obj.x){"+
			           "pos.top += obj.y;"+
			         "}"+
			         " return {x:pos.left, y:pos.top};"+
			    "};"+
				"var elem=document.evaluate(\".//*[@id='main']\", document, null, 7, null).snapshotItem(0);"+
				"return getElemPos(elem).x+':'+getElemPos(elem).y+':'+elem.offsetWidth+':'+elem.offsetHeight;";
	
		String postr=(String)((JavascriptExecutor)driver).executeScript(xpathscript);//加载js-xpath
		//String postr="23:132:940:367";//返回值代表left，top，width，height
        int pos[]=new int [4];
		String aa[]=postr.split(":");
		for(int i=0;i<4;i++){
			pos[i]= Integer.parseInt(aa[i]);
		}
		/*for(int j=0;j<4;j++){
		   System.out.println(pos[j]);
		}*/
		return pos;
	}
	
	public static void main(String[] agrs){
		//System.out.println(CommonUtil.getCurrentDate("yyyy-MM-dd"));
		//System.out.println(CommonUtil.GetDoubleNums("sssssssss444444"));
		//Snapshot.PressKey("F11");
		
	}
	

}
