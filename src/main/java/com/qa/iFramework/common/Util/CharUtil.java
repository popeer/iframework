package com.qa.iFramework.common.Util;

import java.nio.charset.Charset;

/**
 * Created by haijia on 12/6/17.
 */
public final class CharUtil {
    public static final String UTF8 = "UTF-8";

    public CharUtil() {
    }

    public static boolean isEnter(String value) {
        if(value != null && value.length() == 1) {
            byte[] bs = value.getBytes(Charset.forName("US-ASCII"));
            return bs[0] == 63;
        } else {
            return false;
        }
    }

    public static boolean isEquals(Character c1, Character c2) {
        return c1 == null?c2 == null:(c2 == null?false:c1.equals(c2));
    }

    public static boolean isCJK(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return isCJKWord(ub) || isCJKSymbol(ub);
    }

    public static boolean isCJKWord(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return isCJKWord(ub);
    }

    private static boolean isCJKWord(Character.UnicodeBlock ub) {
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;
    }

    public static boolean isCJKSymbol(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return isCJKSymbol(ub);
    }

    private static boolean isCJKSymbol(Character.UnicodeBlock ub) {
        return ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }
}
