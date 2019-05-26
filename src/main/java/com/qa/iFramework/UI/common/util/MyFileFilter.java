package com.qa.iFramework.UI.common.util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/** 
 * 
 * @author luolai 
 */  
public class MyFileFilter {  
  
  public static void ss(){
	Font font = new Font("宋体", Font.BOLD, 12);
  	JFrame frame = new JFrame("JFileChooser Popup");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final JLabel directoryLabel = new JLabel("");
    directoryLabel.setFont(font);  
    frame.add(directoryLabel, BorderLayout.NORTH);

    final JLabel filenameLabel = new JLabel("");
    filenameLabel.setFont(font);  
    frame.add(filenameLabel, BorderLayout.SOUTH);

    JFileChooser fileChooser = new JFileChooser(".");
    fileChooser.setFont(font);
    fileChooser.setControlButtonsAreShown(false);  
    frame.add(fileChooser, BorderLayout.CENTER);

    frame.pack();  
    frame.setVisible(true);
  }
  
    public static void main(String[] args) throws IOException {
/*    	JFileChooser jfc = new JFileChooser("D:\\workspace\\kanbox-web-auto\\uploadFiles\\build.xml");
		jfc.setDialogTitle("sdsd");
		jfc.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
		jfc.setDialogType(JFileChooser.OPEN_DIALOG);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = jfc.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			File dir = jfc.getSelectedFile();
			System.out.println(dir.getAbsolutePath());
		}*/
		
    	
    	//MyFileFilter.ss();
    	
    	 
    		    try 
    		{
    		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    		    }
    		    catch(Exception e) {
    		      e.printStackTrace();
    		    }
    		    
    		  
    }  
}  