package com.qa.iFramework.UI.common.util;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.File;

/**
 * 执行工程根目录下build.xml对应的ant
 * @author houhaijia
 *
 */
public class AntStart {
	
	public void execute(){
		System.out.println("AntStart");
		File buildFile = new File("build.xml");
		Project p = new Project();
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);	
		p.addBuildListener(consoleLogger);
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());

		System.out.println("AntStart  over");
	}
	
	public static void main(String[] args){
		AntStart ant = new AntStart();
		ant.execute();
	}

}
