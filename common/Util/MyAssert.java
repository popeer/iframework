package com.chanjet.chanapp.qa.iFramework.common.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;

import static org.testng.internal.EclipseInterface.*;

/**
 * Created by haijia on 7/3/17.
 */
public class MyAssert {
    private static Logger log = LogManager.getLogger(MyAssert.class);

    static public void assertEquals(Object actual, Object expected, String message) throws Exception{
        if((expected == null) && (actual == null)) {
            return;
        }
        if(expected != null) {
            if (expected.getClass().isArray()) {
                assertArrayEquals(actual, expected, message);
                return;
            } else if (expected.equals(actual)) {
                return;
            }
        }
        failNotEquals(actual, expected, message);
    }

    private static void assertArrayEquals(Object actual, Object expected, String message) throws Exception{
        //is called only when expected is an array
        if (actual.getClass().isArray()) {
            int expectedLength = Array.getLength(expected);
            if (expectedLength == Array.getLength(actual)) {
                for (int i = 0 ; i < expectedLength ; i++) {
                    Object _actual = Array.get(actual, i);
                    Object _expected = Array.get(expected, i);
                    try {
                        assertEquals(_actual, _expected, message);
                        return;
                    } catch (AssertionError ae) {
                        failNotEquals(actual, expected, message == null ? "" : message
                                + " (values at index " + i + " are not the same)");
                        return;
                    }
                }
                //array values matched
                return;
            } else {
                failNotEquals(Array.getLength(actual), expectedLength, message == null ? "" : message
                        + " (Array lengths are not the same)");
            }
        }
        failNotEquals(actual, expected, message);
    }

    static private void failNotEquals(Object actual , Object expected, String message) throws Exception{
        log.error("fail not equal! actual is " + actual + " ; expected is " + expected);
        fail(format(actual, expected, message), ExceptionCodes.NotEqual);
    }

    static private void failNotEquals(Object actual , Object expected, String message, Integer errorCode) throws Exception{
        log.error("fail not equal! actual is " + actual + " ; expected is " + expected);
        fail(format(actual, expected, message), errorCode);
    }

    static public void fail(String message, Integer errorCode) throws Exception{
        log.error(message);
        throw new MyCustomerException(errorCode, message);
    }

    static String format(Object actual, Object expected, String message) {
        String formatted = "";
        if (null != message) {
            formatted = message + " ";
        }

        return formatted + ASSERT_LEFT + expected + ASSERT_MIDDLE + actual + ASSERT_RIGHT;
    }

    static public void assertNotNull(Object object) throws Exception{
        assertNotNull(object, null);
    }

    static public void assertNotNull(Object object, String message) throws Exception{
        if (object == null) {
            String formatted = "";
            if(message != null) {
                formatted = message + " ";
            }
            fail(formatted + " But actual object should NOT be null", ExceptionCodes.IsNull);
        }
        assertTrue(object != null, message, ExceptionCodes.IsNull);
    }

    static public void assertTrue(boolean condition, String message) throws Exception{
        if(!condition) {
            failNotEquals( Boolean.valueOf(condition), Boolean.TRUE, message, ExceptionCodes.NotTrue);
        }
    }

    static public void assertTrue(boolean condition, String message, Integer errorCode) throws Exception{
        if(!condition) {
            failNotEquals( Boolean.valueOf(condition), Boolean.TRUE, message, errorCode);
        }
    }

    static public void assertTrue(boolean condition) throws Exception {
        assertTrue(condition, null, ExceptionCodes.NotTrue);
    }

}
