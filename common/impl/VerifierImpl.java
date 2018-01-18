package com.chanjet.chanapp.qa.iFramework.common.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.Entity.ErrorInfo;
import com.chanjet.chanapp.qa.iFramework.Entity.Result;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.chanjet.chanapp.qa.iFramework.common.Util.*;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.ResponseParameter;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by haijia on 12/1/16.
 */
public class VerifierImpl implements IVerifier {
    private static Logger log = LogManager.getLogger(VerifierImpl.class);

    /**
     * @Auther: haijia
     * @Description:
     * @param result 该步骤的对象
     * @param responseParams 期望值组
     * @param entry excel数据源的期望值
     * @param preExecutedResults 该步之前步骤的结果集合
     * @Date: 1/4/18 16:04
     */
    public Result VerifyResult(Step result, List<ResponseParameter> responseParams, Map<String, String> entry, List<Object> preExecutedResults) throws Exception{
        Result executeResult = new Result();
        JSONObject resultObject = new JSONObject();
        executeResult.setDateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"));

        //判断环境，是测试环境，还是集测环境，还是模拟\预发环境，还是线上环境，还是类Thrift的jar包
        if(!StringUtils.isEmptyOrSpace(result.getUrl())){
            if( -1 != result.getUrl().replace("//", "").indexOf("/")){
                executeResult.setInterfaceName(result.getUrl().substring(result.getUrl().replace("//", "").indexOf("/")));
            }

            if(result.getUrl().toUpperCase().contains("INTE-")){
                executeResult.setEnvironment("INTE");
            } else if(result.getUrl().toUpperCase().contains("MONI-")){
                executeResult.setEnvironment("MONI");
            } else if(result.getUrl().toUpperCase().contains("TEST-")){
                executeResult.setEnvironment("TEST");
            } else {
                executeResult.setEnvironment("ONLINE");
            }
        } else if (!StringUtils.isEmptyOrSpace(result.getMethod())){
            executeResult.setInterfaceName(result.getMethod());
            executeResult.setEnvironment("JAR-TEST");
        }

        executeResult.setStep_name(result.getName());
        executeResult.setNode_name(result.getCaseNodeName());
        // TODO: 1/4/18 设置执行人和该步方法
        executeResult.setOperator("");
        executeResult.setModule("");

        if (null == result || null == result.getResult()) {
            // 判断是否期望访问为空值
            if (!StringUtils.isEmptyOrSpace(result.getVoidReturn()) && "true".equalsIgnoreCase(result.getVoidReturn().trim().toLowerCase())){
                executeResult.setResult(true);
                executeResult.setError(new ErrorInfo(200, "success", result));
                return executeResult;
            }

            executeResult.setError(new ErrorInfo(ExceptionCodes.NoResult, "No executed result!", " {} "));
            executeResult.setResult(false);

            return executeResult;
        }

//        if(null == responseParams){
//            executeResult.setResult(false);
//            executeResult.setError(new ErrorInfo(1004, "expect null result!"));
//            return executeResult;
//        }

        try {

            if (null != result.getName() || !StringUtils.isEmptyOrSpace(result.getName())) {
                executeResult.setStep_name(result.getName());
            }

            //根据返回结果类型jsonObject/arrayList/string/map, 构造实际结果resultObject
            switch (result.getResult().getClass().getName()) {
                case "com.alibaba.fastjson.JSONObject":
                    resultObject = (com.alibaba.fastjson.JSONObject) (result.getResult());
                    break;
                case "java.util.ArrayList":
                    ArrayList arrayList = (java.util.ArrayList) result.getResult();
                    resultObject = (com.alibaba.fastjson.JSONObject) arrayList.get(0);
                    break;
                case "java.lang.String":
                    String resultString = String.valueOf(result.getResult());
                    try {
                        if (!StringUtils.isEmptyOrSpace(result.getType()) && "jar".equalsIgnoreCase(result.getType().trim())){
                            resultObject.put("jar", resultString);
                        } else{
                            resultObject = JSONObject.parseObject(resultString);
                        }
                    } catch (JSONException ex) {
                        resultObject = JSONObject.parseObject("\"str\":\"" + resultString + "\"");
                    } catch (Exception ex) {
                        executeResult.setError(new ErrorInfo(1006, "wrong result!", " return wrong detail: " + resultString));
                        executeResult.setResult(false);
                        return executeResult;
                    }

                    break;
                case "java.util.LinkedHashMap":
                    LinkedHashMap resultLinkMap = (java.util.LinkedHashMap)result.getResult();
                    resultObject = new JSONObject(resultLinkMap);
                    break;
                default:
                    executeResult.setError(new ErrorInfo(1005, "Not support this class!", " not support this Class " + result.getResult().getClass().getName()));
                    executeResult.setResult(true);
                    return executeResult;
            }

            if (null == resultObject) {
                executeResult.setError(new ErrorInfo(1003, "No executed result!", " {} "));
                executeResult.setResult(true);

                return executeResult;
            }

//        if(resultObject.toJSONString().contains("false")){
//            executeResult.setResult(false);
//            executeResult.setError(new ErrorInfo(1001, "the result is failed!", resultObject.toJSONString()));
//            return executeResult;
//        }

            //构造期望结果newResponsePamams
            List<ResponseParameter> newResponsePamams = initResponseParameter(entry, responseParams);

            //遍历比较每个期望结果和实际结果
            for (ResponseParameter expectCompare : newResponsePamams) {
                String path = expectCompare.getPath();
                String[] jsonPath = path.split(",");
                if (0 < jsonPath.length) {
                    JSONObject comparingJSONObject = resultObject;
                    Object comparingObject = new Object();
                    comparingObject = "{}";
                    for (String step : jsonPath) {
                        String[] sign = step.trim().split("___");
                        if (2 == sign.length) {
                            switch (sign[0].toLowerCase()) {
                                case "obj":
                                    comparingJSONObject = comparingJSONObject.getJSONObject(sign[1]);
                                    break;
                                case "array":
                                    comparingObject = comparingJSONObject.getJSONArray(sign[1]);
                                    break;
                                case "string":
                                    comparingObject = comparingJSONObject.get(sign[1]);
                                    break;
                                case "int":
                                    comparingObject = comparingJSONObject.get(sign[1]);
                                    break;
                                default:
                                    break;
                            }
                        } //如果是数组，允许指定获取数组内第几个元素
                        else if ((3 == sign.length) && (sign[0].toLowerCase().equals("array"))) {
                            Object jsonObject = comparingJSONObject.getJSONArray(sign[1]).get(Integer.parseInt(sign[2]));
                            comparingJSONObject = (JSONObject)jsonObject;
                        } else {
                            executeResult.setError(new ErrorInfo(1002, "Expected path number of _ sign is 2 or 3!", comparingJSONObject));
                            executeResult.setResult(false);
                        }
                    }

                    //从上步结果集中获取指定步骤的结果作为期望结果expectCompare,注：目前只能获取单步结果，暂不支持获取多个结果
                    if(StringUtils.isEmpty(expectCompare.getValue()) && StringUtils.isNotEmpty(expectCompare.getSequence_type_keyword())){
//                        MyAssert.assertNotNull(comparingObject, "expect result is " + expectCompare);
//                        MyAssert.assertNotNull(comparingJSONObject, "expect result is " + expectCompare);
                        String[] sequence_type_keyword = expectCompare.getSequence_type_keyword().split("___");
                        if(3 == sequence_type_keyword.length){
                            String sequence = sequence_type_keyword[0];
                            String type = sequence_type_keyword[1];
                            String keyword = sequence_type_keyword[2];
                            Object preResult = preExecutedResults.get(Integer.parseInt(sequence));
                            switch (type.toLowerCase().trim()){
                                case "string":
                                    expectCompare.setValue(String.valueOf(preResult));
                                    break;
                                case "json":
                                    expectCompare.setValue(JSONObject.parseObject(preResult.toString()).getString(keyword));
                                    break;
                                case "int":
                                    break;
                                case "obj":
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    //期望结果与实际结果比较
                    switch (expectCompare.getAction().toLowerCase()) {
                        case "equal":
                            if (expectCompare.getType().toLowerCase().equals("obj")) {
                                MyAssert.assertEquals(
                                        comparingObject,
                                        expectCompare.getValue().equals("null") ? null : expectCompare.getValue(),
                                        "obj type is not equal");
                            } else if (expectCompare.getType().toLowerCase().equals("string")) {
                                MyAssert.assertEquals(
                                        comparingObject.toString(),
                                        expectCompare.getValue(),
                                        "string type is not equal");
                            } else if (expectCompare.getType().toLowerCase().equals("array")) {
                                MyAssert.assertEquals(
                                        ((JSONArray) comparingObject).size(),
                                        Integer.parseInt(expectCompare.getValue()),
                                        "array type is not equal");
                            } else if (expectCompare.getType().toLowerCase().equals("int")){
                                MyAssert.assertEquals(
                                        Integer.parseInt(comparingObject.toString()),
                                        Integer.parseInt(expectCompare.getValue()),
                                        "Integer type is not equal");
                            }

                            break;
                        case "notnull":
                            if (expectCompare.getType().toLowerCase().equals("obj")) {
                                MyAssert.assertNotNull(comparingJSONObject,
                                        "obj type is null");
                            } else if (expectCompare.getType().toLowerCase().equals("string")) {
                                MyAssert.assertNotNull(comparingObject,
                                        "string type is null");
                            } else if (expectCompare.getType().toLowerCase().equals("array")) {
                                MyAssert.assertNotNull(
                                        comparingObject,
                                        "array type is null");
                            }
                            break;
                        case "contain":
                            if (expectCompare.getType().toLowerCase().equals("array")) {
                                if (comparingObject.getClass().getName().equals("com.alibaba.fastjson.JSONArray")) {
                                    MyAssert.assertTrue(
                                            expectCompare.getValue().toLowerCase().trim().equals(comparingObject.toString().trim().toLowerCase()),
                                            "array type Contain is failed");
                                }
                            } else if (expectCompare.getType().toLowerCase().equals("obj")) {
                                if (comparingObject.getClass().getName().equals("com.alibaba.fastjson.JSONArray")) {
                                    JSONArray jsonArray = (JSONArray) comparingObject;
                                    MyAssert.assertTrue(jsonArray.contains(JSONObject.parse(expectCompare.getValue())),
                                            "obj type Contain is failed");
                                }
                            }
                            break;
                        case "containitem":
                            if (expectCompare.getType().toLowerCase().equals("string")) {
                                if (comparingObject.getClass().getName().equals("com.alibaba.fastjson.JSONObject")) {
                                    if(((JSONObject)comparingObject).containsKey(expectCompare.getValue().replace("#", ""))){
                                        MyAssert.assertEquals(
                                                ((JSONObject)comparingObject).get(expectCompare.getKeyword()),
                                                expectCompare.getValue(),
                                                "Contain Item is failed");
                                    }
                                }
                            } else if(expectCompare.getType().toLowerCase().equals("obj")){
                                JSONObject expectJsonObject = JSONObject.parseObject(expectCompare.getValue());

                                for (Map.Entry<String, Object> entity : expectJsonObject.entrySet()) {
                                    if (comparingJSONObject.containsKey(entity.getKey())) {
                                        MyAssert.assertEquals(
                                                comparingJSONObject.get(entity.getKey()),
                                                entity.getValue(),
                                                comparingJSONObject.get(entity.getKey()) + " NOT equal with " + entity.getValue());
                                    } else {
                                        MyAssert.fail("actualObject NOT contain " + entity.getValue(), 1008);
                                    }

                                }

                            }
                            break;
                        default:
                            break;
                    }

                } else {
                    //log error
                    executeResult.setError(new ErrorInfo(1001, "No expected path!", "{}"));
                    executeResult.setResult(false);
                    return executeResult;
                }
            }


        } catch (MyCustomerException ex){
            log.error("Verify occur a MyCustomerException!");
            executeResult.setResult(false);
            executeResult.setError(new ErrorInfo(ex.getErrorCode(), ex.getCustomInfo(), result.getResult()));
            return executeResult;
        } catch (Exception ex){
            log.error("Verify occur an Exception!");
            executeResult.setResult(false);
            executeResult.setError(new ErrorInfo(5000, ex.getMessage(), result.getResult()));
            return executeResult;
        }

        executeResult.setResult(true);
        executeResult.setError(new ErrorInfo(200, "success", resultObject));
        return executeResult;

    }

    /**
     * @Auther: haijia
     * @Description:构建期望结果的定义
     * @param entry
     * @param responseParameters
     * @Date: 1/8/18 15:22
     */
    private List<ResponseParameter> initResponseParameter(Map<String, String> entry, List<ResponseParameter> responseParameters){
        List<ResponseParameter> returnParams = new ArrayList<ResponseParameter>();
        for(ResponseParameter responseParameter : responseParameters){
            if(StringUtils.isEmptyOrSpace(responseParameter.getType())){
                responseParameter.setType("");
            }

            if(StringUtils.isEmptyOrSpace(responseParameter.getValue())){
                responseParameter.setValue("");
            }

            if(StringUtils.isEmptyOrSpace(responseParameter.getAction())){
                responseParameter.setAction("");
            }

            if(StringUtils.isEmptyOrSpace(responseParameter.getPath())){
                responseParameter.setPath("");
            }

            if(StringUtils.isEmptyOrSpace(responseParameter.getSequence_type_keyword())){
                responseParameter.setSequence_type_keyword("");
            }

            //构建从excel数据源获取期望结果的比较定义
            if(!StringUtils.isEmptyOrSpace(responseParameter.getExcelFlag()) && responseParameter.getExcelFlag().equals("true")){
                if(entry.containsKey(responseParameter.getExcelExpectParameterColName())){
                    try{
                        JSONArray responseArray = JSONArray.parseArray(entry.get(responseParameter.getExcelExpectParameterColName()));
                        if(responseArray.size() > 0){
                            for(int i = 0; i < responseArray.size(); i++){
                                JSONObject jsonObject = responseArray.getJSONObject(i);
                                ResponseParameter newresponseParameter = new ResponseParameter();
                                newresponseParameter.setPath(jsonObject.getString("path"));
                                newresponseParameter.setValue(jsonObject.getString("value"));
                                newresponseParameter.setAction(jsonObject.getString("action"));
                                newresponseParameter.setType(jsonObject.getString("type"));
                                newresponseParameter.setSequence_type_keyword(jsonObject.getString("sequence_type_keyword"));
                                newresponseParameter.setExcelFlag(responseParameter.getExcelFlag());
                                newresponseParameter.setExcelExpectParameterColName(responseParameter.getExcelExpectParameterColName());
                                returnParams.add(newresponseParameter);
                            }
                        }

                    } catch (Exception ex){
                        log.error("initResponseParameter fail!!!! for :" + responseParameter);
                    }
                } else{
                    log.error("Miss Correct Col Name for ResponseParameter : " + responseParameter.getExcelExpectParameterColName());
                }
            } else{
                returnParams.add(responseParameter);
            }
        }
        return returnParams;
    }
}
