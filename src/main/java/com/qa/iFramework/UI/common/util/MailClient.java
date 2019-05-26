package com.qa.iFramework.UI.common.util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailClient {
	    
	    private MimeMessage mimMsg;//MIME邮件对象
	    private Session session;//邮件会话属性
	    private Properties prop;//系统属性
	      
	    private String username = "liujinlong";//smtp认证用户名密码
	    private String password = "19830224@ljl5";
	      
	    private Multipart mp;//Multipart对象，邮件内容，标题，附件等内容均添加到其中后再生成MIME对象
	      
	    public MailClient(String smtp){
	        setSmtpHost(smtp);  
	        createMimeMessage();  
	    }
	    /** 
	     * 设置SMTP主机 
	     * @param hostName String 主机名 
	     */  
	    public void setSmtpHost(String hostName){
	        if(prop == null) prop = System.getProperties();//获得系统属性对象
	        prop.put("mail.smtp.host", hostName);//设置SMTP主机   
	    }  
	    /** 
	     * 获得会话，并创建邮件对象 
	     * @return boolean 
	     */  
	    public boolean createMimeMessage(){  
	        try {  
	            session = Session.getDefaultInstance(prop,new SmtpAuth(this.username,this.password));//获得邮件会话对象
	            mimMsg = new MimeMessage(session);//创建MIME邮件对象
	            mp = new MimeMultipart();
	            return true;  
	        } catch (Exception e) {
	            e.printStackTrace();  
	            return false;  
	        }  
	    }  
	    /** 
	     * 设置是否需要验证 
	     * @param need boolean 
	     */  
	    public void setNeedAuth(boolean need){  
	        if(this.prop == null) this.prop = System.getProperties();
	          
	        if(need){  
	            this.prop.put("mail.smtp.auth", "true");  
	        }else{  
	            this.prop.put("mail.smtp.auth", "false");  
	        }  
	    }  
	    /** 
	     * 设置用户名、密码 
	     * @param name String 
	     * @param pass String 
	     */  
	    public void setNamePass(String name, String pass){
	        username = name;  
	        password = pass;  
	    }  
	    /** 
	     * 设置标题 
	     * @param mailSubject String 
	     * @return boolean 
	     */  
	    public boolean setSubject(String mailSubject){
	        try {  
	            mimMsg.setSubject(mailSubject);  
	            return true;  
	        } catch (Exception e) {
	            e.printStackTrace();  
	            return false;  
	        }  
	    }  
	    /** 
	     * 设置邮件正文 
	     * @param mailBody String 
	     * @return boolean 
	     */  
	    public boolean setBody(String mailBody){
	        try {  
	            BodyPart bp = new MimeBodyPart();
	            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=gb2312>"+mailBody, "text/html;charset=GB2312");  
	            mp.addBodyPart(bp);  
	            return true;  
	        } catch (Exception e) {
	            System.err.println("设置邮件正文时错误："+e);
	            return false;  
	        }  
	    }  
	    /** 
	     * 添加附件 
	     * @param fileName String 
	     * @return boolean 
	     */  
	    public boolean addFileAffix(String fileName){
	        try {  
	            BodyPart bp = new MimeBodyPart();
	            FileDataSource fds = new FileDataSource(fileName);
	            bp.setDataHandler(new DataHandler(fds));
	            bp.setFileName(fds.getName());  
	              
	            mp.addBodyPart(bp);  
	              
	            return true;  
	        } catch (Exception e) {
	            System.err.println("增加邮件附件："+fileName+"发生错误"+e);
	            return false;  
	        }  
	    }  
	    /** 
	     * 设置发件人 
	     * @param from String 
	     * @return boolean 
	     */  
	    public boolean setFrom(String from){
	        try {  
	            mimMsg.setFrom(new InternetAddress(from));
	            return true;  
	        } catch (Exception e) {
	            System.err.println("设置发件人："+from+"时出错"+e);
	            return false;  
	        }  
	    }  
	    /** 
	     * 设置收件人(one) 
	     * @param to String 
	     * @return boolean 
	     */  
	    public boolean setTo(String to){
	        try {  
	            mimMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	            return true;  
	        } catch (Exception e) {
	            System.err.println("设置收件人："+to+"时出错"+e);
	            return false;  
	        }  
	    }  
	    /** 
	     * 设置收件人(one or many) 
	     * @param to InternetAddress[] 
	     * @return boolean 
	     */  
	    public boolean setTo(InternetAddress[] to){
	        if(to == null) return false;  
	        try {  
	            mimMsg.setRecipients(Message.RecipientType.TO, to);
	            return true;  
	        } catch (Exception e) {
	            System.err.println("设置收件人："+to+"时出错"+e);
	            return false;  
	        }  
	    }  
	    /** 
	     * 设置抄送人 
	     * @param copyTo String 
	     * @return  boolean 
	     */  
	    public boolean setCopyTo(String copyTo){
	        if(copyTo == null) return false;  
	        try {  
	            mimMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyTo));
	            return true;  
	        } catch (Exception e) {
	            return false;  
	        }  
	    }  
	    /** 
	     * 发送操作 
	     * @return boolean 
	     */  
	    public boolean send(){  
	        try {  
	            mimMsg.setContent(mp);  
	            mimMsg.saveChanges();  
	            Transport transport = session.getTransport("smtp");
	            System.out.println(prop.getProperty("mail.smtp.host"));
	            System.out.println(prop.getProperty("mail.smtp.auth"));
	            transport.connect((String)prop.getProperty("mail.smtp.host"), username, password);
	            transport.sendMessage(mimMsg, mimMsg.getAllRecipients());  
	            transport.close();  
	            return true;  
	        } catch (Exception e) {
	            e.printStackTrace();  
	            return false;  
	        }  
	    }  
	    /** 
	     * 开始测试，主程序 
	     * @author lrb 
	     * @param args 
	     */  
	    public static void main(String[] args) {
	        String mailBody = "<meta http-equiv=Content-Type content=text/html; charset=gb2312>"
	            +"<div align=center> 使用程序自动发送邮件测试，请勿回复 </div>";  
	        InternetAddress[] to = new InternetAddress[2];
	        try {  
	            to[0] = new InternetAddress("lufeng_83@163.com");
	            to[1] = new InternetAddress("258166706@qq.com");
	        } catch (Exception e) {
	            e.printStackTrace();  
	        }  
	          
	        MailClient mt = new MailClient("mail.sogou-inc.com");
	        mt.setNeedAuth(false);  
	        if(mt.setSubject("密码保护的注册资料(请不要回复)") == false) return;  
	        if(mt.setBody(mailBody) == false) return;  
	        if(mt.setTo(to) ==  false) return;  
	        if(mt.setFrom("liujinlong@sogou-inc.com") == false) return;  
//	        if(mt.addFileAffix("c://test.xls") == false) return;  
//	        mt.setNamePass("你的邮箱地址（此处使用QQ邮箱为例，需要登录邮箱将相应服务开启）", "你的邮箱密码");  
	        if(mt.send() ==  false) return;  
	    }  
	
}

class SmtpAuth extends Authenticator {
    private String uname,pwd;
    public SmtpAuth(String username, String password){
        this.uname = username;  
        this.pwd = password;  
    }  
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(uname,pwd);
    }  
}  
