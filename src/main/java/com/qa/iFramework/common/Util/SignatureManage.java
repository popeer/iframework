package com.qa.iFramework.common.Util;

//import com.google.gson.JsonObject;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.common.Util.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.util.encoders.Base64;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SignatureManage {

	public SignatureManage(){

	}

	public SignatureManage(String abc){

	}
	
	/**
	 * 签名
	 *
	 * @param datas 签名内容
	 * @param pemFile 私钥文件路径
	 * @param paramsMap 签名附加内容
	 * @return 签名
	 * @throws Exception
	 */
	public String sign(String datas, String pemFile,Map<String, Object> paramsMap) throws Exception {
		PrivateKey privateKey = loadPrivateKeyOfPem(pemFile);
		Map<String, Object> claims = com.qa.iFramework.common.Util.JwtParamBuilder.build().setSubject("e-commerce").setExpirySeconds(30000)
				.getClaims();
		// 默认规则是当前参数+appsecret，组成签名的原值
		String value = datas;
		claims.put("datas", getMD5(value));
		if (paramsMap != null && paramsMap.size() > 0) {
			Iterator<String> it = paramsMap.keySet().iterator();
			String key = "";
			while (it.hasNext()) {
				key = it.next();
				claims.put(key, paramsMap.get(key));
			}
		}
		// String compactJws = Jwts.builder().signWith(SignatureAlgorithm.PS256,
		// privateKey)
		// .setClaims(claims).compressWith(CompressionCodecs.DEFLATE).compact();
		String compactJws = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.PS256, privateKey).compact();
		return compactJws;
	}

	/**
	 * 计算MD5
	 *
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	private String getMD5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] buf = null;
		buf = str.getBytes("utf-8");
		MessageDigest md5 = null;
		md5 = MessageDigest.getInstance("MD5");
		md5.update(buf);
		byte[] tmp = md5.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : tmp) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	/**
	 * 读取PEM编码格式
	 *
	 * @param pemFile cjet_pri.pkcs8私钥文件路径
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	protected PrivateKey loadPrivateKeyOfPem(String pemFile)
			throws IOException, FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		String fileName = pemFile;
		org.bouncycastle.util.io.pem.PemReader reader = new org.bouncycastle.util.io.pem.PemReader(new FileReader(fileName));
		byte[] privateKeyBytes = reader.readPemObject().getContent();
		reader.close();
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(spec);
		return privateKey;
	}
	
	 /// <summary>
    /// 生成Authorization头，根据token传值与否，决定采用签名方式1  还是签名方式2
    /// </summary>
    /// <param name="appKey"></param>
    /// <param name="appSecret"></param>
    /// <param name="orgId"></param>
    /// <param name="pemFile">私钥文件物理路径</param>
    /// <param name="token">获取到的token值</param>
    /// <returns></returns>
    public String CreateAuthorizationHeader(String appKey, String appSecret, String orgId,String pemFile, String token) throws Exception
    {
        JSONObject signJsonObj = new JSONObject();
        signJsonObj.put("appkey", appKey);
        signJsonObj.put("orgid", orgId);
        signJsonObj.put("appsecret", appSecret);
        String datas = signJsonObj.toString();
        String signStr="";
        if (token==null||token=="")
            signStr = this.sign(datas, pemFile,null);//签名方式1
        else {
        	Map<String,Object> tokenInfo=new HashMap<String,Object>();
        	tokenInfo.put("access_token", token);
        	signStr = this.sign(datas, pemFile, tokenInfo);//签名方式2
        }
		JSONObject authInfoJsonObj = new JSONObject();
        authInfoJsonObj.put("appKey", appKey);
        authInfoJsonObj.put("authInfo", signStr);
        authInfoJsonObj.put("orgId", orgId);
        String authStr = authInfoJsonObj.toString();
        String encode = new String(Base64.encode(authStr.getBytes()));
        return encode;
    }
}