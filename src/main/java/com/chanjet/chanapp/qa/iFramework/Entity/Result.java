package com.chanjet.chanapp.qa.iFramework.Entity;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.Entity.ErrorInfo;

import java.io.Serializable;

/**
 * 服务处理结果的基本描述
 * @author haijia on 11/29/16.
 *
 */
public class Result implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//default false
	private boolean result;
	
	//error information
	private ErrorInfo error;

	String node_name;
	String step_name;
	String interfaceName;
	String environment;
	String dateTime;
	String operator;
	//接口路径
	String module;
	Object resultDetail;
	//接口中文描述
	String module_desc;

	public String getModule_desc() {
		return module_desc;
	}

	public void setModule_desc(String module_desc) {
		this.module_desc = module_desc;
	}

	public Object getResultDetail() {
		return resultDetail;
	}

	public void setResultDetail(Object resultDetail) {
		this.resultDetail = resultDetail;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getStep_name() {
		return step_name;
	}

	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}


	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	
	public ErrorInfo getError() {
		return error;
	}

	public void setError(ErrorInfo error) {
		this.error = error;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public String toString() {
		JSONObject result = new JSONObject();
		result.put("Result", result);
		result.put("ResultDetail", resultDetail);
		result.put("Error", error);
		result.put("Node_name", node_name);
		result.put("StepName", step_name);
		result.put("interfaceName", interfaceName);
		result.put("ModuleName", module);
		result.put("Operator", operator);
		result.put("DateTime", dateTime);
		result.put("Environment", environment);
		return result.toJSONString();
//		return "{" +
//				" \"ResultDetail\":" + result + "\"" +
//				", \"Error\":" + error + "\"" +
//				", \"Node_name\":" + node_name + "\"" +
//				", \"StepName\":" + step_name + "\"" +
//				", \"interfaceName\":" + interfaceName + "\"" +
//				", \"ModuleName\":" + module + "\"" +
//				", \"Operator\":" + operator + "\"" +
//				", \"DateTime\":" + dateTime + "\"" +
//				", \"Environment\":" + environment + "\"" +
//				"}";
	}
}

