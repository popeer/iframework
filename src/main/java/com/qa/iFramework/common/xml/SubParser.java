package com.qa.iFramework.common.xml;

import com.qa.iFramework.common.xml.*;
import com.qa.iFramework.common.xml.Entity.Request;
import com.qa.iFramework.common.xml.Entity.RequestParameter;
import com.qa.iFramework.common.xml.Entity.Step;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 6/23/17.
 */
public class SubParser extends com.qa.iFramework.common.xml.Parser {

    private Object getParameter(String value) throws Exception{
            switch (value.toLowerCase()){
                case "map":
                    String[] maps = value.split(",");
                    HashMap<String, String> resultMap = new HashMap<String, String>();
                    for(String map : maps){
                        String[] keyValue = map.split(":");
                        resultMap.put(keyValue[0], keyValue[1]);
                    }
                    return resultMap;
                case "string":
                    return value;
                case "time":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    return sdf.parse(value);
                case "integer":
                    return Integer.valueOf(value);
                default:
                    log.error("not found matched type of request parameter!");
                    break;
            }

            return null;
    }

    public Map<String, Object> getJarRequestDataFromCase(Step testStep, List<Object> preExecutedResults) throws Exception {
        Map<String, Object> requestParams = new HashMap<String, Object>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            requestParams.put(
                    parameter.getName(),
                    getParameter(getParameterValueFromSysPro(parameter, preExecutedResults)));
        }
        return requestParams;
    }

    public Map<String, Object> getJarRequestDataFromExcel(Step testStep, List<Object> preExecutedResults, Map<String, String> excelParams)
            throws Exception{
        Map<String, Object> requestParams = new HashMap<String, Object>();
        Request request = testStep.getRequest();
        for(RequestParameter parameter : request.getParameters()){

            parameter.setValue(excelParams.get(parameter.getColName()));

            requestParams.put(
                    parameter.getName(),
                    getParameter(getParameterValueFromSysPro(parameter, preExecutedResults)));
        }
        return requestParams;
    }
}
