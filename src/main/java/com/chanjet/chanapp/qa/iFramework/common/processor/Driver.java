package com.chanjet.chanapp.qa.iFramework.common.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.Entity.ErrorInfo;
import com.chanjet.chanapp.qa.iFramework.Entity.Result;
import com.chanjet.chanapp.qa.iFramework.Entity.RunStatus;
import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;
import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl.ISummaryDaoImpl;
import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl.IVersionDaoImpl;
import com.chanjet.chanapp.qa.iFramework.common.DTO.SummaryDto;
import com.chanjet.chanapp.qa.iFramework.common.DTO.VersionDto;
import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.IDriver;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.chanjet.chanapp.qa.iFramework.common.Util.*;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import com.chanjet.chanapp.qa.iFramework.common.xml.Config;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.TestCaseNode;
import com.chanjet.chanapp.qa.iFramework.common.xml.XmlTcm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by haijia on 11/29/16.
 */
public class Driver implements IDriver {
    private static Logger log = LogManager.getLogger(Driver.class);
    @Resource
    private XmlTcm xmlTcm;

    @Resource
    private ISummaryDaoImpl iSummaryDao;

    @Resource
    private IVersionDaoImpl iVersionDao;

    public XmlTcm getXmlTcm() {
        return xmlTcm;
    }

    public void setXmlTcm(XmlTcm xmlTcm) {
        this.xmlTcm = xmlTcm;
    }

    @Override
    public String Execute(List<File> files, IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity) {
        VersionDto versionDto = new VersionDto();
        versionDto.setPn(commandEntity.getProductName());
        iVersionDao.addVersionPn(versionDto);
        commandEntity.setVersionID(Integer.valueOf(versionDto.getId()));

        RunStatus runStatus = new RunStatus(0,0,0,0);
        JSONObject resultJsonObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        try{
            for(File file : files){
                log.info("Start to run " + file.getPath());
                try {
                    TestCase testCase = xmlTcm.GetTestCase(file.getPath());
                    TestCaseNode node = (TestCaseNode) testCase;
                    resultArray.addAll(node.Run(verifier, dataManager, commandEntity, runStatus));
                } catch (MyCustomerException ex) {
                    Result result = initExceptionResult(resultArray, ex);
                    dataManager.storeExecuteResult(result, commandEntity);
                } catch (Exception ex) {
                    log.error(ex);
                    Result result = initExceptionResult(resultArray, ex);
                    dataManager.storeExecuteResult(result, commandEntity);
                }
            }

        } catch (Exception ex){
            log.error(ex);
            Result result = initExceptionResult(resultArray, ex);
            dataManager.storeExecuteResult(result, commandEntity);
        }

        resultJsonObject.put("Result", resultArray);
        resultJsonObject.put("Pass", runStatus.Passed);
        resultJsonObject.put("Failed", runStatus.Failed);
        resultJsonObject.put("Skipped", runStatus.Skipped);

        resultJsonObject.put("pn", commandEntity.getProductName());
        resultJsonObject.put("operator", StringUtils.isNotEmpty(commandEntity.getUser()) ? commandEntity.getUser() : "nouser");
        resultJsonObject.put("versionID", commandEntity.getVersionID());

        String date = DateUtil.getCurrentTime();

        iSummaryDao.addSummary(new SummaryDto(runStatus.Passed, runStatus.Failed, runStatus.Skipped, date, commandEntity.getProductName()));

        String result = resultJsonObject.toJSONString().replace("\\\\", "");

        //报警
//        AlarmUtil.alerm2(result, commandEntity.getProductName());

        return result;
    }

    private Result initExceptionResult(JSONArray resultArray, Exception ex) {
        Result result = new Result();
        result.setResult(false);
        if(ex instanceof MyCustomerException){
            result.setError(new ErrorInfo(ExceptionCodes.RunTimeException, ((MyCustomerException)ex).getCustomInfo(), ex));
        } else {
            result.setError(new ErrorInfo(ExceptionCodes.RunTimeException, ex.toString(), ex));
        }

        result.setDateTime(DateUtil.getCurrentTime());

        resultArray.add(result);
        return result;
    }

    public static Properties sysPro;

