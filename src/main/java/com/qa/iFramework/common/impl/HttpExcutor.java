package com.qa.iFramework.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.common.IExecutor;
import com.qa.iFramework.common.Util.Constants;
import com.qa.iFramework.common.Util.GroovyUtil;
import com.qa.iFramework.common.Util.HttpHelper;
import com.qa.iFramework.common.Util.StringUtils;
import com.qa.iFramework.common.xml.Entity.Step;
import com.qa.iFramework.common.xml.Parser;
import javafx.collections.transformation.SortedList;
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
//        log.info(url);
//        for(Map.Entry<String,String> entity : params.entrySet()){
//            log.debug("key:" + entity.getKey());
//            log.debug("value:" + entity.getValue());
//        }
    }

    public void setup(){

    }

    public Object RunHttpPost() throws Exception{

        Object result;

        try{
            result = HttpHelper.doPost(url, requestParams);
        } catch (Exception ex){
            return ex;
        }

        return result;
    }

    public Object RunHttpGet() throws Exception{

        Object result;

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

    public List<Object> RunHttpGetGetCookie() throws Exception{
        List<Object> results = new ArrayList<Object>();

        try{
            results = HttpHelper.doGetCookie(url, requestParams);
        } catch (Exception ex){
            return null;
        }
        return results;
    }

    public Object RunHttpPostSetCookie(Cookie[] cookie) throws Exception{
        Object result;

        try{
            result = HttpHelper.doPostSetCookie(url, requestParams, cookie);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSetCookie(Cookie[] cookie) throws Exception{
        Object result;

        try{
            result = HttpHelper.doGet(url, requestParams, cookie);
        } catch (Exception ex){
            return null;
        }
        return result;
    }

    public Object RunHttpPostSetHeader(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.doPostSetHeader(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostUploadFileHeader(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            String body = requestParams.get(Constants.bodyData);
            String filePath = requestParams.get(Constants.httpFileUploadPath);
            result = HttpHelper.doPostUploadFile(url, filePath, body, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSetHeader(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.doGetSetHeader(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostSingleKey(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.doPostSingleKey(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSingleKey(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.doGetSingleKey(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostSetHeaderCookie(Map<String, String> headerMaps, Cookie[] cookie) throws Exception{
        Object result;

        try{
            result = HttpHelper.doPostSetHeaderCookie(url, requestParams, headerMaps, cookie);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpGetSetHeaderCookie(Map<String, String> headerMaps, Cookie[] cookie) throws Exception{
        Object result;

        try{
            result = HttpHelper.doGetSetHeaderCookie(url, requestParams, headerMaps, cookie);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunPostBodyHeader(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            String bodyData = requestParams.get(Constants.bodyData);
            result = HttpHelper.doPostBodyData(url, bodyData, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunPostZipBodyHeader(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            String bodyData = requestParams.get(Constants.bodyData);
            result = HttpHelper.doPostZipBodyData(url, bodyData, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunDelete(Map<String, String> headerMaps, String statusCode) throws Exception{
        Object result;

        try{
            result = HttpHelper.DELETE(url, requestParams, headerMaps, statusCode);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunPUT(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.PUT(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunPUTSingleBodyData(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.PutSingleBodyData(url, requestParams, headerMaps);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostJSON() throws Exception{
        Object result;

        try{
            result = HttpHelper.doPostJson(url, requestParams);
        } catch (Exception ex){
            log.error(ex.getMessage());
            return null;
        }

        return result;
    }

    public Object RunHttpPostJSONHeader(Map<String, String> headerMaps) throws Exception{
        Object result;

        try{
            result = HttpHelper.doPostJsonHeader(url, requestParams,headerMaps);
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

        reactUrl(step, preExecutedResults);

        this.setUrl(step.getUrl());
        this.setRequestParams(requestParams);

//        log.debug("url:" + step.getUrl());
//
//        for(Map.Entry<String,String> entity : requestParams.entrySet()){
//            log.info("key:" + entity.getKey());
//            log.info("value:" + JSONObject.toJSONString(entity.getValue()));
//        }

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
                    return RunHttpGetSetHeaderCookie(getHeaderMaps(step, preExecutedResults), getCookie(sequence, preExecutedResults));
                }
                break;
            case "dopostjson":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostJSON();
                } else if (step.getRule().equals("get")){
                    //return executor.RunHttpGetSetHeader(getHeaderMaps());
                    log.info(("Attendtion : get http do not support set header"));
                }
                break;
            case "dopostjsonheader":
                if(StringUtils.isEmptyOrSpace(step.getRule()) || (step.getRule().equals("post"))){
                    return RunHttpPostJSONHeader(getHeaderMaps(step, preExecutedResults));
                } else if (step.getRule().equals("get")){
                    //return executor.RunHttpGetSetHeader(getHeaderMaps());
                    log.info(("Attendtion : get http do not support set header"));
                }
                break;
            case "delete":
                return RunDelete(getHeaderMaps(step, preExecutedResults), step.getDeleteStatusCode());
            case "put":
                return RunPUT(getHeaderMaps(step, preExecutedResults));
            case "putbodydata":
                return RunPUTSingleBodyData(getHeaderMaps(step, preExecutedResults));
            case "postbodydata":
                return RunPostBodyHeader(getHeaderMaps(step, preExecutedResults));
            case "postzipbody":
                return RunPostZipBodyHeader(getHeaderMaps(step, preExecutedResults));
            case "postuploadfile":
                return RunHttpPostUploadFileHeader(getHeaderMaps(step, preExecutedResults));
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


    private Object getGroovyResult(String path, String[] args, String method) throws Exception{
        GroovyUtil groovyUtil = new GroovyUtil();
        return groovyUtil.loadCustomGroovyScript(path, args, method);
    }

    private Map<String, String> getHeaderMaps(Step step, List<Object> preExecutedResults) throws Exception{
        Map<String, String> map = new HashMap<String, String>();

        if(!StringUtils.isEmptyOrSpace(step.getHeaderInit())) {
            String[] headers = step.getHeaderInit().split(",");
            for(String header : headers){
                String[] headerInits = header.split("___");

                if (2 == headerInits.length && org.apache.commons.lang3.StringUtils.isNotBlank(step.getParameterType())) {
                    String key = headerInits[0];
                    String path = headerInits[1];
                    Object result = getGroovyResult(path, step.getParameterType().split(","), step.getGroovyMethodName());
                    if (null != result) {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(step.getHeaders())) {
                            switch (result.getClass().getName()) {
                                case "java.util.Map":
                                    Map<String, String> mapheader = (Map<String, String>) result;
                                    for (Map.Entry<String, String> entry : mapheader.entrySet()) {
                                        step.setHeaders(step.getHeaders() + ";" + entry.getKey() + ":" + entry.getValue());
                                    }
                                    break;
                                default:
                                    step.setHeaders(step.getHeaders() + ";" + key + ":" + result);
                                    break;
                            }
                        } else {
                            switch (result.getClass().getName()) {
                                case "java.util.Map":
                                    Map<String, String> mapheader = (Map<String, String>) result;
                                    for (Map.Entry<String, String> entry : mapheader.entrySet()) {
                                        step.setHeaders(entry.getKey() + ":" + entry.getValue());
                                    }
                                    break;
                                default:
                                    step.setHeaders(key + ":" + result);
                                    break;
                            }
                        }
                    }
                }


                if (3 <= headerInits.length) {
                    String sequence = headerInits[0];
                    String type = headerInits[1];
                    String key = headerInits[2];
                    String result = "";
                    switch (type.toLowerCase()) {
                        case "string":
                            result = (String) preExecutedResults.get(Integer.parseInt(sequence));
                            break;
                        case "int":
                            result = (String) preExecutedResults.get(Integer.parseInt(sequence));
                            break;
                        case "json":
                            result = String.valueOf(preExecutedResults.get(Integer.parseInt(sequence)));
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (4 == headerInits.length) {
                                result = resultJson.getString(headerInits[3]);
                            } else {
                                result = resultJson.getString(key);
                            }
                            break;
                        case "jsonobject":
                            JSONObject jsonObject = (JSONObject) preExecutedResults.get(Integer.parseInt(sequence));
                            result = jsonObject.get(headerInits[3]).toString();
                            break;
                        default:
                            break;
                    }

                    //把之前步骤里的结果作为header的参数传进来
                    List<String> args = new ArrayList<>();
                    String[] params = null;
                    if (StringUtils.isNotEmpty(step.getParameterType())) {
                        params = step.getParameterType().split(",");
                        if (null != params && 0 < params.length) {
                            for (String param : params) {
                                args.add(param);
                            }
                        }
                    }

                    args.add(result);

                    if (5 == headerInits.length) {
                        Object groovyResult = getGroovyResult(headerInits[4], args.toArray(new String[0]), step.getGroovyMethodName());
                        if (null != groovyResult) {
                            if (org.apache.commons.lang3.StringUtils.isNotBlank(step.getHeaders())) {
                                switch (groovyResult.getClass().getName()) {
                                    case "java.util.HashMap":
                                        Map<String, String> mapheader = (Map<String, String>) groovyResult;
                                        for (Map.Entry<String, String> entry : mapheader.entrySet()) {
                                            step.setHeaders(step.getHeaders() + ";" + entry.getKey() + ":" + entry.getValue());
                                        }
                                        break;
                                    default:
                                        step.setHeaders(step.getHeaders() + ";" + key + ":" + result);
                                        break;
                                }
                            } else {
                                switch (groovyResult.getClass().getName()) {
                                    case "java.util.HashMap":
                                        Map<String, String> mapheader = (Map<String, String>) groovyResult;
                                        StringBuilder currentHeader = new StringBuilder();
                                        for (Map.Entry<String, String> entry : mapheader.entrySet()) {
                                            currentHeader.append(entry.getKey() + ":" + entry.getValue() + ";");
                                        }
                                        step.setHeaders(currentHeader.toString());
                                        break;
                                    default:
                                        step.setHeaders(key + ":" + result);
                                        break;
                                }
                            }
                        } else {
                            if (StringUtils.isEmptyOrSpace(step.getHeaders())) {
                                step.setHeaders(key + ":" + result);
                            } else {
                                step.setHeaders(step.getHeaders() + ";" + key + ":" + result);
                            }
                        }
                    } else {
                        if (StringUtils.isEmptyOrSpace(step.getHeaders())) {
                            step.setHeaders(key + ":" + result);
                        } else {
                            step.setHeaders(step.getHeaders() + ";" + key + ":" + result);
                        }
                    }
                }
            }
        }

        if(!StringUtils.isEmptyOrSpace(step.getHeaders())){
            String[] headers = step.getHeaders().split(";");

            for(String header : headers){
                String[] keyValue = header.split(":", 2);
                if(null == keyValue || keyValue.length == 1){
//                    处理header数据多传了分号导致新增了一个为空的header的情况
                    continue;
                }
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return map;
    }

    private Cookie[] getCookie(Integer num, List<Object> preExecutedResults){
        Cookie[] cookies = new Cookie[1];
        if(num >= preExecutedResults.size()){
            return cookies;
        }
        List<Object> results = (ArrayList)preExecutedResults.get(num);

        if((null != results) && (2 == results.size())) {
            cookies = (Cookie[]) results.get(1);
            return cookies;
        }
        return cookies;
    }

    private void reactUrl(Step step, List<Object> preExecutedResults){
        try{
            //用groovy初始化url的情况
            if(!StringUtils.isEmptyOrSpace(step.getGroovyMethodName())
                    && !StringUtils.isEmptyOrSpace(step.getGroovyPath())){
                List<String> args = new ArrayList<>();
                args.add(step.getUrl());
                if (!StringUtils.isEmptyOrSpace(step.getGroovyArgs())) {
                    args.addAll(getGroovyArgs(step, preExecutedResults));
                }
                Object result = Parser.getGroovyResult(step.getGroovyPath(), args.toArray(new String[args.size()]), step.getGroovyMethodName());
                url = String.valueOf(result);
                step.setUrl(url);
            }
        } catch (Exception ex){
            log.error(ex);
        }
    }

    private List<String> getGroovyArgs(Step step, List<Object> preExecutedResults) {
        List<String> results = new ArrayList<String>();
        String[] args = step.getGroovyArgs().split(",");
        for (String arg : args) {
            String[] groovyArgment = arg.split("___");
            if (3 <= groovyArgment.length) {
                String sequence = groovyArgment[0];
                String type = groovyArgment[1];
                String key = groovyArgment[2];
                String result = "";
                switch (type.toLowerCase()) {
                    case "string":
                        result = (String) preExecutedResults.get(Integer.parseInt(sequence));
                        results.add(result);
                        break;
                    case "int":
                        result = (String) preExecutedResults.get(Integer.parseInt(sequence));
                        results.add(result);
                        break;
                    case "json":
                        result = String.valueOf(preExecutedResults.get(Integer.parseInt(sequence)));
                        JSONObject resultJson = JSONObject.parseObject(result);
                        if (4 == groovyArgment.length) {
                            result = resultJson.getString(groovyArgment[3]);
                        } else {
                            result = resultJson.getString(key);
                        }
                        results.add(result);
                        break;
                    case "jsonobject":
                        JSONObject jsonObject = (JSONObject) preExecutedResults.get(Integer.parseInt(sequence));
                        result = jsonObject.get(groovyArgment[3]).toString();
                        results.add(result);
                        break;
                    default:
                        break;
                }
            }
        }
        return results;
    }
}
