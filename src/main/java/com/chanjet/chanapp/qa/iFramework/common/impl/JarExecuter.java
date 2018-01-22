package com.chanjet.chanapp.qa.iFramework.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.common.Util.StringUtils;
import com.chanjet.chanapp.qa.iFramework.common.IExecutor;
import com.chanjet.chanapp.qa.iFramework.common.Util.MyCustomerException;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.RequestParameter;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step;
import com.chanjet.chanapp.qa.iFramework.common.xml.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            log.error("Step miss the ClassName!");
            return null;
        }

        if(StringUtils.isEmptyOrSpace(step.getMethod()) || null == step.getParameterType()){
            log.error("Step miss the Method or ParameterType in xml!");
            return null;
        }

        String[] parameterTypes = step.getParameterType().split(",");
        List<Class<?>> classes = new ArrayList<Class<?>>();
        List<Object> values = new ArrayList<Object>();

        for(String item : parameterTypes){
            String[] items = item.split("___");
            if(null == items || 2 != items.length){
                log.error(item + " shoud NOT Null or length = 2!");
                break;
            }
            String type = items[0];
            String value = items[1];
            switch (type.toLowerCase()){
                case "string":
                    classes.add(String.class);
                    values.add((String) value);
                    continue;
                case "long":
                    classes.add(Long.class);
                    values.add(Integer.parseInt(value));
                case "int":
                    classes.add(int.class);
                    values.add(Integer.parseInt(value));
                    continue;
                case "integer":
                    classes.add(Integer.class);
                    values.add(Integer.valueOf(value));
                    continue;
                case "bool":
                    classes.add(Boolean.class);
                    if(value.toLowerCase().equals("true")){
                        values.add((Object)true);
                    } else{
                        values.add((Object)false);
                    }
                    continue;
                case "object":
                    classes.add(Object.class);
                    values.add((Object)value);
                    continue;
                case "map":
                    classes.add(Map.class);

                    continue;
                case "date":
                    classes.add(Date.class);
                    continue;
                default:
                    log.warn("Miss Class<?> " + type);
                    break;
            }
        }

        Class<?>[] classesArray = new Class<?>[parameterTypes.length];

        classes.toArray(classesArray);

        Object[] valuesArray = new Object[values.size()];
        values.toArray(valuesArray);

        Class<?> clsInstance = Class.forName(step.getClsName());
        Constructor clsInstanceConstructor = clsInstance.getConstructor(classesArray);

        List<RequestParameter> requestParams = null;

        if(null == entry){
            requestParams = parser.getRequestDataFromCaseForJar(step, preExecutedResults);
        } else {
            requestParams = parser.getRequestExcelFromCaseForJar(step, preExecutedResults, entry);
        }

        ArrayList<Object> params = new ArrayList<Object>();
        for(RequestParameter entity : requestParams){
            switch (entity.getType().toLowerCase().trim()){
                case "map":
                    Map<String, String> m1 = new HashMap<String, String>();
                    for(String item : entity.getValue().split(",")){
                        String[] kv = item.split(":");
                        if(null != kv && 2 == kv.length){
                            m1.put(kv[0], kv[1]);
                        } else{
                            log.warn("jar parameter " + item + " should NOT null or size Not 2");
                        }
                    }
                    params.add(m1);
                    break;
                case "json":
                    params.add(entity.getValue());
                    break;
                case "string":
                    params.add(entity.getValue());
                    break;
                case "int":
                    params.add(Integer.parseInt(entity.getValue()));
                    break;
                case "long":
                    params.add(Long.parseLong(entity.getValue()));
                    break;
                case "bool":
                    if(entity.getValue().equals("true")){
                        params.add(true);
                    } else{
                        params.add(false);
                    }
                    break;
                case "date":
                    if(StringUtils.isEmptyOrSpace(entity.getTimeFormat())){
                        throw new MyCustomerException(step.getName() + " MISS timeFormat:" + entity.getType());
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat(entity.getTimeFormat());
                    Date actualDate = formatter.parse(entity.getValue());
                    params.add(actualDate);
                    break;
                case "custom":
                    Class<?> paramClsInstance = Class.forName(entity.getCustomerClass());
                    Constructor paramClsInstanceConstructor = paramClsInstance.getConstructor();
                    Object tempInstance = null;
                    if(!StringUtils.isEmptyOrSpace(entity.getJarparam_keyvalue())){
                        String[] jarParamKeyValue = entity.getJarparam_keyvalue().split(",");
                        Method[] methods = paramClsInstance.getMethods();
                        tempInstance = paramClsInstanceConstructor.newInstance();
                        for(String param : jarParamKeyValue){
                            String[] keyvalue = param.split("___");
                            if(3 == keyvalue.length){
                                for(Method method : methods){
                                    if(method.getName().toLowerCase().equals(keyvalue[0].toLowerCase())){
                                        switch (keyvalue[2].toLowerCase()){
                                            case "int":
                                                method.invoke(tempInstance, Integer.parseInt(keyvalue[1]));
                                                break;
                                            case "string":
                                                method.invoke(tempInstance, String.valueOf(keyvalue[1]));
                                                break;
                                            case "bool":
                                                method.invoke(tempInstance, Boolean.parseBoolean(keyvalue[1]));
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(null != tempInstance){
                        params.add(tempInstance);
                    }
                    break;
                case "class":
                    switch (entity.getValue().toLowerCase()){
                        case "map":
                            params.add(Map.class);
                            break;
                        case "jsonobject":
                            params.add(JSONObject.class);
                            break;
                        default:
                            break;
                    }

                    break;
                default:
                    log.warn("Maybe missing a type define in runJarStepFromCase for " + entity.getType());
                    break;
            }
        }

        //1. run interface
        Object[] paramArray = new Object[params.size()];
        params.toArray(paramArray);

        Method[] methods = clsInstance.getMethods();

        Object result = null;
        for(Method method : methods){
            if(method.getName().equals(step.getMethod())){
                result = method.invoke(clsInstanceConstructor.newInstance(valuesArray), paramArray);
                break;
            }
        }

        log.info("result is: " + result);
        return result;
    }
}
