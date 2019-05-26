package com.qa.iFramework.common.impl;

import com.qa.iFramework.common.IExecutor;
import com.qa.iFramework.common.Util.ReflectUtil;
import com.qa.iFramework.common.Util.StringUtils;
import com.qa.iFramework.common.xml.Entity.Request;
import com.qa.iFramework.common.xml.Entity.RequestParameter;
import com.qa.iFramework.common.xml.Entity.Step;
import com.qa.iFramework.common.xml.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by haijia on 3/5/19.
 */
public class UiExecutor implements IExecutor {
    private static Logger log = LogManager.getLogger(UiExecutor.class);

    public Object Execute(Step step, List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception {
        if (StringUtils.isEmptyOrSpace(step.getClsName())) {
            log.error("UI Step miss the ClassName!");
            return null;
        }

        if(StringUtils.isEmptyOrSpace(step.getMethod()) || null == step.getParameterType()){
            log.error("UI Step miss the Method or ParameterType in Step Name: " + step.getName() + " in Node Name: " + step.getCaseNodeName() );
            return null;
        }

        List<RequestParameter> requestParams = new ArrayList<RequestParameter>();
        Request request = step.getRequest();
        for(RequestParameter parameter : request.getParameters()){
            if(null != entry && StringUtils.isNotEmpty(parameter.getColName())){
                requestParams.add(parser.getRequestExcelFromCaseForUI(preExecutedResults, parameter, entry));
            } else{
                requestParams.add(parser.getRequestDataFromCaseForUI(preExecutedResults, parameter));
            }
        }

        Object[] uiParamArray = ReflectUtil.InitMethodParams(step, requestParams);
        ArrayList<Object> params = new ArrayList<Object>();
        for(Object obj : uiParamArray){
            params.add(obj);
        }
        Object[] paramArray = new Object[params.size()];
        params.toArray(paramArray);
        Class<?> clsInstance = Class.forName(step.getClsName());
        Object result = ReflectUtil.InitAndInvoke(step, preExecutedResults, paramArray, clsInstance);
        log.info("result is: " + result);
        return result;
    }

}
