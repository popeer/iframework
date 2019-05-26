package com.qa.iFramework.UI.common.util;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 对xml的操作，尤其处理testng代码的生成
 * @author houhaijia
 *
 */
public class Dom4JParser {
	private String selectpath = "config/select.xml";

	// 从文件读取XML，输入文件名，返回XML文档
	public Document read(String fileName) {
		SAXReader reader = new SAXReader();
		Document document = null;
		;
		try {
			document = reader.read(new File(fileName));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	// 得到xml根节点
	public Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	// xml文档转换成字符串
	public String getXmlstr(Document doc) {
		String text = doc.asXML();
		return text;
	}

	// 字符串转换成xml文档对象
	public Document getDocumentByStr(String text) {
		// 字符串转XML
		Document document = null;
		try {
			document = DocumentHelper.parseText(text);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	// 获得机器
	@SuppressWarnings("rawtypes")
	public List getMachines() {
		Document document = read(selectpath);
		List list = document.selectNodes("//selector/machines/machine");
		Iterator iter = list.iterator();
		List ips = new ArrayList();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Attribute a = element.attribute("ip");
			String ip = a.getValue();
			ips.add(ip);
		}
		return ips;
	}
	
	//获得properties路径
	public String getPropertiesPathByName(String name) {
		Document document = read(selectpath);
		String xpath = "//selector/properties";
		Element propertuespath = (Element)document.selectObject(xpath);
		String path = propertuespath.attribute("value").getValue();
		return path;
	}

	// 由ip获得浏览器种类
	@SuppressWarnings("rawtypes")
	public List getExplorerByIp(String ip) {
		Document document = read(selectpath);
		String xpath = "//selector/machines/machine[@ip=\"" + ip + "\"]";
		List list = document.selectNodes(xpath);
		Iterator iter = list.iterator();
		List explorers = new ArrayList();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Iterator iterator = element.elementIterator("explorer");
			while (iterator.hasNext()) {
				Element explorer = (Element) iterator.next();
				explorers.add(explorer.getText());
			}
		}
		return explorers;
	}

	/**
	 * 解析select.xml
	 * 
	 * @param inputXml
	 * @return
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List selecter() {
		Document document = read(selectpath);
		
		Element propertuespath = (Element)document.selectObject("//selector/properties");
		String properties = "";
		if(propertuespath!=null){
			properties = propertuespath.attribute("value").getValue();
		}

				
		List list = document.selectNodes("//selector/cases/case");
		Iterator iter = list.iterator();
		Map cases = new HashMap();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Attribute a = element.attribute("path");
			String key = a.getValue();
			String text = element.getText();
			cases.put(key, text);
		}

		list = document.selectNodes("//selector/machines/machine");
		iter = list.iterator();
		Map machines = new HashMap();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Attribute a = element.attribute("ip");
			String key = a.getValue();
			List explorers = new ArrayList();
			Iterator iterator = element.elementIterator("explorer");
			while (iterator.hasNext()) {
				Element explorer = (Element) iterator.next();
				explorers.add(explorer.getText());
			}
			machines.put(key, explorers);
		}
		List selectdata = new ArrayList();
		selectdata.add(cases);
		selectdata.add(machines);
		selectdata.add(properties);
		return selectdata;
	}
	
	/**
	 * 解析selectProject.xml
	 * @return
	 */
	public Map selectProject() {
		String path = "config/selectProject.xml";
		Document document = read(path);
			
		List list = document.selectNodes("//selector/Projects/project");
		Iterator iter = list.iterator();
		Map project = new HashMap();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			String pa = element.attribute("path").getValue();
			String text = element.getText();
			project.put(pa, text);
		}	
		return project;
	}
	
	/**
	 * 写根据case路径写入testng.xml
	 * 
	 * @param caseinfo 0=casepath 1=propertypath
	 */
	public void generateTestngXml(List<String[]> caseinfo) {
		List<String> propertypath = new ArrayList<String>() ;
		for(int i=0;i<caseinfo.size();i++){
			String[] casee = caseinfo.get(i);
			if(!propertypath.contains(casee[2]))propertypath.add(casee[2]);
		}
		String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String xml = head;
		String prefix = "<suite name=\"Suite\" parallel=\"false\">\n";
		xml += prefix;
		for(int k=0;k<propertypath.size();k++){
			String prop = propertypath.get(k);
			String test = "<test name=\"TestAtFirefox"+k+"\">\n";
			String para1 = "<parameter name=\"driverType\" value=\"firefox\" />\n";
			String para2 = "<parameter name=\"ip\" value=\"seleniumserver\" />\n";
			String para3 = "<parameter name=\"propertieFilepath\" value=\"" + prop + "\" />\n";
			String classes = "<classes>\n";
			xml += (test+para1+para2+para3+classes);
			for(int j=0;j<caseinfo.size();j++){
				String[] a = caseinfo.get(j);
				if(prop.equals(a[2])){
					String clas = "<class name=\"" + a[1] + "\" />\n";
					xml += clas;
				}
			}
			xml +=  "</classes>\n</test>\n";
		}
		xml += "</suite> ";
		File file = new File("config/testng/testng_ok.xml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter fw;
		try {
			fw = new FileWriter("config/testng/testng_ok.xml");
			fw.write(xml);
			fw.flush();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    /**
     * 写入testng代码
     * @param cases
     * @param machine
     * @param explorer
     * @param propertiespath
     */
	public void generateTestngXml(String[] cases, String[] machine, Map<String,String[]> explorer, String propertiespath) {
		String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String xml = head;
		for(int i=0;i<machine.length;i++){
			String prefix = "<suite name=\""+machine[i]+"\" parallel=\"false\">\n";
			xml += prefix;
			String[] e = (String[])explorer.get(machine[i]);
			if(e!=null){
				for(int j=0;j<e.length;j++){
					String first = "<test name=\"TestAt" + e[j] + "\">\n";
					String para1 = "<parameter name=\"driverType\" value=\"" + e[j]
							+ "\" />\n";
					String para2 = "<parameter name=\"ip\" value=\"" + machine[i] + "\" />\n";
					String para3 = "<parameter name=\"propertieFilepath\" value=\"" + propertiespath + "\" />\n";
					String classes = "<classes>\n";
					xml += (first+para1+para2+para3+classes);
					for(int k=0;k<cases.length;k++){
						String clas = "<class name=\"" + cases[k] + "\" />\n";
						xml += clas;
					}
					xml +=  "</classes>\n</test>\n";
				}
			}
			xml += "</suite> ";
		}
		
		File file = new File("config/testng/testng.xml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter fw;
		try {
			fw = new FileWriter("config/testng/testng.xml");
			fw.write(xml);
			fw.flush();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] argv) {
		Dom4JParser dom4jParser = new Dom4JParser();
		// dom4jParser.selecter();
		Map m = new HashMap();
		m.put("10.11.203.118", new String[]{"firefox","ie9"});
		m.put("seleniumserver", new String[]{"firefox","ie7","chrome"});
		dom4jParser.generateTestngXml(new String[]{"key","idea"}, new String[]{"10.11.203.118","seleniumserver"},m,"propertieFilepath" );
	}
}