package com.qa.iFramework.UI.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 封装解析property的函数
 * @author houhaijia
 *
 */
public class PropertiesParse {
	
	private String propertieFilepath;
	
	public void setFilePath(String path){
		propertieFilepath = path;
	}
	
	public PropertiesParse(){
		
	}
	
	public PropertiesParse(String path){
		this.propertieFilepath = path;
	}
	
	/**
	 * 得到propertie文件对应key的值
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		return  getPropertieValue(propertieFilepath,key);
	}
	
	public void setValue(String key, String value){
		setPropertieValue(propertieFilepath,key,value);
	}
	
	/**
	 * 设置properties属性值
	 * @param filePath
	 * @param key
	 * @param value
	 */
	public void setPropertieValue(String filePath, String key, String value){
		Properties propertie = new Properties();
		FileInputStream inputFile;
		try {
			inputFile = new FileInputStream(filePath);
			propertie.load(inputFile);
			inputFile.close();
			//Writer fos=new FileWriter(filePath,true); 
			FileOutputStream fos = new FileOutputStream(filePath);
			propertie.setProperty(key, value);
			propertie.store(fos, "");   
	        fos.close();   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到propertie文件对应key的值
	 * @param filePath 文件路径
	 * @param key
	 * @return
	 */
	public String getPropertieValue(String filePath, String key) {
		Properties propertie;
		FileInputStream inputFile;
		propertie = new Properties();
		try {
			inputFile = new FileInputStream(filePath);
			propertie.load(inputFile);
			inputFile.close();
			if (propertie.containsKey(key)) {
				String value = propertie.getProperty(key);
				return value;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * 获取Properties
	 * @param filePath
	 * @param m
	 * @return
	 */
	public Properties getPropertie(String filePath, Properties m) {
		Properties propertie;
		FileInputStream inputFile;
		propertie = new Properties();
		//filePath = FileUtil.getRootPath()+filePath;
		try {
			inputFile = new FileInputStream(filePath);
			propertie.load(inputFile);
			inputFile.close();
			m = propertie;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;

	}
	
	public static void main(String[] args){
		PropertiesParse p = new PropertiesParse("config/build.properties");
		String base = p.getValue("reportBase");
		System.out.println("报告根目录："+base);
		String reportpath = base+"\\"+ DateUtil.Now();
		p.setValue("report", reportpath);
		System.out.println("ss"+p.getValue("report"));
	}

}
