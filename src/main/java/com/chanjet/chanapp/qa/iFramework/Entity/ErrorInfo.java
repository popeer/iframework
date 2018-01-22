package com.chanjet.chanapp.qa.iFramework.Entity;

import com.chanjet.chanapp.qa.iFramework.common.IErrorDefBase;

import java.io.Serializable;

/**
 * Created by haijia on 11/29/16.
 */
public class ErrorInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	private String msg;
	private Object actualResult;

	public ErrorInfo() {
		this.code = 0;
		this.msg = "";
	}
	
	public ErrorInfo(IErrorDefBase errDef) {
		this.setCode(errDef.getCode());
		this.setMsg(errDef.getMsg());
	}
	
	public ErrorInfo(Integer code, String msg) {
		this.setCode(code);
		this.setMsg(msg);
	}

	public ErrorInfo(Integer code, String msg, Object actualResult) {
		this.setCode(code);
		this.setMsg(msg);
		this.setActualResult(actualResult);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getActualResult() {
		return this.actualResult;
	}

	public void setActualResult(Object actualResult) {
		this.actualResult = actualResult;
	}

	@Override
	public String toString() {
		return "{" +
				"\"code\":\"" + code + "\"" +
				", \"msg\":\"" + msg + "\"" +
				", \"actualResult\":" + actualResult +
				"}";
	}
}
