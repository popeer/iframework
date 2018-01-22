package com.chanjet.chanapp.qa.iFramework.common.xml;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.chanjet.chanapp.qa.iFramework.common.Util.StringUtils;
import com.chanjet.chanapp.qa.iFramework.common.Util.Codec;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.*;
import org.apache.commons.digester3.Digester;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.testng.annotations.Test;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.*;

/**
 * Created by haijia on 11/21/16.
 */
public class Parser {
    protected static Logger log = LogManager.getLogger(Parser.class);
    public Digester digester;

    private static String mobileNumber = "";

    private Map<String, String> requestStoredParams = new HashMap<String, String>();

    public Properties sysDataPro;

    public List<Map<String, String>> excelData;

    @Test
    public void getS() throws Exception{
//        Digester digester = new Digester();
//        //指定它不要用DTD验证XML文档的合法性——这是因为我们没有为XML文档定义DTD
//        digester.setValidating(false);
//
//        digester.addObjectCreate("MyTempCase", MyTempCase.class);
//        digester.addSetProperties("MyTempCase");
//        digester.addObjectCreate("MyTempCase/Request", Request.class);
//        digester.addSetProperties("MyTempCase/Request");
//        digester.addSetNext("MyTempCase/Request", "addRequest");
//
//        String xmlStr = "<MyTempCase name=\"PGCreateGroupV5ReqArgs\">\n" +
//                "    <Request id=\"123\" title=\"hahaha\">\n" +
//                "    </Request>\n" +
//                "    <Request id=\"456\" title=\"eew\">\n" +
//                "    </Request>\n" +
//                "</MyTempCase>";
//        MyTempCase myTempCase = digester.parse(new StringReader(xmlStr));
//
//        log.error("mytempcase is " + myTempCase);
    }

    @Test
    public void getSuiteNew() throws Exception{

        initXmlRule();

//        String xmlStr = "<TestCaseNode name=\"test\" author=\"houhaijia\">\n" +
//                "    <Step name=\"PGCreateGroupV5ReqArgs\" >\n" +
//                "        <Request>\n" +
//                "            <Parameter name=\"name\" value=\"ddd\" />\n" +
//                "            <Parameter name=\"tags\" value=\"test\" />\n" +
//                "            <Parameter name=\"joinApprovedType\" value=\"2\" />\n" +
//                "            <Parameter name=\"introduce\" value=\"testintroduce\" />\n" +
//                "            <Parameter name=\"enableSearched\" value=\"1\" />\n" +
//                "        </Request>\n" +
//                "        <Response>\n" +
//                "            <Expect>\n" +
//                "                <Parameter name=\"statusCode\" value=\"200\" />\n" +
//                "                <Parameter name=\"name\" value=\"haha\" />\n" +
//                "            </Expect>\n" +
//                "            <Context>\n" +
//                "                <Parameter name=\"uri\" value=\"www.baidu.com\" />\n" +
//                "            </Context>\n" +
//                "        </Response>\n" +
//                "    </Step>\n" +
//                "</TestCaseNode>";
//
//        TestCaseNode mySuite = digester.parse(new StringReader(xmlStr));
//        log.error(mySuite);

        InputStream xmlsource  = ClassLoader.getSystemResourceAsStream("testCases/Cenc/testGetBssProductInfoByGet.xml");
        String xmlDodumentString = inputStream2String(xmlsource);
        TestCaseNode xmlTestCaseNode = digester.parse(new StringReader(xmlDodumentString));

        log.info((xmlTestCaseNode));
    }

