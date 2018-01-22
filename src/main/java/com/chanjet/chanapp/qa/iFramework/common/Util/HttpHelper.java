package com.chanjet.chanapp.qa.iFramework.common.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by houhja on 10/20/16.
 */

public class HttpHelper {
    private static Logger log = LogManager.getLogger(HttpHelper.class);

    private static final int SOCKET_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 10000;

    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }

            });
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    public static JSONObject httpGet(String url, Cookie[]... cookies) throws Exception {
        URL newUrl = new URL(url);
        java.net.URI uri = new java.net.URI(newUrl.getProtocol(), newUrl.getHost(), newUrl.getPath(), newUrl.getQuery(), null);
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(uri);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if (url.startsWith("https")) {
            try {
                httpClient = createSSLInsecureClient();
            } catch (GeneralSecurityException e) {
//                logger.error("httpGet", e);
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        try {
            response = httpClient.execute(httpGet, new BasicHttpContext());
            HttpEntity entity = response.getEntity();
            String httpResponse = EntityUtils.toString(entity, "utf-8");
            if(httpResponse.startsWith("jsonp%callback%({")){
                httpResponse = httpResponse.replace("jsonp%callback%(", "");
                httpResponse = httpResponse.substring(0, httpResponse.length() - 2);
                log.info("response=" + httpResponse);
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                logger.warn("request url failed, http code=" + response.getStatusLine().getStatusCode()
//                        + ", url=" + url);
                if(entity != null){
                    JSONObject result = new JSONObject();
                    result = JSON.parseObject(httpResponse);
                    if (result.containsKey("errcode") && result.getIntValue("errcode") != 0) {
                        int errCode = result.getIntValue("errcode");
                        String errMsg = result.getString("errmsg");
//                        logger.warn("request failed, url: {}, errcode: {}, errmsg: {}", url, errCode, errMsg);
                        throw new Exception(errMsg);
                    }
                    return result;
                }
                return null;
            }

            if (entity != null) {

//                logger.info("httpResponse, url: {}, response: {}", url, httpResponse);
                if (StringUtils.isNotEmpty(httpResponse)) {
                    JSONObject result = new JSONObject();
                    if(httpResponse.startsWith("<!DOCTYPE html>")){
                        result.put("success", "false");
                        result.put("actual html result", httpResponse);
                        result.put("message", "customer return by iframework as response is a html!");
                    } else{
                        result = JSON.parseObject(httpResponse);
                        if (result.containsKey("errcode") && result.getIntValue("errcode") != 0) {
                            int errCode = result.getIntValue("errcode");
                            String errMsg = result.getString("errmsg");
//                        logger.warn("request failed, url: {}, errcode: {}, errmsg: {}", url, errCode, errMsg);
                            throw new Exception(errMsg);
                        }
                    }
                    return result;
                }
            }
        } catch (IOException e) {
            log.error("httpGet error", e);
//            logger.error("httpGet error, url: {}", url);
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("httpGet error", e);
                }
        }

        return new JSONObject();
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * @param url
     * @param params
     * @return HTTP响应
     */

    public static String doGet(String url, Map<String, String> params) throws Exception {

        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }

        url += param;

        if(url.startsWith("http://passport.chanjet.")){
            String s = "d";
        }

        try {
            JSONObject response = HttpHelper.httpGet(url);
            log.info("response=" + response);
            return response.toString();
        } catch (Exception e){
            log.error("Http get occur error!url=" + url + ",exception:" + e.getStackTrace());
            return "Http get occur error!url=" + url + ",exception:" + e.getStackTrace();
        }

    }

    public static String doGet(String url, Map<String, String> params, Cookie[] cookie) throws Exception {

        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }

        url += param;

        try {
            JSONObject response = HttpHelper.httpGet(url);
            log.info("response=" + response);
            return response.toString();
        } catch (Exception e){
            log.error("Http get occur error!url=" + url + ",exception:" + e.getStackTrace());
            return "Http get occur error!url=" + url + ",exception:" + e.getStackTrace();
        }

    }

    public static List<Object> doPostGetCookie(String url, Map<String, String> data) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        method.getParams().setParameter("Accept", "text/plain");
        for(Map.Entry<String,String> entity : data.entrySet()){
            method.addParameter(entity.getKey(), entity.getValue());
        }

        httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }

        List<Object> results = new ArrayList<Object>();
        results.add(JSONObject.parseObject(body));
        results.add(httpClient.getState().getCookies());

        return results;
    }

    public static JSONObject doPostSetCookie(String url, Map<String, String> data, Cookie[] cookie) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setURI(new URI(url, false));
        method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        method.getParams().setParameter("Accept", "text/plain");
        for(Map.Entry<String,String> entity : data.entrySet()){
            method.addParameter(entity.getKey(), entity.getValue());
        }

        httpClient.getState().addCookies(cookie);

        httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }
        return initResult(body);
    }

    public static JSONObject doPost(String url, Map<String, String> data) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setURI(new URI(url, false));
        method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        method.getParams().setParameter("Accept", "text/plain");
        method.getParams().setParameter("http.protocol.content-charset", "utf-8");
        for(Map.Entry<String,String> entity : data.entrySet()){
            method.addParameter(entity.getKey(), entity.getValue());
        }

        httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }
        return initResult(body);
    }

    private static JSONObject initResult(String body){

        JSONObject result;
        try{
            result = JSONObject.parseObject(body);
        } catch (Exception ex){
            result = new JSONObject();
            result.put("success", "false");
            result.put("result", body);
        }
        return result;
    }

    public static JSONObject doPostSetHeader(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        BufferedReader in = null;
        String result = "";
        try {
            RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            HttpPost method = new HttpPost(url);
            method.setConfig(config);
            method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            method.getParams().setParameter("Accept", "text/plain");

            List<org.apache.http.NameValuePair> paramList = new ArrayList<org.apache.http.NameValuePair>();
            for(Map.Entry<String,String> entity : data.entrySet()){
                BasicNameValuePair nameValuePair = new BasicNameValuePair(entity.getKey(), entity.getValue());
                paramList.add(nameValuePair);
            }

            if(0 < paramList.size()){
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }

            for(Map.Entry<String,String> entity : headers.entrySet()){
                method.setHeader(new BasicHeader(entity.getKey(), entity.getValue()));
            }

            CloseableHttpResponse response = httpClient.execute(method);
            HttpEntity entity = response.getEntity();
            in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info(result);

            if(result.startsWith("jsonp%callback%({")){
                result = result.replace("jsonp%callback%(", "");
                result = result.substring(0, result.length() - 2);
                log.info("response=" + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
            }
            return initResult(result);
        }
    }

    public static JSONObject doGetSetHeader(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        BufferedReader in = null;
        String result = "";
        try {
            List<org.apache.http.NameValuePair> paramList = new ArrayList<org.apache.http.NameValuePair>();
            for(Map.Entry<String,String> entity : data.entrySet()){
                paramList.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
            }
            if(0 < paramList.size()){
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            }

            RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            HttpGet method = new HttpGet(url);
            method.setConfig(config);
            method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            method.getParams().setParameter("Accept", "text/plain");

            for(Map.Entry<String,String> entity : headers.entrySet()){
                method.setHeader(new BasicHeader(entity.getKey(), entity.getValue()));
            }

            CloseableHttpResponse response = httpClient.execute(method);
            HttpEntity entity = response.getEntity();
            in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info(result);

            if(result.startsWith("jsonp%callback%({")){
                result = result.replace("jsonp%callback%(", "");
                result = result.substring(0, result.length() - 2);
                log.info("response=" + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
            }
            return initResult(result);
        }
    }

    public static JSONObject doPostSingleKey(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        BufferedReader in = null;
        String result = "";
        try {
            RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            HttpPost method = new HttpPost(url);
            method.setConfig(config);
            method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            method.getParams().setParameter("Accept", "text/plain");

            String params = "";

            List<org.apache.http.NameValuePair> paramList = new ArrayList<org.apache.http.NameValuePair>();
            for(Map.Entry<String,String> entity : data.entrySet()){
                if(StringUtils.isEmptyOrSpace(entity.getValue())){
                    params += entity.getKey();
                }
            }

            if(!StringUtils.isEmptyOrSpace(params)) {
                method.setEntity(new StringEntity(params));
            }

            for(Map.Entry<String,String> entity : headers.entrySet()){
                method.setHeader(new BasicHeader(entity.getKey(), entity.getValue()));
            }

            CloseableHttpResponse response = httpClient.execute(method);
            HttpEntity entity = response.getEntity();
            in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info(result);

            if(result.startsWith("jsonp%callback%({")){
                result = result.replace("jsonp%callback%(", "");
                result = result.substring(0, result.length() - 2);
                log.info("response=" + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
            }
            return initResult(result);
        }
    }

    public static JSONObject doGetSingleKey(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        BufferedReader in = null;
        String result = "";
        try {
            String params = "";
            for(Map.Entry<String,String> entity : data.entrySet()){
                if(StringUtils.isEmptyOrSpace(entity.getValue())){
                    params += entity.getKey();
                }
            }

            if(!StringUtils.isEmptyOrSpace(params)) {
                url += "?" + params;
            }

            RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            HttpGet method = new HttpGet(url);
            method.setConfig(config);
            method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            method.getParams().setParameter("Accept", "text/plain");

            for(Map.Entry<String,String> entity : headers.entrySet()){
                method.setHeader(new BasicHeader(entity.getKey(), entity.getValue()));
            }

            CloseableHttpResponse response = httpClient.execute(method);
            HttpEntity entity = response.getEntity();
            in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info(result);

            if(result.startsWith("jsonp%callback%({")){
                result = result.replace("jsonp%callback%(", "");
                result = result.substring(0, result.length() - 2);
                log.info("response=" + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
            }
            return initResult(result);
        }
    }

    public static JSONObject doPostSetHeaderCookie(String url, Map<String, String> data, Map<String, String> headers, Cookie[] cookie) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.getParams().setParameter("Content-Type", "text/json");
        method.getParams().setParameter("Accept", "application/json");
        for(Map.Entry<String,String> entity : data.entrySet()){
            if(StringUtils.isEmptyOrSpace(entity.getValue())){
//                RequestEntity requestEntity = new StringRequestEntity(entity.getKey());
                RequestEntity requestEntity = new ByteArrayRequestEntity(entity.getKey().getBytes());
                method.setRequestEntity(requestEntity);
            } else {
                method.addParameter(entity.getKey(), entity.getValue());
            }
        }

        for(Map.Entry<String,String> entity : headers.entrySet()){
            method.setRequestHeader(entity.getKey(), entity.getValue());
        }

        httpClient.getState().addCookies(cookie);
        httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }

        return initResult(body);
    }

    public static HttpClient getClient(){

        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setProxy("localhost", 8081);
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

        HttpConnectionManagerParams params = new HttpConnectionManagerParams();

        int maxHostConnections = 20;
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        connectionManager.setParams(params);

        HttpClient httpClient = new HttpClient(connectionManager);
        httpClient.setHostConfiguration(hostConfig);
        Credentials creds = new UsernamePasswordCredentials("haijia","hello1234");
        httpClient.getState().setCredentials(AuthScope.ANY,creds);

        return httpClient;
    }

    public static JSONObject httpPost(String url, String data) throws Exception {
        return httpPost(url, JSONObject.parseObject(data));
    }

    public static JSONObject httpPost(String url, Map<String, String> data) throws Exception {
        return httpPost(url, JSONObject.toJSONString(data));
    }

    public static JSONObject httpPost(String url, JSONObject data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if (url.startsWith("https")) {
            try {
                httpClient = createSSLInsecureClient();
            } catch (GeneralSecurityException e) {
//                logger.error("httpGet", e);
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        try {
            String body = JSON.toJSONString(data);
//            logger.info("httpRequest, url: {}, body: {}", url, body);
            StringEntity requestEntity = new StringEntity(body, "utf-8");
            httpPost.setEntity(requestEntity);

            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                logger.warn("request url failed, http code=" + response.getStatusLine().getStatusCode()
//                        + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String httpResponse = EntityUtils.toString(entity, "utf-8");
//                logger.info("httpResponse, url: {}, response: {}", url, httpResponse);
                if (StringUtils.isNotEmpty(httpResponse)) {
                    JSONObject result = JSON.parseObject(httpResponse);
                    if (result.containsKey("errcode") && result.getIntValue("errcode") != 0) {
                        int errCode = result.getIntValue("errcode");
                        String errMsg = result.getString("errmsg");
//                        logger.warn("request failed, url: {}, errcode: {}, errmsg: {}", url, errCode, errMsg);
                        throw new Exception(errMsg);
                    }
                    return result;
                }
            }
        } catch (IOException e) {
//            logger.error("httpPost error", e);
//            logger.error("httpPost error, url: {}", url);
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
//                    logger.error("httpPost error", e);
                }
        }

        return null;
    }

    public static String appendParameters(String baseUrl, String append) {
        StringBuilder sb = new StringBuilder(baseUrl.trim());
        if (baseUrl.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        sb.append(append);
        return sb.toString();
    }
}
