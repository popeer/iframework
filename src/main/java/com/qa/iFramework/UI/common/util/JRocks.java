package com.qa.iFramework.UI.common.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JRocks {
	
	public static void selectFile(){
		 final JFrame frame = new JFrame("JRocks");
       //  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         //frame.setPreferredSize(new Dimension(1000,400));
        // frame.pack();
//         frame.setFocusableWindowState(true);
//         frame.setVisible(true);
         
		FileDialog fileDialog = new FileDialog(frame);
		fileDialog.setVisible(true);

		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setDirectory("D:\\workspace\\kanbox-web-auto\\uploadFiles\\");
		fileDialog.setFile(".cise.yml");
		fileDialog.dispose();
		frame.dispose();
		System.out.println(fileDialog.getDirectory());
		System.out.println(fileDialog.getFile());
	}
	
/*	public static void chooseFile() {
		JFileChooser jfc = new JFileChooser("D:\\workspace\\kanbox-web-auto\\uploadFiles\\build.xml");
		jfc.setDialogTitle("sdsd");
		jfc.setDialogType(JFileChooser.OPEN_DIALOG);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = jfc.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			File dir = jfc.getSelectedFile();
			System.out.println(dir.getAbsolutePath());
		}
		
		String filePath = "";  
        JFileChooser chooser = new JFileChooser();  
        chooser.setCurrentDirectory(new File("D:\\workspace\\kanbox-web-auto\\uploadFiles\\"));  
        //去掉显示所有文件这个过滤器。  
        chooser.setAcceptAllFileFilterUsed(false);  
  
        chooser.addChoosableFileFilter(new MyFileFilter("xml", "Excel Files"));  
  
        int returnVal = chooser.showSaveDialog(this);  
        if (returnVal == JFileChooser.APPROVE_OPTION) {  
            filePath = chooser.getSelectedFile().getAbsolutePath();  
        }  
        if (filePath.equals("")) {  
            return false;  
        }  
	}*/
	
	public static void generateWindow(){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                final JFrame frame = new JFrame("JRocks");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(600,400));
                frame.pack();
                frame.setFocusableWindowState(true);
                frame.setVisible(true);
                final JWindow window = new JWindow(frame);
                window.setPreferredSize(new Dimension(300,200));
                window.setBackground(new Color(156,20,128,1));
                window.pack();
                window.setLocation(800,50);
                final Timer timer = new Timer(5000,new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            window.setVisible(!window.isShowing());
                        }
                    });
                frame.addWindowListener(new WindowAdapter(){
                        @Override
                        public void windowOpened(WindowEvent e){
                            timer.start();
                        }
                        @Override
                        public void windowClosing(WindowEvent e){
                            timer.stop();
                            window.dispose();
                        }
                    });
            }
        });
}


    public static void main(final String[] args) throws Exception {
    	JRocks.selectFile();
    }

}