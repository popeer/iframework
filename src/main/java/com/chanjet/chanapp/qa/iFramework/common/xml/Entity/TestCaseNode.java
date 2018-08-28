package com.chanjet.chanapp.qa.iFramework.common.xml.Entity;

import com.alibaba.fastjson.JSONArray;
import com.chanjet.chanapp.qa.iFramework.Entity.ErrorInfo;
import com.chanjet.chanapp.qa.iFramework.Entity.Result;
import com.chanjet.chanapp.qa.iFramework.Entity.RunStatus;
import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;
import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;
import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.chanjet.chanapp.qa.iFramework.common.Util.*;
import com.chanjet.chanapp.qa.iFramework.common.impl.HttpExcutor;
import com.chanjet.chanapp.qa.iFramework.common.impl.JarExecuter;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.*;
import com.chanjet.chanapp.qa.iFramework.common.xml.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by haijia on 11/23/16.
 */
public class TestCaseNode extends TestCase{
    private static Logger log = LogManager.getLogger(TestCaseNode.class);
    private String name;
    private String id;
    private String dataPathName;
    private List<com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step> testSteps = new ArrayList<com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step>();
    private Parser parser;
    private String status;
    private String duration;
    private String flags;

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, String>> excelData;

    public List<com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step> testSteps) {
        this.testSteps = testSteps;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getID(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getDataPathName() {
        return dataPathName;
    }

    public void setDataPathName(String dataPathName) {
        this.dataPathName = dataPathName;
    }

    public void addTestSteps(com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step testStep){
        testSteps.add(testStep);
    }

    @Override
    public String toString() {
        return "TestCaseNode{" +
                "name='" + name + '\'' +
                "id='" + id + '\'' +
                ", testSteps=" + testSteps +
                '}';
    }

    private void readTestData(){
        if(StringUtils.isEmptyOrSpace(dataPathName)){
            return;
        }

        if(dataPathName.toLowerCase().startsWith("excel")){
            String[] names = dataPathName.split(":");
            try{
//                log.info("Excel Path3:" + getClass().getResource("/").getPath().toString().replace("/target/classes/", "") + "/" + names[1]);
                if((null != sysPro.getProperty("filelocation")) && !StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))){
                    excelData = ExcelHelper.readCSV(sysPro.getProperty("filelocation") + names[1]);
                } else {
                    excelData = ExcelHelper.readCSV(System.getProperty("user.dir") + "/" + names[1]);
                }
            } catch (Exception ex){
                ex.printStackTrace();
                log.error(ex.getStackTrace());
                for(StackTraceElement element : ex.getStackTrace()){
                    log.error(element);
                }

            }

        } else{
            InputStream inputStream = null;

            try {
                inputStream = ClassLoader.getSystemResourceAsStream("properties/" + dataPathName + ".properties");
                parser.sysDataPro = new Properties();
                parser.sysDataPro.load(inputStream);
            } catch (IOException ex) {
                ex.printStackTrace();
                log.error(ex.getMessage());
            }
        }
    }

    @Override
    public JSONArray Run(IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity, RunStatus runStatus) throws Exception{
        JSONArray resultJsonArray = new JSONArray();
        Result result = new Result();
        result.setStep_name(this.getName());

        if(!StringUtils.isEmptyOrSpace(this.status)){
            switch (this.status.toLowerCase().trim()){
                case "skip":
                    log.info(this.getName() + " is skipped by expect!");
                    runStatus.Skipped = runStatus.Skipped + 1;

                    result.setResult(true);
                    ErrorInfo errorInfo = new ErrorInfo(200, this.getName() + " is skipped by expect!", "{}");
                    result.setResultDetail(errorInfo);
                    resultJsonArray.add(result);
                    return resultJsonArray;
                default:
                    break;
            }
        }

        parser = new Parser();

        readTestData();

        if(null == excelData){
            return runSteps(verifier, dataManager, null, commandEntity, runStatus);
        } else{
            for(Map<String, String> entry : excelData){
                resultJsonArray.addAll(runSteps(verifier, dataManager, entry, commandEntity, runStatus));
            }
        }

        return resultJsonArray;

    }

    private JSONArray runSteps(IVerifier verifier, IDataManager dataManager, Map<String, String> entry, CommandEntity commandEntity, RunStatus runStatus) throws Exception {
        List<Object> preExecutedResults = new ArrayList<Object>();
        JSONArray resultJsonArray = new JSONArray();

        if(!StringUtils.isEmptyOrSpace(duration)){
            log.info("duration sleep begin!");
            Thread.sleep(Integer.valueOf(duration));
            log.info("duration sleep end!");
        }

        com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step recordStep = new com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step();
        Object executeResult = null;

        try{
            for(com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step step : this.getTestSteps()){
                executeResult = null;
                recordStep = step;
                Result result = new Result();
                // 设置数据库连接方式，并获取全部sql，放入commmandEntity的sqlList
                if(!StringUtils.isEmptyOrSpace(step.getDburl())){
                    commandEntity = parser.getSqlList(step, commandEntity, preExecutedResults);
                    commandEntity.setSqlDriver(step.getSqlDriver());
                    ArrayList<HashMap<String,Object>> sqlResult = DBUtil.execute(commandEntity);
                    result.setResult(true);
                    ErrorInfo errorInfo = new ErrorInfo(200, "sql pass!", sqlResult);
                    result.setResultDetail(errorInfo);
                    result.setDateTime(DateUtil.getCurrentTime());
                    result.setNode_name(step.getCaseNodeName());
                    result.setStep_name(step.getName());
                    resultJsonArray.add(result);
//                    resultSB.append(result.toString());
//                    resultSB.append("\r\n");
                    preExecutedResults.add(sqlResult);
                    continue;
                }

                //默认是http协议，如果没有指定type，并且没有ur，记录错误信息，该用例跳过。
                if(StringUtils.isEmptyOrSpace(step.getUrl()) && StringUtils.isEmptyOrSpace(step.getType())){
                    log.error("missing url and type! so break the test!");
                    result.setResult(false);
                    result.setError(new ErrorInfo(ExceptionCodes.MissParameter, "missing url and type! so break the test", "{}"));
                    resultJsonArray.add(result);
                    return resultJsonArray;
                }

                if(null != step.getType() && (step.getType().toLowerCase().equals("jar"))){
                    step.setExecutor(new JarExecuter());
                } else{
                    if(null != commandEntity && !StringUtils.isEmptyOrSpace(commandEntity.getDomainName())){
                        if(!StringUtils.isEmptyOrSpace(step.getUrl()) && (step.getUrl().startsWith("http"))){
                            step.setUrl(commandEntity.getDomainName() + step.getUrl().substring(step.getUrl().indexOf("com") + 3, step.getUrl().length()));
                        } else{
                            step.setUrl(commandEntity.getDomainName() + step.getUrl());
                        }
                    }

                    step.setExecutor(new HttpExcutor());
                }

                //1. run interface
                executeResult = step.Run(preExecutedResults, parser, entry);
                preExecutedResults.add(executeResult);

                //2. to verify
                step.setCaseNodeName(this.getName());

                Result verifiedResult = verifier.VerifyResult(step, step.getExpectResponseValues(), entry, preExecutedResults, commandEntity);

                //3. to store all info into DB
                ResultDto resultDto = dataManager.storeExecuteResult(verifiedResult, commandEntity);
                resultJsonArray.add(resultDto);
//                resultSB.append(resultDto.toString());
//                resultSB.append("\r\n");

                //4. summary result
                if(verifiedResult.getResult()){
                    runStatus.Passed = runStatus.Passed + 1;
                } else if(null != verifiedResult.getError() &&
                        null != verifiedResult.getError().getCode() &&
                                ExceptionCodes.NotRun == verifiedResult.getError().getCode()){
                    runStatus.Skipped = runStatus.Skipped + 1;
                } else {
                    runStatus.Failed = runStatus.Failed + 1;
                }


//            if(1001 == verifiedResult.getError().getCode()){
//                log.error(verifiedResult.toString());
//                break;
//            }
            }

            return resultJsonArray;
        } catch (MyCustomerException ex){
            return processException(resultJsonArray, recordStep, ex, executeResult, runStatus);
        } catch(Exception ex){
            return processException(resultJsonArray, recordStep, ex, executeResult, runStatus);
        }

    }

    private JSONArray processException(JSONArray resultArray, com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step recordStep, Exception ex, Object executeResult, RunStatus runStatus) throws MyCustomerException {
        Result verifiedResult = new Result();
        verifiedResult.setNode_name(recordStep.getCaseNodeName());
        verifiedResult.setStep_name(recordStep.getName());

        StringBuffer errorResult = new StringBuffer();
        errorResult.append(
                "Cause:" +
                ex.getCause() == null ? "" : ex.getCause() +
                " , ErrorMessage:" +
                ex.getMessage() == null ? "" : ex.getMessage());
        for(StackTraceElement traceElement : ex.getStackTrace()){
            errorResult.append("\r\n");
            errorResult.append(traceElement.toString());
        }

        verifiedResult.setError(new ErrorInfo(ExceptionCodes.RunTimeException, errorResult.toString(), executeResult));
        verifiedResult.setDateTime(DateUtil.getCurrentTime());
        verifiedResult.setResult(false);

        resultArray.add(verifiedResult);

        runStatus.Failed = runStatus.Failed + 1;
        return resultArray;
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

}