    public List<RequestParameter> getRequestDataFromCaseForJar(Step testStep, List<Object> preExecutedResults) throws IOException{
        List<RequestParameter> requestParams = new ArrayList<RequestParameter>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter requestParameter = parameter;
            requestParameter.setValue(actionRequestParameter(parameter.getAction(), getParameterValueFromSysPro(parameter, preExecutedResults)));
            requestParams.add(requestParameter);
        }
        return requestParams;
    }

    public List<RequestParameter> getRequestExcelFromCaseForJar(Step testStep, List<Object> preExecutedResults, Map<String, String> excelParams) throws IOException{
        List<RequestParameter> requestParams = new ArrayList<RequestParameter>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter requestParameter = parameter;
            requestParameter.setValue(actionRequestParameter(parameter.getAction(), getParameterValueFromExcel(parameter, preExecutedResults, excelParams)));
            requestParams.add(requestParameter);
        }
        return requestParams;
    }

    public Map<String, String> getRequestDataFromCase(Step testStep, List<Object> preExecutedResults) throws IOException{
        Map<String, String> requestParams = new HashMap<String, String>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter newparameter = getRequestParams(parameter, preExecutedResults, null);
            requestParams.put(
                    newparameter.getName(),
                    actionRequestParameter(newparameter.getAction(), getParameterValueFromSysPro(newparameter, preExecutedResults)));
        }
        return requestParams;
    }

    public Map<String, String> getRequestDataFromExcel(Step testStep, List<Object> preExecutedResults, Map<String, String> excelParams)
            throws IOException{
        Map<String, String> requestParams = new HashMap<String, String>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter newparameter = getRequestParams(parameter, preExecutedResults, excelParams);
            if(StringUtils.isEmptyOrSpace(newparameter.getColName())){
                requestParams.put(
                        newparameter.getName(),
                        actionRequestParameter(newparameter.getAction(), getParameterValueFromSysPro(newparameter, preExecutedResults)));
            } else{
                requestParams.put(
                        newparameter.getName(),
                        !StringUtils.isEmptyOrSpace(newparameter.getExchangeFlag()) ? "" :
                                actionRequestParameter(newparameter.getAction(), getParameterValueFromExcel(newparameter, preExecutedResults, excelParams)));
            }
        }
        return requestParams;
    }

    private RequestParameter getRequestParams(RequestParameter parameter, List<Object> preExecutedResults, Map<String, String> excelParams) throws IOException{
        RequestParameter resultParameter =  parameter;
        if(StringUtils.isEmptyOrSpace(parameter.getValue()) && (!StringUtils.isEmptyOrSpace(parameter.getName())) && null != parameter.getExchangeFlag() && parameter.getExchangeFlag().equals("true")){
            parameter.setValue(parameter.getName());

            if(!StringUtils.isEmptyOrSpace(parameter.getColName()) && null != excelParams){
                resultParameter.setName(actionRequestParameter(parameter.getAction(), getParameterValueFromExcel(parameter, preExecutedResults, excelParams)));
            } else{
                resultParameter.setName(actionRequestParameter(parameter.getAction(), getParameterValueFromSysPro(parameter, preExecutedResults)));
            }

            resultParameter.setValue("");
            parameter.setValue("");
        }

        return resultParameter;
    }

    public CommandEntity getSqlList(Step step, CommandEntity commandEntity, List<Object> preExecutedResults){
        if(null == commandEntity){
            commandEntity = new CommandEntity();
        }

        if(StringUtils.isEmptyOrSpace(step.getDburl()) && (StringUtils.isEmptyOrSpace(commandEntity.getDbURL()))){
            log.error("miss dbUrl!");
            return commandEntity;
        }

        if(StringUtils.isEmptyOrSpace(step.getUid()) && (StringUtils.isEmptyOrSpace(commandEntity.getUid()))){
            log.error("miss DB UserName!");
            return commandEntity;
        }

        if(StringUtils.isEmptyOrSpace(step.getPwd()) && (StringUtils.isEmptyOrSpace(commandEntity.getPwd()))){
            log.error("miss db password!");
            return commandEntity;
        }

        if(!StringUtils.isEmptyOrSpace(step.getDburl()) && (StringUtils.isEmptyOrSpace(commandEntity.getDbURL()))){
            commandEntity.setDbURL(step.getDburl());
        }

        if(!StringUtils.isEmptyOrSpace(step.getUid()) && (StringUtils.isEmptyOrSpace(commandEntity.getUid()))){
            commandEntity.setUid(step.getUid());
        }

        if(!StringUtils.isEmptyOrSpace(step.getPwd()) && (StringUtils.isEmptyOrSpace(commandEntity.getPwd()))){
            commandEntity.setPwd(step.getPwd());
        }

        LinkedList<String>  sqlList = new LinkedList<String>();

        try{
            Request request = step.getRequest();
            for(RequestParameter parameter : request.getParameters()){
                if(!StringUtils.isEmptyOrSpace(parameter.getValue())){
                    if(!StringUtils.isEmptyOrSpace(parameter.getSequence()) &&
                            !StringUtils.isEmptyOrSpace(parameter.getKeyword()) &&
                            null != preExecutedResults.get(Integer.parseInt(parameter.getSequence()))){
                        JSONObject tempJsonObject = (JSONObject)preExecutedResults.get(Integer.parseInt(parameter.getSequence()));
                        parameter.setValue(parameter.getValue().replace("{0}", String.valueOf(tempJsonObject.get(parameter.getKeyword()))));
                    } else if(!StringUtils.isEmptyOrSpace(parameter.getSequence_type_resultPath_valuePath())){
                        String newValue = processResultPathAndValuePath(parameter, preExecutedResults, parameter.getValue());
                        parameter.setValue(parameter.getValue().replace("{0}", newValue));
                    } else if(!StringUtils.isEmptyOrSpace(parameter.getParam_type_key())){
                        String newValue = processParamTypeKey(parameter, parameter.getValue());
                        parameter.setValue(newValue);
                    }

                    sqlList.add(parameter.getValue());
                    commandEntity.setSqlKeyName(
                            StringUtils.isEmptyOrSpace(parameter.getSqlSelectField()) ? "*" : parameter.getSqlSelectField());

                } else if(!StringUtils.isEmptyOrSpace(parameter.getSqlScript())){
                    String sqlArray = readSqlScript(parameter.getSqlScript());
                    String[] sqlScripts = null;
                    if(!StringUtils.isEmptyOrSpace(sqlArray)){
                        sqlArray = sqlArray.replace("\n", "");
                        sqlScripts = sqlArray.split(";");
                    }

                    if(null == sqlScripts){
                        log.error("no sql executing!");
                        return commandEntity;
                    }

                    for(String sql : sqlScripts){
                        sqlList.add(sql);
                    }
                } else if(!StringUtils.isEmptyOrSpace(parameter.getSequence_type_keyword_key())){
                    String[] sequence_type_keyword_key = parameter.getSequence_type_keyword_key().split("___");
                    if(null != sequence_type_keyword_key && 4 == sequence_type_keyword_key.length){
                        ArrayList<HashMap<String,Object>> sqlResult = (ArrayList<HashMap<String,Object>>)preExecutedResults.get(Integer.parseInt(sequence_type_keyword_key[0]));
                        if(null != sqlResult && 0 < sqlResult.size()){
                            // TODO: 1/5/18 暂且只支持一行数据
                            HashMap<String, Object> row = sqlResult.get(0);
                            Object sqlValue = row.get(sequence_type_keyword_key[2]);
                            parameter.setValue(parameter.getValue().replace("{0}", String.valueOf(sqlValue)));
                        }
                    }

                }

            }
        } catch (Exception ex){
            log.error("get DB SqlList account error: " + ex.getMessage());
        }

        commandEntity.setSqlList(sqlList);

        return commandEntity;
    }

    private String readSqlScript(String path){
        try{

            StringBuffer content = new StringBuffer();
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(
                                            System.getProperty("user.dir")  + "/" + path),
                                    "UTF-8"));

            String line = null;
            while((line = br.readLine()) != null ){
                content.append(line+"\n");
            }
            br.close();
            return content.toString();

        }catch(Exception e){
            log.error(e);
        }

        return "";
    }

    protected String getParameterValueFromSysPro(RequestParameter parameter, List<Object> preExecutedResults) throws IOException{

        String value = parameter.getValue();

        if(value.startsWith("$") && value.endsWith("$")){
            value = value.replace("$", "");

            if(sysPro.containsKey(value.toLowerCase().trim())){
                if(!StringUtils.isEmptyOrSpace(parameter.getKeyword())){
                    String newValue = sysPro.getProperty(value.toLowerCase().trim());
                    JSONObject jsonObject = new JSONObject();
                    if (StringUtils.isEmptyOrSpace(parameter.getType()) || parameter.getType().toLowerCase().equals("json")) {
                        jsonObject = JSONObject.parseObject(newValue);
                        value = synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword());
                    }  if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("obj")) {
                        jsonObject = JSONObject.parseObject(newValue);
                        value = synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword());
                    } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array")) {
                        JSONArray jsonArray = JSONArray.parseArray(newValue);
                        jsonObject = (JSONObject) jsonArray.get(0);
                        value = "[" + synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword()) + "]";
                    }
                } else{
                    value = sysPro.getProperty(value.toLowerCase().trim());
                }
            }
        }

        if(value.startsWith("#") && value.endsWith("#")){
            String[] sequences = parameter.getSequence().split("_");
            for(String s : sequences) {
                Integer sequence = Integer.parseInt(s);
                switch (preExecutedResults.get(sequence).getClass().getName()) {
                    case "com.alibaba.fastjson.JSONObject":
                        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) preExecutedResults.get(sequence);
                        if(jsonObject.containsKey(value.substring(1, value.length() - 1))) {
                            value = jsonObject.get(value.substring(1, value.length() - 1)).toString();
                        }
                        continue;
                    case "java.util.ArrayList":
                        List<Object> results = (ArrayList) preExecutedResults.get(sequence);
                        if ((null != results) && (2 == results.size())) {
                            com.alibaba.fastjson.JSONObject innerObject = (com.alibaba.fastjson.JSONObject) results.get(0);
                            if(innerObject.containsKey(value.substring(1, value.length() - 1))) {
                                value = innerObject.get(value.substring(1, value.length() - 1)).toString();
                            }
                            continue;
                        }
                    case "java.lang.String":
                        com.alibaba.fastjson.JSONObject jsonStringObject = JSONObject.parseObject(preExecutedResults.get(sequence).toString());
                        if(jsonStringObject.containsKey(value.substring(1, value.length() - 1))){
                            value = jsonStringObject.get(value.substring(1, value.length() - 1)).toString();
                        }
                        continue;
                    default:
                        log.debug("Attention: Request Parameter Not Found Out " + parameter.getName());
                        continue;

                }
            }
        }

        if(value.equals("random%mobile%")){
            value = generateMobileNumber();
        }

        if(value.equals("get%mobile%")){
            value = generateSolidMobileNumber();
        }

        if(value.equals("random%email%")){
            value = getRandomString(8) + "@chanjettest.com";
        }

        if(value.equals("random%string8%")){
            value = getRandomString(8);
        }

        if((null != sysDataPro) && (sysDataPro.containsKey(value))){
            value = sysDataPro.getProperty(value);
        }

        value = exchangeValue(parameter, preExecutedResults, value);

        value = logicalArchiveValue(parameter, preExecutedResults, value);

        value = storeParams(parameter, value);

        return value;
    }

    //定义了offlineFlag，就不能定义keyword,否则走不到offlineFlag的逻辑
    private String exchangeValue(RequestParameter parameter, List<Object> preExecutedResults, String newValue) {
        if(!StringUtils.isEmptyOrSpace(parameter.getOfflineFlag()) && parameter.getOfflineFlag().equals("1")) {
            JSONObject jsonObject = new JSONObject();
            if (StringUtils.isEmptyOrSpace(parameter.getType()) || parameter.getType().toLowerCase().equals("json")) {
                jsonObject = JSONObject.parseObject(newValue);
                return synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword());
            } else if (parameter.getType().toLowerCase().equals("array")) {
                JSONArray jsonArray = JSONArray.parseArray(newValue);
                jsonObject = (JSONObject) jsonArray.get(0);
                return "[" + synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword()) + "]";
            } else if (parameter.getType().toLowerCase().equals("string")) {

            }
        }
        return newValue;

    }

    //定义了getSequence_type_keyword，就不能定义keyword,否则走不到sequence_type_keyword的逻辑
    private String logicalArchiveValue(RequestParameter parameter, List<Object> preExecutedResults, String value){
        String newValue = value;

        if(!StringUtils.isEmptyOrSpace(parameter.getSequence_type_keyword_key())){
            String[] logicalValues = parameter.getSequence_type_keyword_key().split(",");
            JSONObject currentParaToJsonObject = new JSONObject();
            if (StringUtils.isEmptyOrSpace(parameter.getType()) || parameter.getType().toLowerCase().equals("json")) {
                currentParaToJsonObject = JSONObject.parseObject(newValue);
            }  if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("obj")) {
                log.warn("TO BE DONE FOR Parameter Type = obj in Parse.java");
            } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array")) {
                JSONArray jsonArray = JSONArray.parseArray(newValue);
                currentParaToJsonObject = (JSONObject) jsonArray.get(0);
            } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("string")) {

            }

            for(String sequence_type_keywords : logicalValues){
                String[] sequence_type_keyword_key = sequence_type_keywords.split("___");
                if(4 == sequence_type_keyword_key.length){
                    Object preResult = new Object();
                    if(preExecutedResults.size() - 1 < Integer.parseInt(sequence_type_keyword_key[0])){
                        log.error("sequence is oversize than preExecutedResults for Parameter " + sequence_type_keyword_key[0]);
                        continue;
                    }

                    preResult = preExecutedResults.get(Integer.parseInt(sequence_type_keyword_key[0]));

                    if(null != preResult){
                        boolean ifReplaced = false;

                        switch (sequence_type_keyword_key[1].toLowerCase().trim()){
                            case "string":
                                ifReplaced = currentParaToJsonObject.replace(sequence_type_keyword_key[3],
                                        currentParaToJsonObject.get(sequence_type_keyword_key[3]),
                                        preResult);
                                if(ifReplaced){
                                    newValue = currentParaToJsonObject.toJSONString();
                                } else{
                                    log.error("failed to replace value:" + currentParaToJsonObject);
                                }
                                break;
                            case "array":
                                log.warn("TO BE DONE... array logicalArchiveValue in Parse.java");
                                break;
                            case "json":
                                JSONObject resultJson = (JSONObject)preResult;
                                if(null != resultJson && resultJson.containsKey(sequence_type_keyword_key[2])){
                                    ifReplaced = currentParaToJsonObject.replace(sequence_type_keyword_key[3],
                                            currentParaToJsonObject.get(sequence_type_keyword_key[3]),
                                            resultJson.get(sequence_type_keyword_key[2]));
                                    if(ifReplaced){
                                        newValue = currentParaToJsonObject.toJSONString();

                                    } else{
                                        log.error("failed to replace value:" + currentParaToJsonObject);
                                    }
                                }
                                break;
                            case "string_json":
                                JSONObject otherJson = (JSONObject)preResult;
                                newValue = (String) otherJson.get(sequence_type_keyword_key[2]);
                                break;
                            case "json_int":
                                JSONObject preJson = (JSONObject)preResult;
                                newValue = preJson.get(sequence_type_keyword_key[2]).toString();
                                break;
                            case "json_array":
                                JSONObject resultJson_2 = (JSONObject)preResult;
                                if(null != resultJson_2 && resultJson_2.containsKey(sequence_type_keyword_key[2])){
                                    JSONArray refactorObj = JSONArray.parseArray("[" + resultJson_2.get(sequence_type_keyword_key[2]).toString() + "]");
                                    ifReplaced = currentParaToJsonObject.replace(
                                            sequence_type_keyword_key[3],
                                            currentParaToJsonObject.get(sequence_type_keyword_key[3]),
                                            refactorObj);
                                    if(ifReplaced){
                                        newValue = currentParaToJsonObject.toJSONString();
                                    } else{
                                        log.error("failed to replace value:" + currentParaToJsonObject);
                                    }
                                }
                                break;
                            case "json_string":
                                JSONObject preStepRsultJson = (JSONObject)preResult;
                                newValue = newValue.replace(sequence_type_keyword_key[3], String.valueOf(preStepRsultJson.get(sequence_type_keyword_key[2])));
                                break;
                            case "sql_json"://支持数据库返回的情况
                                ArrayList<HashMap<String,Object>> sqlResult = (ArrayList<HashMap<String,Object>>)preResult;
                                if(null != sqlResult && 0 < sqlResult.size()){
                                    // TODO: 1/5/18 暂且只支持一行数据
                                    HashMap<String, Object> row = sqlResult.get(0);
                                    Object sqlValue = row.get(sequence_type_keyword_key[2]);
                                    currentParaToJsonObject = JSONObject.parseObject(newValue);
                                    currentParaToJsonObject.replace(sequence_type_keyword_key[3],
                                            String.valueOf(sqlValue));
                                    newValue = currentParaToJsonObject.toJSONString();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        if(!StringUtils.isEmptyOrSpace(parameter.getSequence_type_resultPath_valuePath())){
            newValue = processResultPathAndValuePath(parameter, preExecutedResults, newValue);
        }

        if(!StringUtils.isEmptyOrSpace(parameter.getParam_type_key())){
            newValue = processParamTypeKey(parameter, newValue);
        }

        if(!StringUtils.isEmptyOrSpace(parameter.getParam_keyword_keyPath())){
            String[] logicalValues = parameter.getParam_keyword_keyPath().split(",");
            for(String logicalValue : logicalValues){
                String[] keyword_paths = logicalValue.split("___");
                if(3 == keyword_paths.length) {
                    if (requestStoredParams.containsKey(keyword_paths[1])) {
                        switch (keyword_paths[0].toLowerCase().trim()) {
                            case "json":
                                JSONObject paramJsonObject = JSONObject.parseObject(newValue);
                                if(JSONPath.contains(paramJsonObject, keyword_paths[2])){
                                    JSONPath.set(paramJsonObject, keyword_paths[2],
                                            requestStoredParams.get(keyword_paths[1]));
                                    newValue = paramJsonObject.toJSONString();
                                }
                                break;
                            case "string":
                                break;
                            case "array":
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array") && (!newValue.startsWith("[") || newValue.endsWith("!]"))){
            newValue = "[" + newValue + "]";
        }
        return newValue;
    }

    private String processParamTypeKey(RequestParameter parameter, String newValue) {
        String[] logicalValues = parameter.getParam_type_key().split(",");
        for(String type_keywords : logicalValues){
            String[] type_keyword = type_keywords.split("___");
            if(3 == type_keyword.length) {
                if (requestStoredParams.containsKey(type_keyword[1])) {
                    switch (type_keyword[0].toLowerCase().trim()) {
                        case "json":
                            JSONObject paramJsonObject = JSONObject.parseObject(newValue);
                            boolean ifReplaced = paramJsonObject.replace(type_keyword[2],
                                    paramJsonObject.get(type_keyword[2]),
                                    requestStoredParams.get(type_keyword[1]));
                            if (!ifReplaced) {
                                log.error("failed to replace value:" + paramJsonObject);
                            } else {
                                newValue = paramJsonObject.toJSONString();
                            }
                            break;
                        case "string":
                            if(newValue.contains(type_keyword[2])){
                                newValue = newValue.replace(type_keyword[2], requestStoredParams.get(type_keyword[1]));
                            }
                            break;
                        case "array":
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return newValue;
    }

    private String processResultPathAndValuePath(RequestParameter parameter, List<Object> preExecutedResults, String newValue) {
        String[] logicalValues = parameter.getSequence_type_resultPath_valuePath().split(",");
        JSONObject valueJson = new JSONObject();
        if (StringUtils.isEmptyOrSpace(parameter.getType()) || parameter.getType().toLowerCase().equals("json")) {
            valueJson = JSONObject.parseObject(newValue);
        }
        if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("obj")) {
            log.warn("TO BE DONE FOR Parameter Type = obj in Parse.java");
        } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array")) {
            JSONArray jsonArray = JSONArray.parseArray(newValue);
            valueJson = (JSONObject) jsonArray.get(0);
        } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("string")) {

        }

        for(String sequence_type_resultPath_keyword_valuePath_values : logicalValues){
            String[] sequence_type_resultPath_keyword_valuePath_key = sequence_type_resultPath_keyword_valuePath_values.split("___");
            if(4 == sequence_type_resultPath_keyword_valuePath_key.length){
                Object preResult = new Object();
                if(preExecutedResults.size() - 1 < Integer.parseInt(sequence_type_resultPath_keyword_valuePath_key[0])){
                    log.error("sequence is oversize than preExecutedResults for Parameter " + sequence_type_resultPath_keyword_valuePath_key[0]);
                    continue;
                }

                preResult = preExecutedResults.get(Integer.parseInt(sequence_type_resultPath_keyword_valuePath_key[0]));

                if(null != preResult){
                    JSONObject resultJson;
                    switch (sequence_type_resultPath_keyword_valuePath_key[1].toLowerCase().trim()){
                        case "string":

                            break;
                        case "array":

                            break;
                        case "json":
                            resultJson = (JSONObject)preResult;

                            if(null != resultJson && JSONPath.contains(valueJson, sequence_type_resultPath_keyword_valuePath_key[3])
                                    && JSONPath.contains(resultJson, sequence_type_resultPath_keyword_valuePath_key[2])){

                                JSONPath.set(
                                        valueJson,
                                        sequence_type_resultPath_keyword_valuePath_key[3],
                                        String.valueOf(JSONPath.eval(resultJson, sequence_type_resultPath_keyword_valuePath_key[2])));
                                newValue = valueJson.toJSONString();
                            }
                            break;
                        case "string_json":

                            break;
                        case "json_string":
                            resultJson = (JSONObject)preResult;

                            if(null != resultJson && JSONPath.contains(resultJson, sequence_type_resultPath_keyword_valuePath_key[2])){
                                newValue = String.valueOf(JSONPath.eval(resultJson, sequence_type_resultPath_keyword_valuePath_key[2]));
                            }
                            break;
                        case "json_array":
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return newValue;
    }

    //设置storeParam，就要设置alias
    private String storeParams(RequestParameter parameter, String value){
        String newValue = value;
        if(!StringUtils.isEmptyOrSpace(parameter.getStoreParam())){

            switch (parameter.getStoreParam().toLowerCase().trim()) {
                case "allstring":
                    if(StringUtils.isEmptyOrSpace(parameter.getAlias())){
                        log.warn("设置storeParam，就要设置alias for Parameter " +
                                (StringUtils.isEmptyOrSpace(parameter.getName()) ? parameter.getColName():parameter.getName()));
                    } else{
                        parameter.setStoreParam(parameter.getAlias());
                    }
                    break;
                default:
                    // 如果指定了StoreParam, 就必须指定type
                    if(!StringUtils.isEmptyOrSpace(parameter.getType())) {
                        switch (parameter.getType().toLowerCase()){
                            case "string":
                                break;
                            case "obj":
                                break;
                            case "array":
                                break;
                            case "json":
                                JSONObject requestJsonObject = JSONObject.parseObject(newValue);
                                newValue = (String)requestJsonObject.get(parameter.getStoreParam());
                                break;
                            default:
                                log.warn("no matched RequestParameter.getType:" + parameter.getType());
                        }
                    }
            }

            if(!requestStoredParams.containsKey(parameter.getAlias())){
                requestStoredParams.put(parameter.getAlias(), newValue);
            } else{
                log.warn("requestStoredParams already contain this key:" + parameter.getAlias() + " in Parse.java");
            }

        }

        return value;
    }

    protected String getParameterValueFromExcel(RequestParameter parameter, List<Object> preExecutedResults, Map<String, String> excelParams) throws IOException{
        if(!excelParams.containsKey(parameter.getColName())){
            log.error("Attending: missing col in excel for colName:" + parameter.getColName());
            throw new IOException("Attending: missing col in excel for colName:" + parameter.getColName());
        }

        String excelColName = parameter.getColName();
        String colValue = excelParams.get(excelColName);

        if(excelColName.startsWith("$") && excelColName.endsWith("$")){
            if(!StringUtils.isEmptyOrSpace(parameter.getKeyword())){
                JSONObject jsonObject = new JSONObject();
                if (StringUtils.isEmptyOrSpace(parameter.getType()) || parameter.getType().toLowerCase().equals("json")) {
                    jsonObject = JSONObject.parseObject(colValue);
                    colValue = synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword());
                }  if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("obj")) {
                    jsonObject = JSONObject.parseObject(colValue);
                    colValue = synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword());
                } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array")) {
                    JSONArray jsonArray = JSONArray.parseArray(colValue);
                    jsonObject = (JSONObject) jsonArray.get(0);
                    colValue = "[" + synonymousSubstitution(jsonObject, preExecutedResults, parameter.getSequence(), parameter.getKeyword()) + "]";
                }
            }
        }

        if(!StringUtils.isEmptyOrSpace(parameter.getSequence())){
            String[] sequences = parameter.getSequence().split("_");
            for(String s : sequences){
                Integer sequence = Integer.parseInt(s);
                if(excelParams.get(excelColName).startsWith("#") && excelParams.get(excelColName).endsWith("#")){
                    switch (preExecutedResults.get(sequence).getClass().getName()){
                        case "com.alibaba.fastjson.JSONObject":
                            com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject)preExecutedResults.get(sequence);
                            if(jsonObject.containsKey(excelParams.get(excelColName).substring(1, excelParams.get(excelColName).length() - 1))){
                                return jsonObject.get(excelParams.get(excelColName).substring(1, excelParams.get(excelColName).length() - 1)).toString();
                            }
                            continue;
                        case "java.util.ArrayList":
                            List<Object> results = (ArrayList)preExecutedResults.get(sequence);
                            if((null != results) && (2 == results.size())) {
                                com.alibaba.fastjson.JSONObject innerObject = (com.alibaba.fastjson.JSONObject) results.get(0);
                                if(innerObject.containsKey(excelParams.get(excelColName).substring(1, excelParams.get(excelColName).length() - 1))){
                                    return innerObject.get(excelParams.get(excelColName).substring(1, excelParams.get(excelColName).length() - 1)).toString();
                                }
                            }
                            continue;
                        case "java.lang.String":
                            com.alibaba.fastjson.JSONObject jsonStringObject = JSONObject.parseObject(preExecutedResults.get(sequence).toString());
                            if(jsonStringObject.containsKey(excelParams.get(excelColName).substring(1, excelParams.get(excelColName).length() - 1))){
                                return jsonStringObject.get(excelParams.get(excelColName).substring(1, excelParams.get(excelColName).length() - 1)).toString();
                            }
                            continue;
                        default:
                            log.debug("Attention: Request Parameter Not Found Out " + parameter.getName());
                            continue;

                    }
                }
            }
        }

        if(colValue.equals("random%mobile%")){
            return generateMobileNumber();
        }

        if(colValue.equals("get%mobile%")){
            return generateSolidMobileNumber();
        }

        if(colValue.equals("random%email%")){
            colValue = getRandomString(8) + "@chanjettest.com";
        }

        if(colValue.equals("random%string8%")){
            colValue = getRandomString(8);
        }

        return colValue;
    }

    private String synonymousSubstitution(JSONObject jsonObject, List<Object> preExecutedResults, String sequence, String keyword){
        String[] sequences = new String[0];
        if(!StringUtils.isEmptyOrSpace(sequence)){
            sequences = sequence.split("_");
        }

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {

            switch (String.valueOf(entry.getValue())){
                case "random%mobile%":
                    entry.setValue(generateMobileNumber());
                    break;
                case "get%mobile%":
                    entry.setValue(generateSolidMobileNumber());
                    break;
                case "special#registCode#":
                    for(String s : sequences){
                        Integer index = Integer.parseInt(s);
                        if("com.alibaba.fastjson.JSONObject" == preExecutedResults.get(index).getClass().getName()) {
                            com.alibaba.fastjson.JSONObject preResult = (com.alibaba.fastjson.JSONObject) preExecutedResults.get(index);
                            if(preResult.containsKey("registCode")){
                                entry.setValue(preResult.get("registCode").toString());
                                break;
                            } else{
                                log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + "registCode" + " sequence: " + index);
                            }

                            continue;
                        } else{
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey());
                        }
                    }
                    break;
                case "special#userId#":
                    for(String s : sequences){
                        Integer index = Integer.parseInt(s);
                        if("com.alibaba.fastjson.JSONObject" == preExecutedResults.get(index).getClass().getName()) {
                            com.alibaba.fastjson.JSONObject preResult = (com.alibaba.fastjson.JSONObject) preExecutedResults.get(index);
                            if(preResult.containsKey("userId")){
                                entry.setValue(preResult.get("userId").toString());
                                break;
                            } else{
                                log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + keyword);
                            }

                            continue;
                        } else{
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey());
                        }
                    }

                    break;
                case "special#orgId#":
                    for(String s : sequences){
                        Integer index = Integer.parseInt(s);
                        if("com.alibaba.fastjson.JSONObject" == preExecutedResults.get(index).getClass().getName()) {
                            com.alibaba.fastjson.JSONObject preResult = (com.alibaba.fastjson.JSONObject) preExecutedResults.get(index);
                            if(preResult.containsKey("orgId")){
                                entry.setValue(preResult.get("orgId").toString());
                                break;
                            } else{
                                log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + keyword);
                            }

                            continue;
                        } else{
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey());
                        }
                    }

                    break;
                case "special#auth_code#":
                    for(String s : sequences){
                        Integer index = Integer.parseInt(s);
                        if("com.alibaba.fastjson.JSONObject" == preExecutedResults.get(index).getClass().getName()) {
                            com.alibaba.fastjson.JSONObject preResult = (com.alibaba.fastjson.JSONObject) preExecutedResults.get(index);
                            if(preResult.containsKey("auth_code")){
                                entry.setValue(preResult.get("auth_code").toString());
                                break;
                            } else{
                                log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + keyword);
                            }

                            continue;
                        } else if("java.util.ArrayList" == preExecutedResults.get(index).getClass().getName()) {
                            ArrayList<JSONObject> tempList = (java.util.ArrayList)preExecutedResults.get(index);
                                for (JSONObject item : tempList){
                                    entry.setValue(item.get(keyword));
                                    break;
                                }
                        } else if("java.lang.String" == preExecutedResults.get(index).getClass().getName()) {
                            com.alibaba.fastjson.JSONObject jsonStringObject = JSONObject.parseObject(preExecutedResults.get(index).toString());
                            if(jsonStringObject.containsKey("auth_code")){
                                entry.setValue(jsonStringObject.get("auth_code").toString());
                                break;
                            }
                            continue;
                        } else {
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey());
                        }
                    }

                    break;
                case "random%string4%":
                    entry.setValue(getRandomString(4));
                    break;
                case "random%string8%":
                    entry.setValue(getRandomString(8));
                    break;
                case "random%string15%":
                    entry.setValue(getRandomString(15));
                    break;
                case "random%email%":
                    entry.setValue(getRandomString(8) + "@chanjettest.com");
                    break;
                case "special%startTm%":
                    Date currentDate = new Date();
//                    entry.setValue(currentDate.toInstant().minusSeconds(5).toEpochMilli());
                    currentDate.setTime(currentDate.getTime() - 5000);
                    entry.setValue(currentDate.getTime());
                    break;
                case "special%endTm%":
                    entry.setValue(new Date().getTime());
                    break;
                case "get#premobile#":
                    for(String s : sequences){
                        Integer index = Integer.parseInt(s);
                        if("com.alibaba.fastjson.JSONObject" == preExecutedResults.get(index).getClass().getName()) {
                            com.alibaba.fastjson.JSONObject preResult = (com.alibaba.fastjson.JSONObject) preExecutedResults.get(index);
                            if(preResult.containsKey(keyword)){
                                entry.setValue(preResult.get(keyword).toString());
                                break;
                            } else{
                                log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + "registCode" + " sequence: " + index);
                            }

                            continue;
                        } if("java.lang.String" == preExecutedResults.get(index).getClass().getName()) {
                            String preResult = (String) preExecutedResults.get(index);
                            com.alibaba.fastjson.JSONObject preJsonResult = com.alibaba.fastjson.JSONObject.parseObject(preResult);
                            if(preJsonResult.containsKey(keyword)){
                                entry.setValue(preJsonResult.get(keyword).toString());
                                break;
                            } else{
                                log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + "registCode" + " sequence: " + index);
                            }
                        } else{
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey());
                        }
                    }

                    break;
                default:
