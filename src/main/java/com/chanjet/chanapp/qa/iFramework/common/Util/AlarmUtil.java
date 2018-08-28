package com.chanjet.chanapp.qa.iFramework.common.Util;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.common.Util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haijia on 5/24/18.
 */
public class AlarmUtil {
    private static Logger log = LogManager.getLogger(AlarmUtil.class);

    private static String url = "http://inte-mcclient.chanjet.com.cn:8713/app/collectMessage";

    public static boolean alerm(String message, String type){
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", message);
        params.put("type", type);
        params.put("authUsername", "tina");
        params.put("authPassword", "tina");

        String result = "";
        try{
            result = HttpHelper.doGet(url, params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(null != jsonObject){
                String alermResult = jsonObject.getString("result");
                String alermMessage = jsonObject.getString("msg");
                log.info("alert result " + alermResult);
                log.info("alert message " + alermMessage);
                if(alermResult == null || alermResult.equals("false")){
                    log.warn("alert fail");
                    return false;
                }
            } else {
                log.error("alerm return null at " + com.chanjet.chanapp.qa.iFramework.common.Util.DateUtil.getCurrentTime());
                return false;
            }

        } catch (Exception ex){
            log.error("Alerm Result: " + result +  " ; " + ex);
            return false;
        }

        return true;
    }

    public static boolean alerm2(String message, String pn){
        if(!JsonUtil.isJsonString(message)){
            JSONObject messageJson = new JSONObject();
            messageJson.put("message", message);
            message = messageJson.toJSONString();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("message", message);
        params.put("pn", pn);
        params.put("authUsername", "tina");
        params.put("authPassword", "tina");

        Object result = "";
        try{
            result = HttpHelper.doPostJson("http://inte-mcclient.chanjet.com.cn:8714/appPost/collectHeraMessage", params);
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(result));
            if(null != jsonObject){
                String alermResult = jsonObject.getString("result");
                String alermMessage = jsonObject.getString("msg");
                log.info("alert result " + alermResult);
                log.info("alert message " + alermMessage);
                if(alermResult == null || alermResult.equals("false")){
                    log.warn("alert fail");
                    return false;
                }
            } else {
                log.error("alerm return null at " + com.chanjet.chanapp.qa.iFramework.common.Util.DateUtil.getCurrentTime());
                return false;
            }

        } catch (Exception ex){
            log.error("Alerm Result: " + result +  " ; " + ex);
            return false;
        }

        return true;
    }
}
