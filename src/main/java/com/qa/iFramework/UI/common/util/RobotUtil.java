package com.qa.iFramework.UI.common.util;

import java.awt.*;
import java.awt.event.KeyEvent;

public class RobotUtil {
	
	public static Robot robot =null;
	
	static{
		if(robot==null){
			   try {   
				    robot = new Robot();
				   } catch (AWTException e){
				    e.printStackTrace();   
				   }
			}
	}
	
	public static Robot getRobot(){
		if(robot==null){
		   try {   
			    robot = new Robot();
			   } catch (AWTException e){
			    e.printStackTrace();   
			   }
		}
		return robot;
	}
	
	public static void keypress(String str) throws InterruptedException {
        char[] s = str.toCharArray();
		for(char c:s){
			int k = (int)(c);
			if(c==':'){
				int[] j = {KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON};
				keypress(j);
			}
			else{
                //To Do: Haijia update since no JDK 7.0 locally
				//keypress(KeyEvent.getExtendedKeyCodeForChar(k));
                keypress(KeyEvent.getModifiersExText(k));
			}
		}
	}
	
	public static void keypressEnter(){
		keypress(KeyEvent.VK_ENTER);
	}
	
	public static void keypressShift(){
		keypress(KeyEvent.VK_SHIFT);
	}
	
	public static void keypress(int k){
		robot.keyPress(k);
		robot.keyRelease(k);
	}
	
	public static void keypress(int[] k){
		for(int i:k){
			robot.keyPress(i);
		}
		for(int i:k){
			robot.keyRelease(i);
		}
	}
	
	public static void mouseLeftPress(){
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
	}
	
	public static void mouseLeftPress(int x,int y){
		robot.mouseMove(x, y);
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
	}
	
	public static void mouseMove(int x,int y){
		robot.mouseMove(x, y);
	}
	
	/** 可以弹出QQ */  
	public void keyBoardDemo(){   
	   robot.keyPress(KeyEvent.VK_ALT);
	   robot.keyPress(KeyEvent.VK_CONTROL);
	   robot.keyPress(KeyEvent.VK_Z);
	   robot.keyRelease(KeyEvent.VK_Z);
	   robot.keyRelease(KeyEvent.VK_CONTROL);
	   robot.keyRelease(KeyEvent.VK_ALT);
	}   
	/** 前提是有个最大化的窗口，功能是移动到标题栏，然后拖拽到600,600的位置*/  
	public void mouseDemo(){   
	   robot.mouseMove(80, 10);   
	   robot.mousePress(KeyEvent.BUTTON1_MASK);
	   try {   
	    Thread.sleep(20);
	   } catch (InterruptedException e){
	    e.printStackTrace();   
	   }   
	   robot.mouseMove(600, 600);   
	   robot.mouseRelease(KeyEvent.BUTTON1_MASK);
	}   
	/** *//**  
	   * @param args  
	 * @throws InterruptedException
	   */  
	public static void main(String[] args) throws InterruptedException {
//	   RobotUtil demo=new RobotUtil();   
//	   demo.keyBoardDemo();   
//	   demo.mouseDemo();   
		RobotUtil.getRobot();
		//RobotUtil.keypress("D:\\workspace\\failed-method.xml");
		//robot.keyPress((119w));
		
		System.setProperty("template.path", "/Users/haijiahou/temp/");
		System.out.println(System.getProperty("template.path"));

	}   

}