//                    log.debug("go to default logic in Method synonymousSubstitution for value:" + entry.getValue());
//                    if(!StringUtils.isEmptyOrSpace(String.valueOf(entry.getValue()))){
//                        String entryValue = String.valueOf(entry.getValue());
//                        switch (entryValue.getClass().getName()){
//                            case "com.alibaba.fastjson.JSONObject":
//                                break;
//                            case "java.lang.String":
//                                break;
//                            case "java.util.ArrayList":
//
//                                entry.setValue("[" + "]");
//                                break;
//                            case "com.alibaba.fastjson.JSONArray":
//                                break;
//                            default:
//                                break;
//
//                        }
//
//                    }
                    break;
            }
        }

        return jsonObject.toString();
    }

    private String generateSolidMobileNumber(){
        if(!StringUtils.isEmptyOrSpace(mobileNumber)){
            return mobileNumber;
        }

        return generateMobileNumber();
    }

    private String generateMobileNumber(){
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, 11);
        String fixLenthString = String.valueOf(pross);
        mobileNumber = fixLenthString.substring(1, 12).replace('.','1').replace('E', '1');
        return mobileNumber;
    }

    private static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return "test" + sb.toString();
    }

    protected String actionRequestParameter(String action, String parameter){
        if((StringUtils.isEmptyOrSpace(action)) || (StringUtils.isEmptyOrSpace(parameter))){
            return parameter;
        }

        switch (action.toLowerCase().trim()){
            case "md5":
                return Codec.hexMD5(parameter);
            case "decrypt":
                return getFromBase64(parameter);
            default:
                    break;
        }

        return parameter;
    }

    public List<Response> getExpectDataFromSuite(TestCaseNode testCaseNode){
        List<Response> responseParams = new ArrayList<Response>();
        for(Step testcase : testCaseNode.getTestSteps()) {
            Response response = testcase.getResponse();
            responseParams.add(response);
        }

        return responseParams;
    }


    public void initXmlRule() {
        digester = new Digester();
        //指定它不要用DTD验证XML文档的合法性——这是因为我们没有为XML文档定义DTD
        digester.setValidating(false);

        digester.addObjectCreate("TestCaseNode", TestCaseNode.class);
        digester.addSetProperties("TestCaseNode");
        digester.addObjectCreate("TestCaseNode/Step", Step.class);
        digester.addSetProperties("TestCaseNode/Step");
        digester.addSetNext("TestCaseNode/Step", "addTestSteps");

        digester.addObjectCreate("TestCaseNode/Step/Request", Request.class);
        digester.addSetProperties("TestCaseNode/Step/Request");
        digester.addSetNext("TestCaseNode/Step/Request", "setRequest");

        digester.addObjectCreate("TestCaseNode/Step/Request/Parameter", RequestParameter.class);
        digester.addSetProperties("TestCaseNode/Step/Request/Parameter");
        digester.addSetNext("TestCaseNode/Step/Request/Parameter", "addParameters");

        digester.addObjectCreate("TestCaseNode/Step/Response", Response.class);
        digester.addSetProperties("TestCaseNode/Step/Response");
        digester.addSetNext("TestCaseNode/Step/Response", "setResponse");

        digester.addObjectCreate("TestCaseNode/Step/Response/Expect/Parameter", ResponseParameter.class);
        digester.addSetProperties("TestCaseNode/Step/Response/Expect/Parameter");
        digester.addSetNext("TestCaseNode/Step/Response/Expect/Parameter", "addParameters");
    }

    @Test
    public void getSuite() throws Exception{

        InputStream stream = ClassLoader.getSystemResourceAsStream("httpTestCase-rule.xml");

//        Digester digester3 = DigesterLoader.createDigester(ClassLoader.getSystemResource("httpTestCase-rule1.xml"));
//        InputStream stream = ClassLoader.getSystemResourceAsStream("httpTestCase-rule.xml");

//        Properties sysPro = new Properties();
//        sysPro.load(stream);

        Document dom = null;

        try{

            StringBuffer content = new StringBuffer();
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(
                                            "/Users/haijia/code/iFramework/src/test/resources/testTestCase-rule1.xml"),
                                    "UTF-8"));
            String line = null;
            while((line = br.readLine()) != null ){
                content.append(line+"\n");
            }
            br.close();
            dom = DocumentHelper.parseText(content.toString());

        }catch(Exception e){
            log.error(e);
        }

