package com.qa.iFramework.common.Util;

/**
 * Created by haijia on 6/21/18.
 */
public class JsonUtil {
    public static boolean isJsonString(Object input){
        if(null == input){
            return false;
        }

        String s = String.valueOf(input);
        if(s.startsWith("{") && s.endsWith("}")){
            return true;
        }

        if(s.startsWith("[") && s.endsWith("]")){
            return true;
        }

        return false;
    }

    public static boolean isJsonObject(Object input){
        if(null == input){
            return false;
        }

        String s = String.valueOf(input);
        if(s.startsWith("{") && s.endsWith("}")){
            return true;
        }

        return false;
    }

    public static boolean isJsonArray(Object input){
        if(null == input){
            return false;
        }

        String s = String.valueOf(input);

        if(s.startsWith("[") && s.endsWith("]")){
            return true;
        }

        return false;
    }

    public static boolean isJsonString(Object input, Object aa){
        if(null == input){
            return false;
        }

        String s = String.valueOf(input);
        if(s.startsWith("{") && s.endsWith("}")){
            return true;
        }

        if(s.startsWith("[") && s.endsWith("]")){
            return true;
        }

        return false;
    }
}
