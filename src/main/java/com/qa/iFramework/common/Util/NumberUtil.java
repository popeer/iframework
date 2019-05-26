package com.qa.iFramework.common.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
    public static boolean isNumeric(String str) {
        if(StringUtils.isEmptyOrSpace(str)){
            return false;
        }

        Pattern pattern1 = Pattern.compile("(^[\\-0-9][0-9]*(.[0-9]+)?)%?$");
//        增加对千分位数字的支持
        Pattern pattern2 = Pattern.compile("(([-]?[1-9][0-9]{0,2}(,\\d{3})*)|0)(\\.\\d{1,2})?");
        Matcher isNum = pattern1.matcher(str);
        Matcher isNum2 = pattern2.matcher(str);
        if( !isNum.matches() && !isNum2.matches()){
            return false;
        }
        return true;
    }
}