//        java.io.InputStream jsource = ClassLoader.getSystemResourceAsStream("httpTestCase-rule.xml");
//        InputSource mySource = new InputSource(jsource);
//        mySource.setEncoding("utf-8");
//        Digester digester4 = DigesterLoader.createDigester(mySource);
//        Digester digester2 = DigesterLoader.createDigester(new InputSource(ClassLoader.getSystemResourceAsStream("httpTestCase-rule1.xml")));

//        Digester digester = new Digester();
//
//        digester.setValidating(false);
//
//        digester.addObjectCreate("MyTempCase", MyTempCase.class);
//
//        digester.addObjectCreate("MyTempCase/Request", Request.class);
//
//        MyTempCase myTempCase;
//
//        try{
//            myTempCase = (MyTempCase)digester.parse(ClassLoader.getSystemResourceAsStream("temp.xml"));
//        }catch (IOException e) {
//            throw new Exception(e);
//        } catch (SAXException e) {
//            throw new Exception(e);
//        } catch (Exception ex){
//            throw new Exception(ex);
//        }

//        java.io.InputStream xmlsource  = ClassLoader.getSystemResourceAsStream("testCases/Bizsvc/testGetBssProductInfoByGet.xml");

//        TestCaseNode suite = (TestCaseNode)digester4.parse(xmlsource);

//        for(Step testCase : suite.getTestSteps()){
//
//        }


    }

    public static String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        log.info(k);
        return k;
    }

    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
                log.info("getFromBase64:" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String inputStream2String(InputStream is) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }

    public static Properties sysPro;

    static{
        InputStream inputStream = null;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream("properties/variables.properties");
            sysPro = new Properties();
            sysPro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
//            System.exit(-1);
            log.error(e.getMessage());
        }
    }

}
