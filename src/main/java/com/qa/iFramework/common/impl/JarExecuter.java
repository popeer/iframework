package com.qa.iFramework.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.common.IExecutor;
import com.qa.iFramework.common.Util.MyCustomerException;
import com.qa.iFramework.common.Util.ReflectUtil;
import com.qa.iFramework.common.Util.StringUtils;
import com.qa.iFramework.common.xml.Entity.RequestParameter;
import com.qa.iFramework.common.xml.Entity.Step;
import com.qa.iFramework.common.xml.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by haijia on 8/1/17.
 */
public class JarExecuter implements IExecutor {
    private static Logger log = LogManager.getLogger(JarExecuter.class);

    public Object Execute(Step step, List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception{
        if(StringUtils.isEmptyOrSpace(step.getClsName())){
            log.error("Jar Step miss the ClassName!");
            return null;
        }

        List<RequestParameter> requestParams = null;

        if(null == entry){
            requestParams = parser.getRequestDataFromCaseForJar(step, preExecutedResults);
        } else {
            requestParams = parser.getRequestExcelFromCaseForJar(step, preExecutedResults, entry);
        }

        return ReflectUtil.ExecuteReflect(step, preExecutedResults, requestParams);

    }

    public static void main(String[] args) throws Exception{
        try {
            String className = "com.qa.iFramework.common.Util.JsonUtil";//这里注意了，是：包名.类名，只写类名会出问题的哦
            Class<?> testClass = Class.forName(className);

            Method saddMethod2 = testClass.getMethod("isJsonString", new Class[]{Object.class});

//            String result = saddMethod2.invoke(null,new Object[]{"abc", "ddd"}).toString();


            Object[] p = new Object[2];
            String[] str ={"a","b"};
            p[0] = str;
            p[1] = str;

            String result = saddMethod2.invoke(null,p).toString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


}
