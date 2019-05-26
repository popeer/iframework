package com.qa.iFramework.common.xml;

import com.alibaba.fastjson.*;
import com.qa.iFramework.common.Util.*;
import com.qa.iFramework.common.processor.CommandEntity;
import com.qa.iFramework.common.xml.Entity.*;
import org.apache.commons.digester3.Digester;
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

    /**
     * @Auther: haijia
     * @Description: UI用例请求参数构造
     * @param parameter
     * @param preExecutedResults
     * @Date: 2019/3/14 14:10
     */
    public RequestParameter getRequestDataFromCaseForUI(List<Object> preExecutedResults,RequestParameter parameter) throws Exception{
        parameter.setValue(getParameterValueFromSysPro(parameter, preExecutedResults));
        return parameter;
    }

    /**
     * @Auther: haijia
     * @Description: UI用例请求参数构造，从excel获取测试数据
     * @param parameter
     * @param preExecutedResults
     * @param excelParams
     * @Date: 2019/3/14 14:09
     */
    public RequestParameter getRequestExcelFromCaseForUI(List<Object> preExecutedResults,RequestParameter parameter, Map<String, String> excelParams) throws Exception {
        String value = getParameterValueFromExcel(parameter, preExecutedResults, excelParams);
        parameter.setValue(actionRequestParameter(parameter.getAction(), value));
        return parameter;
    }

    /**
     * @Auther: haijia
     * @Description: jar接口请求参数构造
     * @param testStep
     * @param preExecutedResults
     * @Date: 2019/3/14 14:11
     */
    public List<RequestParameter> getRequestDataFromCaseForJar(Step testStep, List<Object> preExecutedResults) throws Exception{
        List<RequestParameter> requestParams = new ArrayList<RequestParameter>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter newparameter = getRequestParams(parameter, preExecutedResults, null);
            newparameter.setValue(actionRequestParameter(newparameter.getAction(), getParameterValueFromSysPro(newparameter, preExecutedResults)));
            requestParams.add(newparameter);
        }
        return requestParams;
    }

    /**
     * @Auther: haijia
     * @Description: jar接口请求参数构造，从excel获取测试数据
     * @param testStep
     * @param preExecutedResults
     * @param excelParams
     * @Date: 2019/3/14 14:08
     */
    public List<RequestParameter> getRequestExcelFromCaseForJar(Step testStep, List<Object> preExecutedResults, Map<String, String> excelParams) throws Exception{
        List<RequestParameter> requestParams = new ArrayList<RequestParameter>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter newparameter = getRequestParams(parameter, preExecutedResults, excelParams);
            newparameter.setValue(actionRequestParameter(newparameter.getAction(), getParameterValueFromExcel(newparameter, preExecutedResults, excelParams)));
            requestParams.add(newparameter);
        }
        return requestParams;
    }

    /**
     * @Auther: haijia
     * @Description: 构造请求参数对象
     * @param testStep
     * @param preExecutedResults
     * @Date: 2019/3/14 14:05
     */
    public Map<String, String> getRequestDataFromCase(Step testStep, List<Object> preExecutedResults) throws Exception{
        Map<String, String> requestParams = new HashMap<String, String>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            if(null == parameter.getBodyData()){
                RequestParameter newparameter = getRequestParams(parameter, preExecutedResults, null);
                requestParams.put(
                        newparameter.getName(),
                        actionRequestParameter(newparameter.getAction(), getParameterValueFromSysPro(newparameter, preExecutedResults)));
            } else {
                // bodydata可以以大括号{或中括号[开头
                requestParams = setBodyDataRequest(requestParams, parameter, preExecutedResults, null);
            }

        }
        return requestParams;
    }

    private Map<String, String> setBodyDataRequest(Map<String, String> requestParams,
                                                   RequestParameter newparameter,
                                                   List<Object> preExecutedResults,
                                                   Map<String, String> excelParams) throws IOException{
        // bodydata可以以大括号{或中括号[开头
        if (newparameter.getBodyData().startsWith("{")){
            newparameter.setValue(newparameter.getBodyData());
            if(StringUtils.isEmptyOrSpace(newparameter.getColName())){
                requestParams.put(Constants.bodyData, getParameterValueFromSysPro(newparameter,preExecutedResults));
            } else {
                requestParams.put(Constants.bodyData, getParameterValueFromExcel(newparameter, preExecutedResults, excelParams));
            }
        } else if (newparameter.getBodyData().startsWith("[")){
            newparameter.setValue(newparameter.getBodyData());
            if(StringUtils.isEmptyOrSpace(newparameter.getColName())){
                requestParams.put(Constants.bodyData, getParameterValueFromSysPro(newparameter,preExecutedResults));
            } else {
                requestParams.put(Constants.bodyData, getParameterValueFromExcel(newparameter, preExecutedResults, excelParams));
            }

        } else {
            log.error("TO DO LIST FOR Transfering TO MAP");
        }
        return requestParams;
    }

    /**
     * @Auther: haijia
     * @Description: 从excel读取数据构造请求参数对象
     * @param testStep
     * @param preExecutedResults
     * @param excelParams
     * @Date: 2019/3/14 14:06
     */
    public Map<String, String> getRequestDataFromExcel(Step testStep, List<Object> preExecutedResults, Map<String, String> excelParams)
            throws Exception{
        Map<String, String> requestParams = new HashMap<String, String>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            RequestParameter newparameter = getRequestParams(parameter, preExecutedResults, excelParams);
            if(null != newparameter.getBodyData()) {
                // bodydata可以以大括号{或中括号[开头
                requestParams = setBodyDataRequest(requestParams, newparameter, preExecutedResults, excelParams);
            } else{
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
        }
        return requestParams;
    }

    /**
     * 针对特殊参数(需要groovy脚本处理,
     * 针对name为参数值而value为空的情况)先做RequestParameter设置
     * */
    private RequestParameter getRequestParams(RequestParameter parameter, List<Object> preExecutedResults, Map<String, String> excelParams) throws Exception{
        RequestParameter resultParameter =  parameter;
        /*处理参数里name为参数值,value为空,同时ExchangeFlag为TRUE的情况 */
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

        /*处理参数值需要用groovy脚本处理的情况，需要groovy脚本路径、方法名都必须有值，构造函数参数可以为空 */
        if(!StringUtils.isEmptyOrSpace(parameter.getGroovyPath())
                && !StringUtils.isEmptyOrSpace(parameter.getGroovyMethodName())){
            //当excel参数里有groovy脚本的参数值定义时，对parameter赋值。注意：excel的groovy args就是列名列名
            if(null != excelParams){
                if(null != excelParams.get(parameter.getGroovyArgs())){
                    parameter.setGroovyArgs(excelParams.get(parameter.getGroovyArgs()));
                }
            }

            Object groovyResult = getGroovyResult(parameter.getGroovyPath(),
                    parameter.getGroovyArgs().split(","),
                    parameter.getGroovyMethodName());

            if(null != groovyResult){
                switch (groovyResult.getClass().getName()){
                    case "java.util.HashMap":
                        Map<String, String> mapheader = (Map<String, String>)groovyResult;
                        for (Map.Entry<String, String> entry : mapheader.entrySet()){
                            resultParameter.setValue(resultParameter.getValue() + ";" + entry.getKey() + ":" + entry.getValue());
                        }
                        break;
                    case "java.lang.String":
                        resultParameter.setValue(String.valueOf(groovyResult));
                        break;
                    default:
                        resultParameter.setValue(String.valueOf(groovyResult));
                        break;
                }

            }

            //获取到groovy结果就不需要再用excelParams来赋值了，故修改excelParams里参数值
            //当配置了数据源时，colname有值；当没有数据源时，colname没有值，应该取name为key
            if(null != excelParams) {
                excelParams.put(
                        StringUtils.isEmptyOrSpace(parameter.getColName()) ? parameter.getName() : parameter.getColName(),
                        resultParameter.getValue());
            }
        }

        return resultParameter;
    }

    /**
     * @Auther: haijia
     * @Description: 构造数据库操作类型的step的请求参数的sql语句，放入LinkedList<String>结构里，该结构存到公共对象commandEntity里。
     * @param step
     * @param commandEntity
     * @param preExecutedResults
     * @Date: 2019/3/14 14:13
     */
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

        // TODO: 1/29/18 暂时注销下面这行判断，因为当一个用例里有多个数据库源连接时候，需要更新commandEntity的数据库连接参数