    static{
        InputStream inputStream = null;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream("properties/config.properties");
            sysPro = new Properties();
            sysPro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
//            System.exit(-1);
            log.error(e.getMessage());
        }
    }

    public static List<File> getFileList(String strPath, List<File> filelist) throws Exception{
        File directory;
        log.info(strPath);
        if((null == sysPro.getProperty("filelocation"))
                || StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))
                || strPath.startsWith(sysPro.getProperty("filelocation"))){
//            xmlsource  = ClassLoader.getSystemResourceAsStream(path);
            directory = new File(strPath);

        } else {
            directory = new File(sysPro.getProperty("filelocation") + strPath);
            strPath = directory.getAbsolutePath();
//            String strPath2 = directory.getCanonicalPath();
//            xmlsource = new FileInputStream(caseFile);
        }

        if(null == directory || StringUtils.isEmptyOrSpace(directory.getAbsolutePath())){
            return null;
        }
        strPath = directory.getAbsolutePath();

        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(), filelist); // 获取文件绝对路径
                } else if (fileName.endsWith("xml")) { // 判断文件名是否以.xml结尾
                    String strFileName = files[i].getAbsolutePath();
                    log.info("---" + strFileName);
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        } else{
            if(dir.exists()){
                filelist.add(dir);
            }
        }
        return filelist;
    }

    public static JSONObject getFileList(String strPath, JSONObject resultJsonObject) throws Exception{
        File directory = null;
        log.info(strPath);
        if((null == sysPro.getProperty("filelocation"))
                || StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))
                || strPath.startsWith(sysPro.getProperty("filelocation"))){
            directory = new File(strPath);

        } else {
            if(!strPath.startsWith(sysPro.getProperty("filelocation"))) {
                directory = new File(sysPro.getProperty("filelocation") + strPath);
            } else{
                directory = new File(strPath);
            }
        }

        if(null == directory || StringUtils.isEmptyOrSpace(directory.getAbsolutePath())){
            return null;
        }
        strPath = directory.getAbsolutePath();

        File dir = new File(strPath);

        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    if(resultJsonObject.containsKey("directory")){
                        resultJsonObject.put("directory", resultJsonObject.getString("directory") + ";" + fileName);
                    } else{
                        resultJsonObject.put("directory", fileName);
                    }

                    // 获取文件绝对路径
                    getFileList(files[i].getAbsolutePath(), resultJsonObject);
                } // 判断文件名是否以.xml结尾
                else if (fileName.endsWith("xml")) {
                    if(resultJsonObject.containsKey("file")){
                        resultJsonObject.put("file", resultJsonObject.getString("file") + ";" + fileName);
                    } else{
                        resultJsonObject.put("file", fileName);
                    }
                } else {
                    continue;
                }
            }

        } else{
            if(dir.exists()){
                if(resultJsonObject.containsKey("directory")){
                    resultJsonObject.put("directory", resultJsonObject.getString("directory") + ";" + dir);
                } else{
                    resultJsonObject.put("directory", dir);
                }
            }
        }

        log.debug(resultJsonObject.toJSONString());
        return resultJsonObject;
    }

    public static JSONObject getDictFileList(String strPath, JSONObject resultJsonObject) throws Exception{
        File directory = null;
        log.info(strPath);
        if((null == sysPro.getProperty("filelocation"))
                || StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))
                || strPath.startsWith(sysPro.getProperty("filelocation"))){
            directory = new File(strPath);

        } else {
            if(!strPath.startsWith(sysPro.getProperty("filelocation"))) {
                directory = new File(sysPro.getProperty("filelocation") + strPath);
            } else{
                directory = new File(strPath);
            }
        }

        if(null == directory || StringUtils.isEmptyOrSpace(directory.getAbsolutePath())){
            return null;
        }
        strPath = directory.getAbsolutePath();

        File dir = new File(strPath);

        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    if(resultJsonObject.containsKey("directory")){
                        resultJsonObject.put("directory", resultJsonObject.getString("directory") + ";" + fileName);
                    } else{
                        resultJsonObject.put("directory", fileName);
                    }

//                     不循环获取子文件夹文件
//                    getFileList(files[i].getAbsolutePath(), resultJsonObject);
                } // 判断文件名是否以.xml结尾
                else if (fileName.endsWith("xml")) {
                    if(resultJsonObject.containsKey("file")){
                        resultJsonObject.put("file", resultJsonObject.getString("file") + ";" + fileName);
                    } else{
                        resultJsonObject.put("file", fileName);
                    }
                } else {
                    continue;
                }
            }

        } else{
            if(dir.exists()){
                if(resultJsonObject.containsKey("directory")){
                    resultJsonObject.put("directory", resultJsonObject.getString("directory") + ";" + dir);
                } else{
                    resultJsonObject.put("directory", dir);
                }
            }
        }

        log.debug(resultJsonObject.toJSONString());
        return resultJsonObject;
    }

    public static String getXmlFile(String path){
        List<File> files = new ArrayList<File>();

        try{
            getFileList(path, files);
        } catch (Exception ex){
            log.info(ex);
            return ex.getMessage();
        }


        try{
            for(File file : files){
                InputStream xmlsource = new FileInputStream(file);
                String xmlDodumentString = Config.inputStream2String(xmlsource);
                return xmlDodumentString;
            }
        } catch (Exception ex){
            log.error(ex.getStackTrace());
        }

        return "no file to show";
    }

    public static boolean editXmlFile(String path, String xmlContent){
        try{
            if(StringUtils.isEmptyOrSpace(path)){
                return false;
            }

            if(path.endsWith(".xml")){
                String filePath = "";
                if((null == sysPro.getProperty("filelocation")) || StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))){
                    filePath = path;
                } else {
                    if(!path.startsWith(sysPro.getProperty("filelocation") )) {
                        filePath = sysPro.getProperty("filelocation") + path;
                    } else{
                        filePath = path;
                    }
                }

                if("" != filePath){
                    BufferedWriter bw = null;
                    try {
                        // 根据文件路径创建缓冲输出流
                        bw = new BufferedWriter(new FileWriter(filePath));
                        // 将内容写入文件中
                        bw.write(xmlContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    } finally {
                        // 关闭流
                        if (bw != null) {
                            try {
                                bw.close();
                            } catch (IOException e) {
                                bw = null;
                            }
                        }
                    }
                    return true;
                }
            }

            return false;

        } catch (Exception ex){
            log.info(ex);
            return false;
        }
    }

}
