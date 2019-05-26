package com.qa.iFramework.UI.common.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 定义一些常用的公共方法
 * @author houhaijia
 *
 */
public class CommonUtil {

	/**
	 * 功能概述：抽取字符串中的数字，返回数字字符串
	 * @param s
	 * @return
	 */
	public static String GetNums(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			if ((s.charAt(i) <= '9' && s.charAt(i) >= '0')
					|| s.charAt(i) == '.') {

				str += s.substring(i, i + 1);
			}
		}
		return str;
	}

	/**
	 * 功能概述：将获取到的数字字符串转化长 double
	 * @param s
	 * @return
	 */
	public static double GetDoubleNums(String s) {
		String str = GetNums(s);
		if (str.equals("")) {
			return 0;
		}
		double d = 0.0;
		try {
			d = Double.parseDouble(str);
		} catch (Exception e) {
		}
		return d;

	}
	
	public static String getLongString(double b){
		java.text.DecimalFormat form = new java.text.DecimalFormat( "####0.00"); 
		String s = form.format(b);
        return s;
	}
	
	/**
	 * 功能概述：获取指定日期格式的当前时间
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format){
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	
	/**
	 * 功能概述：根据当前时间生成名称，用于保存随机文件
	 * 细节描述：文件名例如——2012-5-21-18-51-27
	 * @return
	 */
	public static String generateRandomFilename() {
		Calendar c = Calendar.getInstance();
		String filename = "";
		filename = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1)
		+ "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
		+ c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE)
		+ "-" + c.get(Calendar.SECOND);
		return filename;
	}
	
	/**
	 * 返回当前日期，按格式yyyy-MM-dd
	 * @return
	 */
	public static String getDay(){
		Calendar c = Calendar.getInstance();
		String day = "";
		int month = c.get(Calendar.MONTH)+1;
		day = "" + c.get(Calendar.YEAR) + "-" + (month<10?"0"+month:month)
		+ "-" + c.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	/**
	 * 功能概述：获得工程根目录
	 * @return
	 */
	public static String getRootpath(){
		String path = CommonUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String filepath = "";
		if(path.contains("bin"))//开发环境
		   filepath = path.substring(1, path.indexOf("bin"));
		else {//服务器环境
			filepath = path.substring(1, path.indexOf("lib"));
		}
		return filepath;
	}
	
	
	/**
	 * 比较两个Map 是否相等
	 */
	public static boolean checkMapIsEqual(Map m1, Map m2) {
		Set<String> keySet = m1.keySet();
		for (String key : keySet) {
			if (m2.containsKey(key)) {
				if (m2.get(key) != null&&(m1.get(key).toString().equals(m2.get(key).toString()))) {
					continue;
				}
				else return false;
			}
			else return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串是否数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}
	
	/**
	 * 复制文件
	 * @param srcFile
	 * @param destDir
	 * @param newFileName
	 * @return
	 */
    public static long copyFile(File srcFile, File destDir, String newFileName) {
        long copySizes = 0;   
        if (!srcFile.exists()) {   
            System.out.println("源文件不存在");
            copySizes = -1;   
        } else if (!destDir.exists()) {   
            System.out.println("目标目录不存在");
            copySizes = -1;   
        } else if (newFileName == null) {   
            System.out.println("文件名为null");
            copySizes = -1;   
        } else {   
            try {   
                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(new File(destDir,newFileName)).getChannel();
                long size = fcin.size();   
                fcin.transferTo(0, fcin.size(), fcout);   
                fcin.close();   
                fcout.close();   
                copySizes = size;   
            } catch (FileNotFoundException e) {
                e.printStackTrace();   
            } catch (IOException e) {
                e.printStackTrace();   
            }   
        }   
        return copySizes;   
    }  
    
    /**
     * 判断某年某月多少天
     * @param year
     * @param month
     * @return
     */
    public static int getDaysInMonth(int year,int month){
    	java.util.GregorianCalendar date = new java.util.GregorianCalendar(year,month,1); 
    	date.add(Calendar.DATE,   -1);
    	return (date.get(Calendar.DAY_OF_MONTH));
    }
    
    /**
     * 根据日期，得到这个月还剩几天
     * @param day 格式必须是 yyyy-MM-dd形式
     * @return
     */
    public static int getLaveDaysInMonth(String day){
    	String[] dd = day.split("-");
    	int year = Integer.parseInt(dd[0]);
    	int month = Integer.parseInt(dd[1]);
    	int da = Integer.parseInt(dd[2]);
    	int days = getDaysInMonth(year,month);
    	return days-da;
    }
    
    public static List<String> getReportPaths(){
		PropertiesParse p = new PropertiesParse("config/build.properties");
		String base = p.getValue("reportBase");
        List<String> k = FileUtil.getChildDirList(base);
        k.remove("pageimage");
        return k;
    }
    
    public static void main(String[] args){
    	System.out.println(CommonUtil.class.getResource("").getPath());
    	System.out.println(CommonUtil.getRootpath());
    }
    
}
