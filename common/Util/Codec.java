package com.chanjet.chanapp.qa.iFramework.common.Util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by haijia on 4/21/17.
 */
public class Codec {
    /**
     * @return an UUID String
     */
    public static String UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Encode a String to base64
     * @param value The plain String
     * @return The base64 encoded String
     */
    public static String encodeBASE64(String value) {
        try {
            return new String(Base64.encodeBase64(value.getBytes("utf-8")));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Encode binary data to base64
     * @param value The binary data
     * @return The base64 encoded String
     */
    public static String encodeBASE64(byte[] value) {
        return new String(Base64.encodeBase64(value));
    }

    /**
     * Decode a base64 value
     * @param value The base64 encoded String
     * @return decoded binary data
     */
    public static byte[] decodeBASE64(String value) {
        try {
            return Base64.decodeBase64(value.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Build an hexadecimal MD5 hash for a String
     * @param value The String to hash
     * @return An hexadecimal Hash
     */
    public static String hexMD5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes("utf-8"));
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Build an hexadecimal SHA1 hash for a String
     * @param value The String to hash
     * @return An hexadecimal Hash
     */
    public static String hexSHA1(String value) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = new byte[40];
            md.update(value.getBytes("utf-8"));
            digest = md.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Write a byte array as hexedecimal String.
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            int v = bytes[i];
            if (v < 0) {
                v += 256;
            }
            String n = Integer.toHexString(v);
            if(n.length() == 1)
                n = "0" + n;
            builder.append(n);
        }

        return builder.toString();
    }

    /**
     * Transform an hexadecimal String to a byte array.
     */
    public static byte[] hexStringToByte(String hexString) {
        byte[] raw = new byte[16];
        for(int i=0;i<16;i++) {
            raw[i] = Integer.decode("0x" + hexString.substring(i*2, i*2+2)).byteValue();
        }
        return raw;
    }

    public static byte[] hmacSha1(byte[] input, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

}
