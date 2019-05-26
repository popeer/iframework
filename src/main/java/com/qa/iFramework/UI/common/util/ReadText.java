package com.qa.iFramework.UI.common.util;

import java.io.*;

/**
 * 封装解析文本文件函数
 * 
 * @author houhaijia
 * 
 */
public class ReadText {

	public static final void readF1(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			if (line.contains("zidongshenheKey"))
				System.out.println(line);
		}
		br.close();
	}

	public static final void readF2(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader bufferedreader = new BufferedReader(fr);
		String instring;
		while ((instring = bufferedreader.readLine().trim()) != null) {
			if (0 != instring.length()) {
				System.out.println(instring);
			}
		}
		fr.close();
	}

	public static void main(String[] args) throws Exception {
		String filepath = "config/build.txt";
		try {
			String name="haoqingshi";
			FileWriter fw=new FileWriter(filepath,true);
			BufferedWriter out = new BufferedWriter(fw);
			out.newLine();  
			out.write(name); 
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}