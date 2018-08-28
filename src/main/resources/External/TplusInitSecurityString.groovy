package External

import org.apache.commons.codec.binary.Base64

import java.security.MessageDigest
import java.text.SimpleDateFormat
/**
 * Created by haijia on 5/19/18.
 */

    def getSecurityString(String orgId, String userId){
        def securityString = "{\"orgId\":\"" + orgId + "\",userId\":" + userId + "\",\"date\":\"" + today() + "\"}";
        securityString = encodeBASE64(hexMD5(securityString) + "," + securityString);
        return securityString;
    }

    def getSecurityString2(String orgId, String userId) {
        def securityString = "{\"orgId\": \"" + orgId + "\",\"userid\": \"" + userId + "\",\"date\": \"" + today() + "\"}";
        securityString = encodeBASE64(hexMD5(securityString) + "," + securityString);
        return securityString;
    }

    def getSecurityStringWithAppKey(String appkey, String orgId, String userId) {
        def securityString = "{\"appkey\":" + appkey + "\",orgId\":\"" + orgId + "\",userid\":" + userId + "\",\"date\":\"" + today() + "\"}";
        securityString = encodeBASE64(hexMD5(securityString) + "," + securityString);
        return securityString;
    }


    def today()
    {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
        Calendar lastDate = Calendar.getInstance();
        str = sdf.format(lastDate.getTime());
        return str;

    }

    /**
     * Build an hexadecimal MD5 hash for a String
     * @param value The String to hash
     * @return An hexadecimal Hash
     */
    def hexMD5(String value) {
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
     * Write a byte array as hexedecimal String.
     */
    def byteToHexString(byte[] bytes) {
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

