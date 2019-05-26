package com.qa.iFramework.UI.common.util;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.*;

/**
 * 解析excel，处理批量得到关键词
 * @author houhaijia
 *
 */
public class ExcelParse {
	
	private static String excelPath = "" ;
	
	public  void setFilepath(String path){
		excelPath = path;
	}
	
	public static Map<String,String[][]> parse(String path){
		excelPath = path;
		return parse();
	}
	
	@SuppressWarnings("deprecation")
	public static Map<String,String[][]> parse() {
		    Map map = new HashMap();
			map.clear();
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径   
			XSSFWorkbook xwb;
			try {
				xwb = new XSSFWorkbook(excelPath);
				// 读取第一章表格内容   
				XSSFSheet sheet = xwb.getSheetAt(0);   
				// 定义 row、cell   
				XSSFRow row;   
				String key;
				String in;
				String out;
				String[][] data;
				// 循环输出表格中的内容   
				for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {   
				    row = sheet.getRow(i);   
				    key = row.getCell(2).toString();
				    in = row.getCell(3).toString();
				    out = row.getCell(4).toString();
				    if(!key.equals("")){
				    	String[] ins = in.split("/,/");
				    	int len = ins.length;
					    if(len>1){
					    	if(out.split("/,/").length>1&&out.split("/,/").length==len){
					    		String[] outs = out.split("/,/");
							    data = new String[len][len];
					    		for(int j=0;j<ins.length;j++){
					    			data[j][0] = ins[j];
					    			data[j][1] = outs[j];
					    		}
					    	}
					    	else if(out.split("/,/").length==1){
					    		data = new String[len][len];
					    		for(int j=0;j<ins.length;j++){
					    			data[j][0] = ins[j];
					    			data[j][1] = out;
					    		}
					    	}
					    	else {
					    		data = new String[1][1];
					    		data[0][0] = "excel: "+key+" 数据写错，请按规范写";  //一般是结果的字符串与输入字符串个数对不上
					    	}
					    }
					    else{//不用/,/分隔的表示一个字符串
					    	data = new String[1][2];
					    	data[0][0] = in;
					    	data[0][1] = out;
					    }
					    map.put(key, data);
				    }
				}  
				return map;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return null;

	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public List<String[]> getDataByExcel() {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径   
			XSSFWorkbook xwb;
			try {
				xwb = new XSSFWorkbook(excelPath);
				// 读取第一章表格内容   
				XSSFSheet sheet = xwb.getSheetAt(0);   
				// 定义 row、cell   
				XSSFRow row;   
				List l = new ArrayList();
				// 循环输出表格中的内容   
				for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {  
					String[] data = new String[3];
				    row = sheet.getRow(i);   
				    data[0] = row.getCell(0).toString();
				    data[1] = row.getCell(1).toString();
				    data[2] = row.getCell(2).toString();
				    l.add(data);
				}  
				return l;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return null;

	}
	
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public List<Object[]> getDataByExcel(int columnCount) {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径   
			XSSFWorkbook xwb;
			try {
				xwb = new XSSFWorkbook(excelPath);
				// 读取第一章表格内容   
				XSSFSheet sheet = xwb.getSheetAt(0);   
				// 定义 row、cell   
				XSSFRow row;   
				List l = new ArrayList();
				// 循环输出表格中的内容   
				for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {  
					Object[] data = new Object[3];
				    row = sheet.getRow(i);   
					String[] dataColumn = new String[columnCount-2];
				    for(int j=0;j<columnCount-2;j++){
				    	dataColumn[j] = row.getCell(j).toString();
				    }
				    data[0] = dataColumn;
				    data[1] = row.getCell(columnCount - 2).toString();
				    data[2] = row.getCell(columnCount - 1).toString();
				    l.add(data);
				}  
				return l;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return null;

	}
	
	public static void main(String[] args){
		Map<String,String[][]> dataMap = new HashMap<String,String[][]>();
		parse("config/key.xlsx");
		Set s = dataMap.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String)entry.getKey();
			String[] value = (String[])entry.getValue();
			System.out.println("key = "+key+" value = "+value[0]);
		}
	}

}
