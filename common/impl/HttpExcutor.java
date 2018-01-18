package com.chanjet.chanapp.qa.iFramework.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.common.Util.StringUtils;
import com.chanjet.chanapp.qa.iFramework.common.IExecutor;
import com.chanjet.chanapp.qa.iFramework.common.Util.HttpHelper;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step;
import com.chanjet.chanapp.qa.iFramework.common.xml.Parser;
import org.apache.commons.httpclient.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houhja on 11/25/16.
 */
public class HttpExcutor implements IExecutor {
    private static Logger log = LogManager.getLogger(HttpExcutor.class);
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    private Map<String, String> requestParams;

    public HttpExcutor(){

    }

    public HttpExcutor(String url, Map<String, String> params){
        this.url = url;
        this.requestParams = params;
        log.debug(url);
        for(Map.Entry<String,String> entity : params.entrySet()){
            log.debug("key:" + entity.getKey());
            log.debug("value:" + entity.getValue());
        }
    }

    public void setup(){

    }

    public Object RunHttpPost() throws Exception{

        JSONObject result;

        try{
            result = HttpHelper.doPost(url, requestParams);
        } catch (Exception ex){
            return ex;
        }

        return result;
    }

    public Object RunHttpGet() throws Exception{

        String result;

        try{
            result = HttpHelper.doGet(url, requestParams);
        } catch (Exception ex){
            return ex;
        }
        return result;
    }


    public List<Object> RunHttpPostGetCookie() throws Exception{
        List<Object> results = new ArrayList<Object>();

        try{
            results = HttpHelper.doPostGetCookie(url, requestParams);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }

        return results;
    }

    public Object RunHttpGetGetCookie() throws Exception{
        String result;

        try{
            result = HttpHelper.doGet(url, requestParams);
        } catch (Exception ex){
            return null;
        }
        return result;
    }

    public Object RunHttpPostSetCookie(Cookie[] cookie) throws Exception{
        JSONObject result;

        try{
            result = HttpHelper.doPostSetCookie(url, requestParams, cookie);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSetCookie(Cookie[] cookie) throws Exception{
        String result;

        try{
            result = HttpHelper.doGet(url, requestParams, cookie);
        } catch (Exception ex){
            return null;
        }
        return result;
    }

    public Object RunHttpPostSetHeader(Map<String, String> headerMaps) throws Exception{
        JSONObject result;

        try{
            result = HttpHelper.doPostSetHeader(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSetHeader(Map<String, String> headerMaps) throws Exception{
        JSONObject result;

        try{
            result = HttpHelper.doGetSetHeader(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostSingleKey(Map<String, String> headerMaps) throws Exception{
        JSONObject result;

        try{
            result = HttpHelper.doPostSingleKey(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSingleKey(Map<String, String> headerMaps) throws Exception{
        JSONObject result;

        try{
            result = HttpHelper.doGetSingleKey(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostSetHeaderCookie(Map<String, String> headerMaps, Cookie[] cookie) throws Exception{
        JSONObject result;

        try{
            result = HttpHelper.doPostSetHeaderCookie(url, requestParams, headerMaps, cookie);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    @Override
    public Object Execute(Step step, List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception{

        Map<String, String> requestParams = null;

        if(null == entry){
            requestParams = parser.getRequestDataFromCase(step, preExecutedResults);
        } else{
          requestParams = parser.getRequestDataFromExcel(step, preExecutedResults, entry);
        }

        this.setUrl(step.getUrl());
        this.setRequestParams(requestParams);

        log.debug("url:" + step.getUrl());

        for(Map.Entry<String,String> entity : requestParams.entrySet()){
            log.info("key:" + entity.getKey());
            log.info("value:" + entity.getValue());
        }

        String action = "";
        Integer sequence = 0;
        String[] flags;
        if(StringUtils.isNotEmpty(step.getAction())){
            action = step.getAction();
            flags = action.split("_");
            if((null != flags) && (2 == flags.length)){
                action = flags[0];
                sequence = Integer.parseInt(flags[1]);
            }
        }

        if(!StringUtils.isEmptyOrSpace(step.getRule())){
            step.setRule(step.getRule().toLowerCase());
        }

        switch (action.toLowerCase()){
            case "":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPost();
                } else if (step.getRule().equals("get")){
                    return RunHttpGet();
                }
                break;
            case "getcookie":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostGetCookie();
                } else if (step.getRule().equals("get")){
                    return RunHttpGetGetCookie();
                }
                break;
            case "setcookie":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostSetCookie(getCookie(sequence, preExecutedResults));
                } else if (step.getRule().equals("get")){
                    return RunHttpGetSetCookie(getCookie(sequence, preExecutedResults));
                }
                break;
            case "setheader":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostSetHeader(getHeaderMaps(step, preExecutedResults));
                } else if (step.getRule().equals("get")){
                    return RunHttpGetSetHeader(getHeaderMaps(step, preExecutedResults));
                }
                break;
            case "singlekey":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostSingleKey(getHeaderMaps(step, preExecutedResults));
                } else if (step.getRule().equals("get")){
                    return RunHttpGetSingleKey(getHeaderMaps(step, preExecutedResults));
                }
                break;
            case "setheadercookie":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostSetHeaderCookie(getHeaderMaps(step, preExecutedResults), getCookie(sequence, preExecutedResults));
                } else if (step.getRule().equals("get")){
                    //return executor.RunHttpGetSetHeader(getHeaderMaps());
                    log.info(("Attendtion : get http do not support set header"));
                }
                break;
            default:
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPost();
                } else if (step.getRule().equals("get")){
                    return RunHttpGet();
                }
                break;
        }

        log.warn("unexpect run executor");
        return RunHttpPost();

    }

    private Map<String, String> getHeaderMaps(Step step, List<Object> preExecutedResults){
        Map<String, String> map = new HashMap<String, String>();

        if(!StringUtils.isEmptyOrSpace(step.getHeaderInit())){
            String[] items = step.getHeaderInit().split("___");
            if(3 == items.length){
                String sequence = items[0];
                String type = items[1];
                String key = items[2];
                String result = "";
                switch (type.toLowerCase()){
                    case "string":
                        result = (String)preExecutedResults.get(Integer.parseInt(sequence));
                        break;
                    case "int":
                        result =  (String)preExecutedResults.get(Integer.parseInt(sequence));
                        break;
                    default:
                            break;
                }

                if(StringUtils.isEmptyOrSpace(step.getHeaders())){
                    step.setHeaders(key + ":" + result);
                } else{
                    step.setHeaders(step.getHeaders() + ";" + key + ":" + result);
                }

            }
        }

        if(!StringUtils.isEmptyOrSpace(step.getHeaders())){
            String[] headers = step.getHeaders().split(";");

            for(String header : headers){
                String[] keyValue = header.split(":");
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return map;
    }

    private Cookie[] getCookie(Integer num, List<Object> preExecutedResults){
        List<Object> results = (ArrayList)preExecutedResults.get(num);
        Cookie[] cookies = new Cookie[1];
        if((null != results) && (2 == results.size())) {
            cookies = (Cookie[]) results.get(1);
            return cookies;
        }
        return cookies;
    }

}
