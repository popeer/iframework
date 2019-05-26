package com.qa.iFramework.common.Util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haijia on 12/6/17.
 */
public final class StringUtils {
    private static String[] EMPTY = new String[0];
    private static final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

    public StringUtils() {
    }

    public static boolean isEmpty(String value) {
        return null == value || value.isEmpty();
    }

    public static boolean isEmptyOrSpace(String value) {
        return isEmpty(value) || value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static String[] toArray(String value, String regex) {
        if(null == value) {
            return EMPTY;
        } else {
            value = value.trim();
            if(value.length() == 0) {
                return EMPTY;
            } else {
                String[] values = value.split(regex);

                for(int i = 0; i < values.length; ++i) {
                    values[i] = values[i].trim();
                }

                return values;
            }
        }
    }

    public static String[] toArray(Collection<String> strs) {
        if(strs != null && !strs.isEmpty()) {
            String[] arr = new String[strs.size()];
            strs.toArray(arr);
            return arr;
        } else {
            return EMPTY;
        }
    }

    public static List<String> toList(String value, String regex) {
        if(null == value) {
            return new ArrayList();
        } else {
            value = value.trim();
            return (List)(value.length() == 0?new ArrayList(): Splitter.on(regex).splitToList(value));
        }
    }

    public static List<String> toList(String[] values) {
        if(values == null) {
            return null;
        } else {
            ArrayList list = Lists.newArrayList();
            String[] arr$ = values;
            int len$ = values.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String value = arr$[i$];
                list.add(value);
            }

            return list;
        }
    }

    public static String toString(Object value, String defValue) {
        return value != null?value.toString():defValue;
    }

    public static boolean isAllChinese(String s) {
        if(isEmpty(s)) {
            return false;
        } else {
            char[] ch = s.toCharArray();

            for(int i = 0; i < ch.length; ++i) {
                char c = ch[i];
                if(!CharUtil.isCJKWord(c)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean hasChinese(String str) {
        if(isEmpty(str)) {
            return false;
        } else {
            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if(CharUtil.isCJKWord(c)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isAllNumeric(String str) {
        if(isEmpty(str)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[0-9]+");
            return pattern.matcher(str).matches();
        }
    }

    public static boolean isAllLetter(String str) {
        if(isEmpty(str)) {
            return false;
        } else {
            for(int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                if(!isLetter(c)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isLetter(char c) {
        return c >= 97 && c <= 122 || c >= 65 && c <= 90;
    }

    public static boolean hasNumeric(String str) {
        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if(isNumber(c)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isNumber(char c) {
        return c >= 48 && c <= 57;
    }

    public static boolean hasLetter(String str) {
        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if(isLetter(c)) {
                return true;
            }
        }

        return false;
    }

    public static String append(String str, String spliter, String word, boolean pre, boolean post) {
        if(isEmpty(str)) {
            return str;
        } else {
            StringBuffer sb = new StringBuffer();
            String[] ss = str.split(spliter);
            String[] arr$ = ss;
            int len$ = ss.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String s = arr$[i$];
                if(s.length() != 0) {
                    if(pre && !s.startsWith(word)) {
                        s = word + s;
                    }

                    if(post && !s.endsWith(word)) {
                        s = s + word;
                    }

                    sb.append(s).append(spliter);
                }
            }

            if(sb.length() > 0) {
                sb.delete(sb.length() - spliter.length(), sb.length());
            }

            return sb.toString();
        }
    }

    public static LinkedHashMap<Integer, String> trim(String src, Pattern p, StringBuffer sb) {
        LinkedHashMap ret = new LinkedHashMap();
        if(isNotEmpty(src)) {
            Matcher matcher = p.matcher(src);

            while(matcher.find()) {
                ret.put(Integer.valueOf(matcher.start()), matcher.group());
                matcher.appendReplacement(sb, "");
            }

            matcher.appendTail(sb);
        }

        return ret;
    }

    public static String replace(String str, Pattern srcPattern, String replaced) {
        Matcher matcher = srcPattern.matcher(str);
        return matcher.replaceAll(replaced);
    }

    public static List<String> findAllRegex(String str, Pattern srcPattern, int matchId) {
        ArrayList list = Lists.newArrayList();
        Matcher matcher = srcPattern.matcher(str);

        while(matcher.find()) {
            list.add(matcher.group(matchId));
        }

        return list;
    }

    public static void insert(StringBuffer str, LinkedHashMap<Integer, String> values) {
        Iterator i$ = values.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry value = (Map.Entry)i$.next();
            str.insert(((Integer)value.getKey()).intValue(), (String)value.getValue());
        }

    }

    public static int findFirstIndex(String str, String reg) {
        reg = reg.trim();
        int len = reg.length();

        for(int i = 0; i < len; ++i) {
            int firstIndex = str.indexOf(reg.substring(i, i + 1));
            if(firstIndex > 0) {
                return firstIndex;
            }
        }

        return -1;
    }

    public static int findLastIndex(String str, String reg) {
        int len = reg.length();

        for(int i = 0; i < len; ++i) {
            int lastIndex = str.lastIndexOf(reg.substring(i, i + 1));
            if(lastIndex > 0) {
                return lastIndex;
            }
        }

        return -1;
    }

    public static String[] copy(String[] values) {
        if(values == null) {
            return null;
        } else {
            String[] copy = new String[values.length];

            for(int i = 0; i < values.length; ++i) {
                copy[i] = values[i];
            }

            return copy;
        }
    }

    public static String encodeHex(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);

        for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            sb.append("\\u");
            sb.append(Character.forDigit(c >>> 12 & 15, 16));
            sb.append(Character.forDigit(c >>> 8 & 15, 16));
            sb.append(Character.forDigit(c >>> 4 & 15, 16));
            sb.append(Character.forDigit(c & 15, 16));
        }

        return sb.toString();
    }

    public static String decodeHex(String s) {
        Matcher m = reUnicode.matcher(s);
        StringBuffer sb = new StringBuffer(s.length());

        while(m.find()) {
            m.appendReplacement(sb, Character.toString((char)Integer.parseInt(m.group(1), 16)));
        }

        m.appendTail(sb);
        return sb.toString();
    }

    /**
     *
     * String转map
     * @param str
     * @return
     */
    public static Map<String,String> getStringToMap(String str){
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String,String> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split(":");
            //str2[0]为KEY,str2[1]为值
            map.put(str2[0],str2[1]);
        }
        return map;
    }

    public static Map<String,Object> getStringToMapObject(String str){
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String,Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split(":");
            //str2[0]为KEY,str2[1]为值
            map.put(str2[0],str2[1]);
        }
        return map;
    }

    /**
     * Map转String
     * @param map
     * @return
     */
    public static String getMapToString(Map<String,String> map){
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if(i != keyArray.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
