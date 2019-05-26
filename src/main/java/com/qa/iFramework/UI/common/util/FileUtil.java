package com.qa.iFramework.UI.common.util;

import au.com.bytecode.opencsv.CSVReader;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装文件操作常用方法
 * 
 * @author zhanghaitao
 *
 */
public class FileUtil {
	/**
	 * 用于清空某个目录的所有文件
	 * 
	 * @param filedir
	 */
	public static void cleanFolder(String filedir) {
		File delfilefolder = new File(filedir);
		try {
			if (delfilefolder.exists()) {
				File[] files = delfilefolder.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile())
						files[i].delete();
				}

			}
			delfilefolder.mkdir();
		} catch (Exception e) {
			System.out.println("清空目录操作出错");
			e.printStackTrace();
		}

	}

	/**
	 * 从文件夹中获取第一个文件fullpathname
	 * 
	 * @param filefolder
	 * @return
	 */
	public static String getFile(String filefolder) {
		File af = new File(filefolder);
		File[] files = af.listFiles();
		System.out.println(files.length);
		if (files[0].isFile()) {
			return files[0].getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * 打印文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void printCsv(String file) throws Exception {
		// FileReader aa=new FileReader
		File f = new File(file);
		InputStreamReader aa = new InputStreamReader(new FileInputStream(f),
				"gbk");
		BufferedReader br = new BufferedReader(aa);
		CSVReader reader = new CSVReader(br, ',', '\"', 0);
		String[] nextLine;
		int lineno = 0, colno = 0;
		while ((nextLine = reader.readNext()) != null) {
			for (int i = 0; i < nextLine.length; i++) {
				System.out.println(nextLine[i]);
			}

		}

	}

	/**
	 * 下载文件
	 */
	public static void download() {
		try {
			Thread.sleep(1000);
			Process p = Runtime.getRuntime().exec(
					"/target/classes/config/download.exe");
			p.waitFor();
			System.out.println("下载文件完成！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输入法品专上传文件 upload.exe
	 * C:\Users\zhanghaitao\Pictures\测试物料图片\QQ邮箱方_logo90x90.jpg
	 */
	public static void upload(String filename) {
		try {
			Process p = Runtime.getRuntime().exec(
					"target/classes/config/firefox_upload.exe " + filename);
			p.waitFor();
			System.out.println("上传文件完成！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void clickUpload(String filebutton, int index) {
		ScreenRegion s = new DesktopScreenRegion();
		// Target imageTarget = new ImageTarget(imageURL);
		Target imageTarget = new ImageTarget(new File(filebutton));
		ScreenRegion r = s.wait(imageTarget, 5000);
		List<ScreenRegion> rs = r.findAll(imageTarget);// 返回所有的匹配对象
		Mouse mouse = new DesktopMouse();
		// mouse.click(r.getCenter());
		mouse.click(rs.get(index).getCenter());// 用第二个匹配对象进行操作
	}

	public static void compareImage(String filebutton) {
		ScreenRegion s = new DesktopScreenRegion();
		// Target imageTarget = new ImageTarget(imageURL);
		Target imageTarget = new ImageTarget(new File(filebutton));
		ScreenRegion r = s.wait(imageTarget, 5000);
		List<ScreenRegion> rs = r.findAll(imageTarget);// 返回所有的匹配对象
		if (rs.size() > 0)
			System.out.println("比较成功");
		else
			System.out.println("比较失败:" + filebutton);

	}
	
	public static String getRootPath(){
		String path = System.getProperty("user.dir");
		return path;
	}

	/**
	 * 获取父目录下的所有子目录
	 * 
	 * @param parentPath
	 * @return
	 */
	public static List<String> getChildDirList(String parentPath) {
		File af = new File(parentPath);
		List<String> a = new ArrayList<String>();
		if (af.isDirectory()) {
			File file[] = af.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isDirectory())
					a.add(file[i].getName());
			}
		}
		return a;
	}

	/**
	 * 造文件
	 * 
	 * @param filepath
	 *            文件路径
	 * @param size
	 *            文件大小，单位M
	 */
	public static void writeFile(String filepath, int size) {
		FileOutputStream fop = null;
		File file;
		char c = '我';
		int numPerM = 524288;// 1M 这么多汉字
		file = new File(filepath);
		try {
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
			StringBuffer content = new StringBuffer();
			if (size > 1) {
				for (int i = 0; i < size; i++){
					for (int k = 0; k < numPerM; k++){
						content.append(c);
					}
					content.append("\n");
				}
			}
			byte[] contentInBytes = content.toString().getBytes("utf-8");
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws Exception {
		// FileUtil.cleanFolder("C:\\Users\\zhanghaitao\\Downloads\\agent");
		// FileUtil.printCsv("C:\\Users\\zhanghaitao\\Downloads\\agent\\客服维度存量消耗统计报告.csv");
		// String colvalue="晶晶";

		// colvalue=CommonUtil.GetNums(colvalue);
		// System.out.println(colvalue);
		// FileUtil.download();
		//FileUtil.compareImage("e:\\bushu\\pageimage\\compare1.png");
		System.out.println(FileUtil.getRootPath());
	}

}
