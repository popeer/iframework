package com.qa.iFramework.common.Util;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by haijia on 5/19/18.
 */
public class GroovyUtil {
    protected static Logger log = LogManager.getLogger(GroovyUtil.class);

    public Object loadCustomGroovyScript(String scriptPath, Object[] args, String method) throws Exception{
        if(StringUtils.isEmptyOrSpace(scriptPath)){
            return null;
        }

        if(StringUtils.isEmptyOrSpace(method)){
            return null;
        }

        try{
            // 构造GroovyClassLoader
            ClassLoader parent = getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);

            // 动态加载Groovy类
            Class securityStringCls = loader.parseClass(new File(scriptPath));
            GroovyObject customObject = (GroovyObject)securityStringCls.newInstance();
            Object result = customObject.invokeMethod(method, args);
            return result;

        } catch (Exception ex){
            log.warn(ex);
            throw ex;
        }
    }
}
