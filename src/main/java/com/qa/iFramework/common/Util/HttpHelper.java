package com.qa.iFramework.common.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
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
import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Created by houhja on 10/20/16.
 */

public class HttpHelper {
    private static Logger log = LogManager.getLogger(HttpHelper.class);

    private static final int SOCKET_TIMEOUT = 3000000;
    private static final int CONNECT_TIMEOUT = 3000000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 3000000;

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

    public static Object httpGet(String url, Cookie[]... cookies) throws Exception {
        HttpGet httpGet = null;

        if(UrlEncoderUtil.hasUrlEncoded(url)){
            httpGet = new HttpGet(url);
        } else {
            URL newUrl = new URL(url);
            java.net.URI uri = new java.net.URI(newUrl.getProtocol(), newUrl.getUserInfo(), newUrl.getHost(), newUrl.getPort(), newUrl.getPath(), newUrl.getQuery(), null);
            httpGet = new HttpGet();
            httpGet.setURI(uri);
        }

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
                    Object result = null;
                    result = initResult(httpResponse);
                    return result;
                }
                return null;
            }

            if (entity != null) {

//                logger.info("httpResponse, url: {}, response: {}", url, httpResponse);
                if (StringUtils.isNotEmpty(httpResponse)) {
                    Object result = null;
                    if(httpResponse.startsWith("<!DOCTYPE html>")){
                        JSONObject jsonObjectResult = new JSONObject();
                        jsonObjectResult.put("success", "false");
                        jsonObjectResult.put("actual html result", httpResponse);
                        jsonObjectResult.put("message", "customer return by iframework as response is a html!");
                        result = jsonObjectResult;
                    } else {
                        result = initResult(httpResponse);
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

    public static Object doGet(String url, Map<String, String> params) throws Exception {

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
            Object response = HttpHelper.httpGet(url);
            log.info("response=" + response);
            return response;
        } catch (Exception e){
            log.error("Http get occur error!url=" + url + ",exception:" + e.getStackTrace());
            return "Http get occur error!url=" + url + ",exception:" + e.getStackTrace();
        }

    }

    public static Object doGet(String url, Map<String, String> params, Cookie[] cookie) throws Exception {

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
            Object response = HttpHelper.httpGet(url);
            log.info("response=" + response);
            return response;
        } catch (Exception e){
            log.error("Http get occur error!url=" + url + ",exception:" + e.getStackTrace());
            return "Http get occur error!url=" + url + ",exception:" + e.getStackTrace();
        }

    }

    public static List<Object> doGetCookie(String url, Map<String, String> params) throws Exception {

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

        CookieStore cookieStore = new BasicCookieStore();
        try {
            JSONObject response = new JSONObject();
            log.info("response=" + response);

            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            BufferedReader in = null;
            String result = "";
            try {
                RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
                HttpGet method = new HttpGet(url);
                method.setConfig(config);
                method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                method.getParams().setParameter("Accept", "text/plain");

                CloseableHttpResponse httpResponse = httpClient.execute(method);
                HttpEntity entity = httpResponse.getEntity();
                if(null == entity){
                    in = new BufferedReader(new InputStreamReader( null, "UTF-8"));
                } else{
                    in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                }
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
            }

            List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();

            List<Object> results = new ArrayList<Object>();
            results.add(JSONObject.parseObject(result));
            results.add(cookies);

            return results;
        } catch (Exception e){
            log.error("Http get occur error!url=" + url + ",exception:" + e.getStackTrace());
            return null;
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

        int statusCode = httpClient.executeMethod(method);
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

    public static Object doPostSetCookie(String url, Map<String, String> data, Cookie[] cookie) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setURI(new URI(url, false));
        method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        method.getParams().setParameter("Accept", "text/plain");
        for(Map.Entry<String,String> entity : data.entrySet()){
            method.addParameter(entity.getKey(), entity.getValue());
        }

        httpClient.getState().addCookies(cookie);

        int statusCode = httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }
        return initResult(body);
    }

    public static Object doPost(String url, Map<String, String> data) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setURI(new URI(url, false));
        method.getParams().setParameter("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        method.getParams().setParameter("Accept", "text/plain");
        method.getParams().setParameter("http.protocol.content-charset", "utf-8");
        for(Map.Entry<String,String> entity : data.entrySet()){
            method.addParameter(entity.getKey(), entity.getValue());
        }

        int statusCode = httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }
        return initResult(body);
    }

    private static Object initResult(String body){

        Object result;
        try{
            Object objResult = JSON.parse(body);
            switch (objResult.getClass().getName()){
                case "com.alibaba.fastjson.JSONObject":
                    result = JSONObject.parseObject(body);
                    break;
                case "com.alibaba.fastjson.JSONArray":
                    result = JSONArray.parse(body);
                    break;
                default:
                    result = body;
                    break;
            }
        } catch (Exception ex){
            result = body;
        }

        log.info("result: " + result);
        return result;
    }

    public static Object doPostZipBodyData(String url, String bodydata, Map<String, String> headers) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        for(Map.Entry<String,String> entity : headers.entrySet()){
            postMethod.setRequestHeader(entity.getKey(), entity.getValue());
        }

        RequestEntity se = new StringRequestEntity(JSONObject.toJSON(bodydata).toString(), "text/json", "UTF-8");
        postMethod.setRequestEntity(se);

        httpClient.executeMethod(postMethod);

        String charSet =  postMethod.getResponseCharSet();
        InputStream inputStream = postMethod.getResponseBodyAsStream();

        GZIPInputStream gis = new GZIPInputStream(inputStream);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(gis,"UTF-8"));
        StringBuffer stringBuffer = new StringBuffer();
        String str= "";
        while((str = br2.readLine())!= null){
            stringBuffer.append(str);
        }

        String ds = new String(stringBuffer.toString().getBytes(), charSet);
        return initResult(ds);
    }

    public static Object doPostBodyData(String url, String bodydata, Map<String, String> headers) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new UTF8PostMethod(url);

        for(Map.Entry<String,String> entity : headers.entrySet()){
            postMethod.setRequestHeader(entity.getKey(), entity.getValue());
        }

        RequestEntity se = new StringRequestEntity(JSONObject.toJSON(bodydata).toString(), "text/json", "UTF-8");
        postMethod.setRequestEntity(se);

        httpClient.executeMethod(postMethod);
        byte[] result = postMethod.getResponseBody();

        return initResult(new String(result, "utf-8"));
    }

    public static Object doPostSetHeader(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        BufferedReader in = null;
        String result = "";
        try {
            RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            HttpPost method = new HttpPost(url);
            method.setConfig(config);
            method.addHeader("Accept", "text/plain");
            method.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

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
            if(null == entity){
                in = new BufferedReader(new InputStreamReader( null, "UTF-8"));
            } else{
                in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            }

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

    /**
     * 上传url文件到指定URL
     * @throws IOException
     */
    public static Object doPostUploadFile(String url, String filePath, String body, Map<String, String> headers) throws IOException {
        if(StringUtils.isEmpty(url))
            return null;

        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpPost httppost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            httppost.setConfig(requestConfig);
            for(Map.Entry<String,String> entity : headers.entrySet()){
                httppost.setHeader(entity.getKey(), entity.getValue());
            }

            FileBody bin = new FileBody(new File(filePath));
            StringBody comment = new StringBody(body, ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).addPart("comment", comment).build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(response.getEntity());
                    System.out.println(result);
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return initResult(result);
    }

    public static Object doGetSetHeader(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
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
            if(null == entity){
                in = new BufferedReader(new InputStreamReader( null, "UTF-8"));
            } else{
                in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            }
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

    public static Object doPostSingleKey(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
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
            if(null == entity){
                in = new BufferedReader(new InputStreamReader( null, "UTF-8"));
            } else{
                in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            }
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

    public static Object doGetSingleKey(String url, Map<String, String> data, Map<String, String> headers) throws Exception{
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
            if(null == entity){
                in = new BufferedReader(new InputStreamReader( null, "UTF-8"));
            } else{
                in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            }
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

    public static Object doPostSetHeaderCookie(String url, Map<String, String> data, Map<String, String> headers, Cookie[] cookie) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
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
        int statusCode = httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }

        return initResult(body);
    }

    public static Object doGetSetHeaderCookie(String url, Map<String, String> data, Map<String, String> headers, Cookie[] cookie) throws Exception{
        String params = "";
        for(Map.Entry<String,String> entity : data.entrySet()){
            if(StringUtils.isEmptyOrSpace(entity.getValue())){
                params += entity.getKey();
            }
        }

        if(!StringUtils.isEmptyOrSpace(params)) {
            url += "?" + params;
        }

        GetMethod method = new GetMethod(url);
        method.getParams().setParameter("Content-Type", "text/json");
        method.getParams().setParameter("Accept", "application/json");

        for(Map.Entry<String,String> entity : headers.entrySet()){
            method.setRequestHeader(entity.getKey(), entity.getValue());
        }

        HttpClient httpClient = new HttpClient();
        httpClient.getState().addCookies(cookie);
        int statusCode = httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }

        return initResult(body);
    }

    public static Object doPostJson(String url, Map<String, String> data) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
//        method.getParams().setParameter("Content-Type", "application/json;charset=utf-8");
//        method.getParams().setParameter("Accept", "application/json");


        method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        method.setRequestHeader("Accept", "application/json");

        log.info("url:" + url);

        for(Map.Entry<String,String> entity : data.entrySet()){
            if(StringUtils.isEmptyOrSpace(entity.getValue())){
                RequestEntity requestEntity = new ByteArrayRequestEntity(entity.getKey().getBytes());
                method.setRequestEntity(requestEntity);
            } else {
                method.addParameter(entity.getKey(), entity.getValue());
            }
            log.info("key:" + entity.getKey() + " , value:" + entity.getValue());
        }

//        HttpPost httpPost = new HttpPost(url);
//        CloseableHttpResponse response = null;
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        if (url.startsWith("https")) {
//            try {
//                httpClient = createSSLInsecureClient();
//            } catch (GeneralSecurityException e) {
////                logger.error("httpGet", e);
//            }
//        }
//        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
//                .setConnectTimeout(CONNECT_TIMEOUT).build();
//        httpPost.setConfig(requestConfig);
//        httpPost.addHeader("Content-Type", "application/JSON;charset=UTF-8");
//
//        try {
//            String body = JSON.toJSONString(data);
//
//            StringEntity requestEntity = new StringEntity(body, "utf-8");
//            httpPost.setEntity(requestEntity);
//
//            response = httpClient.execute(httpPost, new BasicHttpContext());
//
//            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                return null;
//            }
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                String httpResponse = EntityUtils.toString(entity, "utf-8");
//
//                if (StringUtils.isNotEmpty(httpResponse)) {
//                    JSONObject result = JSON.parseObject(httpResponse);
//                    if (result.containsKey("errcode") && result.getIntValue("errcode") != 0) {
//                        int errCode = result.getIntValue("errcode");
//                        String errMsg = result.getString("errmsg");
//                        throw new Exception(errMsg);
//                    }
//                    return result;
//                }
//            }
//        } catch (IOException e) {
//
//        } finally {
//            if (response != null)
//                try {
//                    response.close();
//                } catch (IOException e) {
//
//                }
//        }


        int statusCode = httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }

        return initResult(body);
    }

    public static Object doPostJsonHeader(String url, Map<String, String> data, Map<String, String> headerMaps) throws Exception{
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
//        method.getParams().setParameter("Content-Type", "application/json;charset=utf-8");
//        method.getParams().setParameter("Accept", "application/json");


        method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
        method.setRequestHeader("Accept", "application/json");



        for(Map.Entry<String,String> entity : data.entrySet()){
            if(StringUtils.isEmptyOrSpace(entity.getValue())){
                RequestEntity requestEntity = new ByteArrayRequestEntity(entity.getKey().getBytes());
                method.setRequestEntity(requestEntity);
            } else {
                method.addParameter(entity.getKey(), entity.getValue());
            }
        }

        for(Map.Entry<String,String> entity : headerMaps.entrySet()){
            method.setRequestHeader(entity.getKey(), entity.getValue());
        }

//        HttpPost httpPost = new HttpPost(url);
//        CloseableHttpResponse response = null;
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        if (url.startsWith("https")) {
//            try {
//                httpClient = createSSLInsecureClient();
//            } catch (GeneralSecurityException e) {
////                logger.error("httpGet", e);
//            }
//        }
//        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
//                .setConnectTimeout(CONNECT_TIMEOUT).build();
//        httpPost.setConfig(requestConfig);
//        httpPost.addHeader("Content-Type", "application/JSON;charset=UTF-8");
//
//        try {
//            String body = JSON.toJSONString(data);
//
//            StringEntity requestEntity = new StringEntity(body, "utf-8");
//            httpPost.setEntity(requestEntity);
//
//            response = httpClient.execute(httpPost, new BasicHttpContext());
//
//            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                return null;
//            }
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                String httpResponse = EntityUtils.toString(entity, "utf-8");
//
//                if (StringUtils.isNotEmpty(httpResponse)) {
//                    JSONObject result = JSON.parseObject(httpResponse);
//                    if (result.containsKey("errcode") && result.getIntValue("errcode") != 0) {
//                        int errCode = result.getIntValue("errcode");
//                        String errMsg = result.getString("errmsg");
//                        throw new Exception(errMsg);
//                    }
//                    return result;
//                }
//            }
//        } catch (IOException e) {
//
//        } finally {
//            if (response != null)
//                try {
//                    response.close();
//                } catch (IOException e) {
//
//                }
//        }


        int statusCode = httpClient.executeMethod(method);
        String body = method.getResponseBodyAsString();
        log.info("response=" + body);
        if(body.startsWith("jsonp%callback%({")){
            body = body.replace("jsonp%callback%(", "");
            body = body.substring(0, body.length() - 2);
            log.info("response=" + body);
        }

        return initResult(body);
    }

    /**
     * 发送http delete请求
     */
    public static Object DELETE(String url,Map<String,String> dataForm, Map<String,String> headers, String deleteExpectedStatusCode) throws Exception{
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(url);

        if(null != headers && 0 < headers.size()){
            for (Map.Entry<String, String> entry : headers.entrySet()){
                deleteMethod.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        if(dataForm!=null){
            Set<String> keys = dataForm.keySet();
            for(String key:keys){
                NameValuePair nameValuePair = new NameValuePair(key, (String) dataForm.get(key));
                data.add(nameValuePair);
            }
        }
        deleteMethod.setQueryString(data.toArray(new NameValuePair[0]));
        try {
            int statusCode = httpClient.executeMethod(deleteMethod);

            if(StringUtils.isNotEmpty(deleteExpectedStatusCode)){
                if(!String.valueOf(statusCode).equals(deleteExpectedStatusCode.trim())){
                    return initResult("Expect delete status code is " + deleteExpectedStatusCode + " ;but Actual status code is " + statusCode);
                }
            }

            // Read the response body.
            byte[] responseBody = deleteMethod.getResponseBody();
            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            String httpResponse = new String(responseBody);
            if (StringUtils.isNotEmpty(httpResponse)) {
                return initResult(httpResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            deleteMethod.releaseConnection();
        }
        return null;
    }

    /**
     * 发送 http put 请求，参数以原生字符串进行提交
     * @param url
     * @param data
     * @param headers
     * @return
     */
    public static Object PUT(String url, Map<String, String> data, Map<String,String> headers){
        HttpResponse response = null;

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : data.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(data.get(key));
            i++;
        }

        url += param;
        HttpPut httpput = null;
        try {
            if(UrlEncoderUtil.hasUrlEncoded(url)){
                httpput = new HttpPut(url);
            } else {
                URL newUrl = new URL(url);
                java.net.URI uri = new java.net.URI(newUrl.getProtocol(), newUrl.getUserInfo(), newUrl.getHost(), newUrl.getPort(), newUrl.getPath(), newUrl.getQuery(), null);
                httpput = new HttpPut();
                httpput.setURI(uri);
            }
        } catch (Exception ex){
            log.error(ex.getMessage());
            return initResult("URL is invalid. " + ex.getMessage());
        }

        //设置header
        httpput.setHeader("Content-type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //组织请求参数
        StringEntity stringEntity = null;
        if(null != data && 0 < data.size()){
            stringEntity = new StringEntity(JSON.toJSONString(data), "utf-8");
        }

        httpput.setEntity(stringEntity);
        String content = null;
        CloseableHttpResponse  httpResponse = null;
        try {
            //响应信息
            httpResponse = closeableHttpClient.execute(httpput);
            HttpEntity entity = httpResponse.getEntity();
            if(null == entity){
                content = "No Content";
            } else{
                content = EntityUtils.toString(entity, "utf-8");
            }
            return initResult(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            closeableHttpClient.close();  //关闭连接、释放资源
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送 http put 请求，参数以原生字符串进行提交
     * @param url
     * @param data
     * @param headers
     * @return
     */
    public static Object PutSingleBodyData(String url, Map<String, String> data, Map<String,String> headers){
        HttpResponse response = null;

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : data.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(data.get(key));
            i++;
        }

        url += param;
        HttpPut httpput = null;
        try {
            if(UrlEncoderUtil.hasUrlEncoded(url)){
                httpput = new HttpPut(url);
            } else {
                URL newUrl = new URL(url);
                java.net.URI uri = new java.net.URI(newUrl.getProtocol(), newUrl.getUserInfo(), newUrl.getHost(), newUrl.getPort(), newUrl.getPath(), newUrl.getQuery(), null);
                httpput = new HttpPut();
                httpput.setURI(uri);
            }
        } catch (Exception ex){
            log.error(ex.getMessage());
            return initResult("URL is invalid. " + ex.getMessage());
        }

        //设置header
        httpput.setHeader("Content-type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //组织请求参数
        StringEntity stringEntity = null;
        if(null != data && 0 < data.size()){
            String dataJson = data.get(Constants.bodyData);
            stringEntity = new StringEntity(dataJson, "utf-8");
        }

        httpput.setEntity(stringEntity);
        String content = null;
        CloseableHttpResponse  httpResponse = null;
        try {
            //响应信息
            httpResponse = closeableHttpClient.execute(httpput);
            HttpEntity entity = httpResponse.getEntity();
            if(null == entity){
                content = "No Content";
            } else{
                content = EntityUtils.toString(entity, "utf-8");
            }
            return initResult(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            closeableHttpClient.close();  //关闭连接、释放资源
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public static Object httpPost(String url, String data) throws Exception {
        return httpPost(url, JSONObject.parseObject(data));
    }

    public static Object httpPost(String url, Map<String, String> data) throws Exception {
        return httpPost(url, JSONObject.toJSONString(data));
    }

    public static Object httpPost(String url, JSONObject data) throws Exception {
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
                Object result = null;
                if (StringUtils.isNotEmpty(httpResponse)) {
                    result = initResult(httpResponse);
                }
                return result;
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