//        if(!StringUtils.isEmptyOrSpace(step.getDburl()) && (StringUtils.isEmptyOrSpace(commandEntity.getDbURL()))){
        if(!StringUtils.isEmptyOrSpace(step.getDburl())){
            commandEntity.setDbURL(step.getDburl());
        }

//        if(!StringUtils.isEmptyOrSpace(step.getUid()) && (StringUtils.isEmptyOrSpace(commandEntity.getUid()))){
        if(!StringUtils.isEmptyOrSpace(step.getUid())) {
            commandEntity.setUid(step.getUid());
        }

//        if(!StringUtils.isEmptyOrSpace(step.getPwd()) && (StringUtils.isEmptyOrSpace(commandEntity.getPwd()))){
        if(!StringUtils.isEmptyOrSpace(step.getPwd())){
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
                        //使用上步结果中keyword对应的值来替换当前值里的{0}
                        parameter.setValue(parameter.getValue().replace("{0}", String.valueOf(tempJsonObject.get(parameter.getKeyword()))));
                    } else if(!StringUtils.isEmptyOrSpace(parameter.getSequence_type_resultPath_valuePath())){
                        //使用json path获取上步结果中特定值替换或赋值给当前值
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

    /**
     * @Auther: haijia
     * @Description: 从指定位置读取sql脚本
     * @param path
     * @Date: 2019/3/14 14:12
     */
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

    /**
     * @Auther: haijia
     * @Description:处理请求参数的value
     * @param parameter
     * @param preExecutedResults
     * @Date: 2019/3/14 13:58
     */
    protected String getParameterValueFromSysPro(RequestParameter parameter, List<Object> preExecutedResults) throws IOException{

        String value = parameter.getValue();

        // $$用来表明数据值从配置文件里取
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

        /*  ##用来表示从上面步骤里获取特定关键字的value.
            上面步骤里的结果可以是json,array, json string
            同时支持从配置文件里获取以#keyword#开头结尾对应的value.
        */
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
            value = getRandomString(8) + "@qatest.com";
        }

        if(value.equals("random%string8%")){
            value = getRandomString(8);
        }

        if(value.equals("random%string15%")){
            value = getRandomString(15);
        }

        if((null != sysDataPro) && (sysDataPro.containsKey(value))){
            value = sysDataPro.getProperty(value);
        }

        value = exchangeValue(parameter, preExecutedResults, value);

        value = logicalArchiveValue(parameter, preExecutedResults, value);

        value = storeParams(parameter, value);

        return value;
    }

    /**
     * @Auther: haijia
     * @Description:
     * 当offlineFlag=1，
    requestParameter的type为空或等于json时候，当前value转换成json，替换json里特定值
    requestParameter的type为空或等于array时候，当前value转换成array，获取数组第一个元素，替换第一个元素里特定值
     * 当offlineFlag=2，
    用regex替换RequestParameter的value里的自定义关键字。自定义关键字包括：random%mobile%、get%mobile%、random%string4%、
    random%string8%、random%string15%、random%email%、random%now%、random%guid%。
     * @param parameter
     * @param preExecutedResults
     * @param newValue
     * @Date: 2019/3/14 13:55
     */
    private String exchangeValue(RequestParameter parameter, List<Object> preExecutedResults, String newValue) {
        if(!StringUtils.isEmptyOrSpace(parameter.getOfflineFlag())){
            switch (parameter.getOfflineFlag()){
                case "1":
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
                    break;
                case "2":
                    if(StringUtils.isNotEmpty(parameter.getRegex())){
                        String[] regex = parameter.getRegex().split(",");
                        for(String s : regex){
                            newValue = newValue.replace(s, initCustomParam(s));
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return newValue;

    }

    /**
     * @Auther: haijia
     * @Description:
     * 生成自定义标签对应的值
     * @param regex
     * @Date: 2019/3/14 13:54
     */
    private String initCustomParam(String regex){
        switch (regex){
            case "random%mobile%":
                return generateMobileNumber();
            case "get%mobile%":
                return generateSolidMobileNumber();
            case "random%string4%":
                return getRandomString(4);
            case "random%string8%":
                return getRandomString(8);
            case "random%string15%":
                return getRandomString(15);
            case "random%email%":
                return getRandomString(8) + "@qatest.com";
            case "random%now%":
                return generateNow("yyyy-MM-dd");
            case "random%guid%":
                return generateGUID();
            default:
                break;
        }
        return regex;
    }

    /**
     * @Auther: haijia
     * @Description:从指定步骤里获取对象
     * 1. 从指定步骤里获取对象，
     * 当Sequence_type_keyword_key的type为string,把当前value转化成的jsonObject，其中key等于Sequence_type_keyword_key的key的旧值替换为上步结果的值。替换失败，记录日志;
     * 当Sequence_type_keyword_key的type为array,当前并不处理；
     * 当Sequence_type_keyword_key的type为json,把上步结果转换成jsonObject,用jsonObject里的keyword替换当前value里key的值；
     * 当Sequence_type_keyword_key的type为string_json,把上步结果转换成jsonObject,用jsonObject里的keyword对应的value赋给当前value；
     * 当Sequence_type_keyword_key的type为json_int,把上步结果转换成jsonObject,用jsonObject里的keyword对应的value赋给当前value；
     * 当Sequence_type_keyword_key的type为json_array,把上步结果转换成jsonObject,获取jsonObject里的keyword对应的value, 前后加上中括号，转成jsonArray，替换value里sequence_type_keyword_key的keyword对应的值；
     * 当Sequence_type_keyword_key的type为json_string, 把上步结果转换成jsonObject，用jsonObject里的keyword替换当前value里为key的值；
     * 当Sequence_type_keyword_key的type为sql_json,当前结果转换为ArrayList<HashMap<String,Object>>，获取第一个元素，该元素获取keyword对应的值，替换当前值里key对应的值；
     * 当Sequence_type_keyword_key的type为map，把上步结果转换成jsonObject，把当前value转成map<string,string>，用上步结果转换的jsonObject的keyword对应的值来替换map里key的值。
     * 2. 使用JsonPath从上步结果中取值
     * 对上步结果中的json结构的数据使用json path语法来获取对应值
     * Sequence_type_resultPath_valuePath的长度为4，其他长度不处理。
     * 若期望从上步结果里获取特定值，使用type="json"，用上步结果中resultPath对应的值来替换当前值里为valuePath的值；
     * 若期望用上步结果里特定值作为当前值，使用type="json_string"，上步结果中resultPath对应的值赋值给当前value。
     * 3. 从已存的参数里取值
     * Param_type_key长度为3，其他长度不处理。
     * Param_type_key的type="json",当前值转为jsonobject, 使用存储的请求参数里keyword对应的值替换jsonobject里Param_type_key里key对应的值。替换失败，记录日志。
     * Param_type_key的type="string",用已存参数里key对应的值替换当前值keyword对应的值。
     * Param_type_key的type="map",把当前值转成map,用已存参数里keyword对应的值替换当前值里key对应的值。
     * 4. 使用JsonPath语法查找当前结果里的值，被已存请求keyword对应的值替换
     * Param_keyword_keyPath长度为3
     * 5. 处理当前值是数组但没有以[]开头结尾的情况
     * @param parameter
     * @param preExecutedResults
     * @param value
     * @Date: 2019/3/14 11:26
     */
    private String logicalArchiveValue(RequestParameter parameter, List<Object> preExecutedResults, String value){
        String newValue = value;

        /*
            当前requestParameter的type为空或json时，把value转为json；
            当前requestParameter的type为空或obj时，记录日志；
            当前requestParameter的type为空或array时，把value转为array；
         */
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

            } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("map")) {

            }

            //通过关键字从上步结果中取值
            newValue = getPreStepValue(preExecutedResults, newValue, logicalValues, currentParaToJsonObject);
        }

        //bugfix: 参数值为null的话，在添加到http请求参数map对象时会报非法参数异常
        if(null == newValue){
            newValue = "";
        }

        //使用JsonPath从上步结果中取值
        if(!StringUtils.isEmptyOrSpace(parameter.getSequence_type_resultPath_valuePath())){
            newValue = processResultPathAndValuePath(parameter, preExecutedResults, newValue);
        }

        //从已存的参数里取值
        if(!StringUtils.isEmptyOrSpace(parameter.getParam_type_key())){
            newValue = processParamTypeKey(parameter, newValue);
        }

        //使用JsonPath语法查找当前结果里的值，被已存请求keyword对应的值替换
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
                                    //使用已存参数里keyword对应的值替换当前值里key对应的值
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

        //处理当前值是数组但没有以[]开头结尾的情况
        if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array") && (!newValue.startsWith("[") || !newValue.endsWith("]"))){
            newValue = "[" + newValue + "]";
        }
        return newValue;
    }

    public static String getPreStepValue(List<Object> preExecutedResults, String newValue, String[] logicalValues, JSONObject currentParaToJsonObject) {
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
                            String preString = preResult.toString();
                            JSONObject otherJson = JSONObject.parseObject(preString);
                            newValue = String.valueOf(otherJson.get(sequence_type_keyword_key[2]));
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
                            String preJsonString = preResult.toString();
                            JSONObject preStepRsultJson =  JSONObject.parseObject(preJsonString);
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
                        case "map":
                            if(JsonUtil.isJsonObject(preResult)){
                                JSONObject preStepJson = (JSONObject)preResult;
                                Map<String, String> newMap = StringUtils.getStringToMap(newValue);
                                newMap.put(sequence_type_keyword_key[3], String.valueOf(preStepJson.get(sequence_type_keyword_key[2])));
                                newValue = StringUtils.getMapToString(newMap);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return newValue;
    }

    /**
     * @Auther: haijia
     * @Description:从之前的请求参数里获取指定的值，来替换参数里的值，并返回完整的参数内容
     * Param_type_key长度为3，其他长度不处理。
     * Param_type_key的type="json",当前值转为jsonobject, 使用存储的请求参数里keyword对应的值替换jsonobject里Param_type_key里key对应的值。替换失败，记录日志。
     * Param_type_key的type="string",用已存参数里key对应的值替换当前值keyword对应的值。
     * Param_type_key的type="map",把当前值转成map,用已存参数里keyword对应的值替换当前值里key对应的值。
     * @param parameter
     * @param newValue
     * @Date: 1/29/18 14:36
     */
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
                        case "map":
                            Map<String, String> newMap = StringUtils.getStringToMap(newValue);
                            newMap.put(type_keyword[1], requestStoredParams.get(type_keyword[2]));
                            newValue = StringUtils.getMapToString(newMap);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return newValue;
    }

    /**
     * @Auther: haijia
     * @Description:对上步结果中的json结构的数据使用json path语法来获取对应值
     *  Sequence_type_resultPath_valuePath的长度为4，其他长度不处理。
     *  若期望从上步结果里获取特定值，使用type="json"，用上步结果中resultPath对应的值来替换当前值里为valuePath的值；
     *  若期望用上步结果里特定值作为当前值，使用type="json_string"，上步结果中resultPath对应的值赋值给当前value。
     * @param parameter
     * @param preExecutedResults
     * @param newValue
     * @Date: 2019/3/14 11:23
     */
    private String processResultPathAndValuePath(RequestParameter parameter, List<Object> preExecutedResults, String newValue) {
        String[] logicalValues = parameter.getSequence_type_resultPath_valuePath().split(",");
        JSONObject valueJson = new JSONObject();
        JSONArray valueJSONArray = new JSONArray();
        if (StringUtils.isEmptyOrSpace(parameter.getType()) || parameter.getType().toLowerCase().equals("json")) {
            valueJson = JSONObject.parseObject(newValue);
        }
        if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("obj")) {
            log.warn("TO BE DONE FOR Parameter Type = obj in Parse.java");
        } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("array")) {
            JSONArray jsonArray = JSONArray.parseArray(newValue);
            valueJson = (JSONObject) jsonArray.get(0);
        } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("string")) {

        } else if (!StringUtils.isEmptyOrSpace(parameter.getType()) && parameter.getType().toLowerCase().equals("arrayjson")){
            valueJSONArray = JSONArray.parseArray(newValue);
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
                        case "jsonarray":
                            resultJson = (JSONObject)preResult;

                            if(null != resultJson && JSONPath.contains(valueJSONArray, sequence_type_resultPath_keyword_valuePath_key[3])
                                    && JSONPath.contains(resultJson, sequence_type_resultPath_keyword_valuePath_key[2])){

                                JSONPath.set(
                                        valueJSONArray,
                                        sequence_type_resultPath_keyword_valuePath_key[3],
                                        JSONPath.eval(resultJson, sequence_type_resultPath_keyword_valuePath_key[2]).toString());
                                newValue = valueJSONArray.toJSONString();
                            }
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
                        case "json_int":
                            resultJson = (JSONObject)preResult;

                            if(null != resultJson && JSONPath.contains(valueJson, sequence_type_resultPath_keyword_valuePath_key[3])
                                    && JSONPath.contains(resultJson, sequence_type_resultPath_keyword_valuePath_key[2])){

                                JSONPath.set(
                                        valueJson,
                                        sequence_type_resultPath_keyword_valuePath_key[3],
                                        Long.valueOf(JSONPath.eval(resultJson, sequence_type_resultPath_keyword_valuePath_key[2]).toString()));
                                newValue = valueJson.toJSONString();
                            }
                            break;
                        case "array_int":
                            JSONArray arrayPreResult = (JSONArray)preResult;
                            resultJson = arrayPreResult.getJSONObject(0);
                            if(null != resultJson && JSONPath.contains(valueJson, sequence_type_resultPath_keyword_valuePath_key[3])
                                    && JSONPath.contains(resultJson, sequence_type_resultPath_keyword_valuePath_key[2])){

                                JSONPath.set(
                                        valueJson,
                                        sequence_type_resultPath_keyword_valuePath_key[3],
                                        Long.valueOf(JSONPath.eval(resultJson, sequence_type_resultPath_keyword_valuePath_key[2]).toString()));
                                newValue = valueJson.toJSONString();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return newValue;
    }

    /**
     * @Auther: haijia
     * @Description:
     * 功能概述：存储当前参数
     * 当前参数里只需存一个值时：
     * type为string时，使用storeParam标签的值替换当前值里内容为regex标签的值。
     * type为map时，把当前值转为map，取map里key为storeParam的值，赋值给当前值。
     * type为obj时，什么都不做。
     * type为array时，什么都不做。
     * type为json时，把当前值转成jsonObject，获取该jsonObject里key为storeParam的值，赋值给当前值。
     * 当StoreParam标签等于"allstring"时，把alias替换storeParam的值，
     * 再把alias作为作为key，当前值为value，存入已存参数里，结构为Map<String, String>
     *
     * 当前参数里只需存多个值时：
     * 针对接口用例storedParam的长度为5，把当前值转成jsonobject，当type为json,使用jsonPath 通过 path 获取jsonObject里的值，alias做为key，存入map
     * 针对UI用例storedParam的长度为2，把当前值转成map，获取key的value，alias作为key, 存入map。
     * 注意事项：设置storeParam，就要设置alias

     * @param parameter 请求参数对象
     * @param value 当前请求参数的值
     * @Date: 2019/3/14 11:03
     */
    private String storeParams(RequestParameter parameter, String value){
        String newValue = value;
        if(!StringUtils.isEmptyOrSpace(parameter.getStoreParam())){
            String[] storeParams = parameter.getStoreParam().split(",");
            //处理当一个RequestParameter只需要存一个StoreParam时
            if(1 == storeParams.length){
                switch (parameter.getStoreParam().toLowerCase().trim()) {
                    case "allstring":
                        if (StringUtils.isEmptyOrSpace(parameter.getAlias())) {
                            log.warn("设置storeParam，就要设置alias for Parameter " +
                                    (StringUtils.isEmptyOrSpace(parameter.getName()) ? parameter.getColName() : parameter.getName()));
                        } else {
                            parameter.setStoreParam(parameter.getAlias());
                        }
                        break;
                    default:
                        // 如果指定了StoreParam, 就必须指定type
                        if (!StringUtils.isEmptyOrSpace(parameter.getType())) {
                            switch (parameter.getType().toLowerCase()) {
                                case "string":
                                    newValue = newValue.replace(parameter.getRegex(), initCustomParam(parameter.getStoreParam()));
                                    break;
                                case "map":
                                    newValue = StringUtils.getStringToMap(newValue).get(parameter.getStoreParam());
                                    break;
                                case "obj":
                                    break;
                                case "array":
                                    break;
                                case "json":
                                    JSONObject requestJsonObject = JSONObject.parseObject(newValue);
                                    newValue = (String) requestJsonObject.get(parameter.getStoreParam());
                                    break;
                                default:
                                    log.warn("no matched RequestParameter.getType:" + parameter.getType());
                            }
                        }
                }

                //再把alias作为作为key，当前值为value，存入已存参数里，结构为Map<String, String>
                if (!requestStoredParams.containsKey(parameter.getAlias())) {
                    requestStoredParams.put(parameter.getAlias(), newValue);
                } else {
                    log.warn("requestStoredParams already contain this key:" + parameter.getAlias() + " in Parse.java");
                }
            } else if (1 < storeParams.length){
                for(String storedParam : storeParams) {
                    String[] ArraysForcol_name_alias_path_type = storedParam.split("___");
                    String alias = "";
                    String name = "";
                    String type = "";
                    String storeP = "";
                    //接口用例要配5个参数，UI用例要配4个
                    switch (ArraysForcol_name_alias_path_type.length){
                        case 5:
                            //针对接口用例，接口用例使用json接口表达参数
                            String col = ArraysForcol_name_alias_path_type[0];
                            name = ArraysForcol_name_alias_path_type[1];
                            alias = ArraysForcol_name_alias_path_type[2];
                            String path = ArraysForcol_name_alias_path_type[3];
                            type = ArraysForcol_name_alias_path_type[4];

                            JSONObject valueJsonObject = JSONObject.parseObject(newValue);

                            switch (type.toLowerCase().trim()){
                                case "json":
                                    if(null != valueJsonObject && JSONPath.contains(valueJsonObject, path)){
                                        storeP = String.valueOf(JSONPath.eval(valueJsonObject, path));
                                    }
                                    break;
                                case "json_string":

                                    break;
                                case "array":
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            //针对UI用例，UI用例使用MAP结构表达参数
                            name = ArraysForcol_name_alias_path_type[0];
                            alias = ArraysForcol_name_alias_path_type[1];

                            Map<String, String> valueMap = StringUtils.getStringToMap(newValue);
                            storeP = valueMap.get(name);
                            break;
                    }

                    if (!requestStoredParams.containsKey(alias)) {
                        requestStoredParams.put(alias, storeP);
                    } else {
                        log.warn("requestStoredParams already contain this key:" + alias + " in Line 787 Parse.java");
                    }
                }
            } else {
                log.info("Do nothing when storeParams Length is 0");
            }
        }

        return value;
    }

    /**
     * @Auther: haijia
     * @Description: 从excel数据源里获取值，并构造最终期望参数值
     * @param parameter
     * @param preExecutedResults
     * @param excelParams
     * @Date: 2019/3/14 11:22
     */
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
            colValue = getRandomString(8) + "@qatest.com";
        }

        if(colValue.equals("random%string8%")){
            colValue = getRandomString(8);
        }

        colValue = exchangeValue(parameter, preExecutedResults, colValue);

        colValue = logicalArchiveValue(parameter, preExecutedResults, colValue);

        colValue = storeParams(parameter, colValue);

        return colValue;
    }

    /**
     * @Auther: haijia
     * @Description:循环递归json，替换特定值
     * @param jsonObject
     * @param preExecutedResults
     * @param sequence
     * @param keyword
     * @Date: 2019/3/14 11:21
     */
    private String synonymousSubstitution(JSONObject jsonObject, List<Object> preExecutedResults, String sequence, String keyword){

        if(null == jsonObject){
            return "";
        }

        String[] sequences = new String[0];
        if(!StringUtils.isEmptyOrSpace(sequence)){
            sequences = sequence.split("_");
        }

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if(String.valueOf(entry.getValue()).startsWith("{") && String.valueOf(entry.getValue()).endsWith("}")){
                try{
//                    entry.setValue(synonymousSubstitution(JSONObject.parseObject(String.valueOf(entry.getValue())), preExecutedResults, sequence, keyword));
                    String valueString = synonymousSubstitution(JSONObject.parseObject(String.valueOf(entry.getValue())), preExecutedResults, sequence, keyword);

                    if(JsonUtil.isJsonArray(valueString)){
                        JSONArray jsonArray = JSON.parseArray(valueString);
                        if(null != jsonArray && 0 < jsonArray.size()){
                            entry.setValue(jsonArray);
                        }
                    } else if(JsonUtil.isJsonObject(valueString)){
                        JSONObject subJsonObj = JSON.parseObject(valueString);
                        if(null != subJsonObj){
                            entry.setValue(subJsonObj);
                        }
                    } else{
                        entry.setValue(valueString);
                    }
                } catch (JSONException ex){

                }
            }

            generateCustomParam(preExecutedResults, keyword, sequences, entry);
        }

        return jsonObject.toJSONString();
    }

    /**
     * @Auther: haijia
     * @Description:
     * 处理框架自定义标签
     * @param preExecutedResults
     * @param keyword
     * @param sequences
     * @param entry
     * @Date: 2019/3/14 13:50
     */
    private void generateCustomParam(List<Object> preExecutedResults, String keyword, String[] sequences, Map.Entry<String, Object> entry) {
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
                        JSONObject preResult = (JSONObject) preExecutedResults.get(index);
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
                        JSONObject preResult = (JSONObject) preExecutedResults.get(index);
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
                        JSONObject preResult = (JSONObject) preExecutedResults.get(index);
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
                        JSONObject preResult = (JSONObject) preExecutedResults.get(index);
                        if(preResult.containsKey("auth_code")){
                            entry.setValue(preResult.get("auth_code").toString());
                            break;
                        } else{
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + keyword);
                        }

                        continue;
                    } else if("java.util.ArrayList" == preExecutedResults.get(index).getClass().getName()) {
                        ArrayList<JSONObject> tempList = (ArrayList)preExecutedResults.get(index);
                        for (JSONObject item : tempList){
                            entry.setValue(item.get(keyword));
                            break;
                        }
                    } else if("java.lang.String" == preExecutedResults.get(index).getClass().getName()) {
                        JSONObject jsonStringObject = JSONObject.parseObject(preExecutedResults.get(index).toString());
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
                entry.setValue(getRandomString(8) + "@qatest.com");
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
            case "special%now%":
                Date current = new Date();
                current.setTime(current.getTime() + 5000);
                entry.setValue(current);
                break;
            case "get#premobile#":
                for(String s : sequences){
                    Integer index = Integer.parseInt(s);
                    if("com.alibaba.fastjson.JSONObject" == preExecutedResults.get(index).getClass().getName()) {
                        JSONObject preResult = (JSONObject) preExecutedResults.get(index);
                        if(preResult.containsKey(keyword)){
                            entry.setValue(preResult.get(keyword).toString());
                            break;
                        } else{
                            log.debug("Attention: Request Parameter Not Found Out " + entry.getKey() + " in preResult for keyword " + "registCode" + " sequence: " + index);
                        }

                        continue;
                    } if("java.lang.String" == preExecutedResults.get(index).getClass().getName()) {
                        String preResult = (String) preExecutedResults.get(index);
                        JSONObject preJsonResult = JSONObject.parseObject(preResult);
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
            case "random%now%":
                entry.setValue(generateNow("yyyy-MM-dd"));
                break;
            case "random%guid%":
                entry.setValue(generateGUID());
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

    /**
     * @Auther: haijia
     * @Description:
     * 调用groovy脚本，返回结果
     * @param path
     * @param args
     * @param method
     * @Date: 2019/3/14 13:52
     */
    public static Object getGroovyResult(String path, String[] args, String method) throws Exception{
        GroovyUtil groovyUtil = new GroovyUtil();
        return groovyUtil.loadCustomGroovyScript(path, args, method);
    }

    private static String generateGUID(){
        return java.util.UUID.randomUUID().toString();
    }

    private static String generateNow(String format){
        return DateUtil.format(new Date(), format);
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
            case "urlencode":
                return Codec.urlEncoder(parameter);
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

    /**
     * @Auther: haijia
     * @Description:
     * 通过digester组件把xml测试用例格式规则定义
     * @Date: 2019/3/14 13:53
     */
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

    }

//    public String decrypt(String encText){
//        if(StringUtils.isEmptyOrSpace(encText)){
//            return encText;
//        }
//
//        try {
//            HttpClient httpClient = new HttpClient();
//            PostMethod method = new PostMethod("http://cenc.chanapp.chanjet.com/special/v1/product/decodeByChallengeCode");
//            method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            method.getParams().setParameter("Accept", "text/plain");
//            method.addParameter("appKey", "");
//            method.addParameter("encText", encText);
//            method.addParameter("clientChallengeCode", "");
//
//            httpClient.executeMethod(method);
//            String body = method.getResponseBodyAsString();
//            log.info("body=" + body);
//
//            JSONObject resultObject = JSONObject.parseObject(body);
//            return resultObject.get("value").toString();
//        } catch (Exception ex){
//            log.error("Fail to descrypt text:" + encText);
//        }
//        return "";
//    }

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
