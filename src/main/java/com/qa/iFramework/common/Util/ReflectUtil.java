package com.qa.iFramework.common.Util;

import com.alibaba.fastjson.JSONObject;
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

public class ReflectUtil {
    private static Logger log = LogManager.getLogger(ReflectUtil.class);

    public static Object ExecuteReflect(Step step, List<Object> preExecutedResults, List<RequestParameter> requestParams) throws Exception{
        Object[] paramArray = InitMethodParams(step, requestParams);

        Class<?> clsInstance = Class.forName(step.getClsName());
        Object result = null;

        if(null != step.getStaticCls() && !StringUtils.isEmptyOrSpace(step.getStaticCls())){
            //静态类调用
            Method method = clsInstance.getMethod(step.getMethod(), new Class[]{Object.class});
            result = method.invoke(null, paramArray).toString();
        } else{
            //非静态类调用
            result = InitAndInvoke(step, preExecutedResults, paramArray, clsInstance);
        }

        log.info("result is: " + result);
        return result;
    }

    public static Object InitAndInvoke(Step step, List<Object> preExecutedResults, Object[] paramArray, Class<?> clsInstance) throws Exception {
        if(StringUtils.isEmptyOrSpace(step.getMethod()) || null == step.getParameterType()){
            log.error("Step miss the Method or ParameterType in xml!");
            return null;
        }

        //从ParameterInit里获取指定步骤里的值来替换ParameterType里的关键字
        if(StringUtils.isNotEmpty(step.getParameterInit())){
            String newParamType = Parser.getPreStepValue(preExecutedResults, step.getParameterType(), step.getParameterInit().split(","), new JSONObject());
            step.setParameterType(newParamType);
        }
        return ReflectUtil.InitAndInvokeClass(clsInstance, step, paramArray);
    }

    public static Object[] InitMethodParams(Step step, List<RequestParameter> requestParams)
            throws Exception{
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
                    params.add(JSONObject.parseObject(entity.getValue()));
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
                    String dateOrginal = "";
                    switch (entity.getValue().getClass().getName()){
                        case "com.alibaba.fastjson.JSONObject":
                            dateOrginal = (JSONObject.parseObject(entity.getValue())).getString("");
                            break;
                        case "java.lang.String":
                            dateOrginal = entity.getValue();
                            break;
                        default:
                            dateOrginal = entity.getValue();
                            break;
                    }
                    Date actualDate = formatter.parse(dateOrginal);
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
                case "file":
                    if(StringUtils.isNotEmpty(entity.getValue())) {
                        File paramfile = new File(entity.getValue()) {

                            /**
                             *
                             */
                            private static final long serialVersionUID = 12345674322L;

                        };
                        params.add(paramfile);
                        break;
                    }
                default:
                    log.warn("Maybe missing a type define in runJarStepFromCase for " + entity.getType());
                    break;
            }
        }

        Object[] paramArray = new Object[params.size()];
        params.toArray(paramArray);
        return paramArray;
    }

    public static Object InitAndInvokeClass(Class<?> clsInstance, Step step, Object[] paramArray) throws Exception{
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
                case "json":
                    classes.add(JSONObject.class);
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

        Object realObjectInstance;
        if(0 != values.size()){
            realObjectInstance = clsInstance.getConstructor(classesArray).newInstance(valuesArray);
        } else{
            realObjectInstance = clsInstance;
        }
        Method[] methods = clsInstance.getMethods();
        for(Method method : methods){
            if(method.getName().equals(step.getMethod())
                    && method.getParameterCount() == paramArray.length
                    && CheckParameterTypes(method.getParameterTypes(), paramArray)){
                return method.invoke(realObjectInstance, paramArray);
            }
        }
        return null;
    }

    public static boolean CheckParameterTypes(Class<?>[] classes, Object[] paramsTypes){
        if(classes.length != paramsTypes.length){
            return false;
        }

        for(int i = 0; i < classes.length;i++){
            if(classes[i].isAssignableFrom(paramsTypes[i].getClass())){
                return true;
            }

            if(!classes[i].getTypeName().equals(paramsTypes[i].getClass().getName())){
                return false;
            }
        }
        return true;
    }
}
