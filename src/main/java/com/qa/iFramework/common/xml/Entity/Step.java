package com.qa.iFramework.common.xml.Entity;

import com.qa.iFramework.Entity.TestCase;
import com.qa.iFramework.common.IExecutor;
import com.qa.iFramework.common.xml.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 11/23/16.
 */
public class Step extends TestCase {
    private static Logger log = LogManager.getLogger(Step.class);
    private String name;
    private String id;
    private String proxy;
    private int rerun;
    private String url;
    private Request request;
    private Response response;
    private IExecutor executor;
    private String protocol;
    private String rule;
    private String action;
    private Object result;
    private String pwd;
    private String uid;
    private String dburl;
    private String domainName;
    private String clsName;
    private String method;
    private String parameterType;
    private String headers;
    private String type;
    private String caseNodeName;
    private String headerInit;
    private String voidReturn;
    private String sqlDriver;
    private String groovyMethodName;
    private String groovyPath;
    private String groovyArgs;
    private String desc;
    private String staticCls;
    private String deleteStatusCode;
    private String product;
    private WebDriver webDriver;
    //指明该STEP的URL不需要被CommandEntity的DomainName替换掉
    private String urlSolid;
    //class类构造函数参数构造取值逻辑
    private String parameterInit;

    public String getParameterInit() {
        return parameterInit;
    }

    public void setParameterInit(String parameterInit) {
        this.parameterInit = parameterInit;
    }

    public String getUrlSolid() {
        return urlSolid;
    }

    public void setUrlSolid(String urlSolid) {
        this.urlSolid = urlSolid;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDeleteStatusCode() {
        return deleteStatusCode;
    }

    public void setDeleteStatusCode(String deleteStatusCode) {
        this.deleteStatusCode = deleteStatusCode;
    }

    public String getStaticCls() {
        return staticCls;
    }

    public void setStaticCls(String staticCls) {
        this.staticCls = staticCls;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroovyMethodName() {
        return groovyMethodName;
    }

    public void setGroovyMethodName(String groovyMethodName) {
        this.groovyMethodName = groovyMethodName;
    }

    public String getSqlDriver() {
        return sqlDriver;
    }

    public void setSqlDriver(String sqlDriver) {
        this.sqlDriver = sqlDriver;
    }

    public String getVoidReturn() {
        return voidReturn;
    }

    public void setVoidReturn(String voidReturn) {
        this.voidReturn = voidReturn;
    }

    public String getHeaderInit() {
        return headerInit;
    }

    public void setHeaderInit(String headerInit) {
        this.headerInit = headerInit;
    }

    public String getCaseNodeName() {
        return caseNodeName;
    }

    public void setCaseNodeName(String caseNodeName) {
        this.caseNodeName = caseNodeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getHeaders(){
        return this.headers;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getDburl() {
        return dburl;
    }

    public void setDburl(String dburl) {
        this.dburl = dburl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public IExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(IExecutor executor) {
        this.executor = executor;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public int getRerun() {
        return rerun;
    }

    public void setRerun(int rerun) {
        this.rerun = rerun;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getID(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getResult(){
        return result;
    }

    public void setResult(Object result){
        this.result = result;
    }

    public String getGroovyPath() {
        return groovyPath;
    }

    public void setGroovyPath(String groovyPath) {
        this.groovyPath = groovyPath;
    }

    public String getGroovyArgs() {
        return groovyArgs;
    }

    public void setGroovyArgs(String groovyArgs) {
        this.groovyArgs = groovyArgs;
    }

    @Override
    public String toString() {
        return "Step{" +
                "name='" + name + '\'' +
                "protocol='" + protocol + '\'' +
                ", request=" + request +
                ", response=" + response +
                '}';
    }

    @Override
    public Object Run(List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception{

        Object result = null;
        try{
            result = executor.Execute(this, preExecutedResults, parser, entry);

        } catch (Exception ex){
            log.error(ex);
            throw ex;
        }
        this.setResult(result);
        return result;

    }

    public List<ResponseParameter> getExpectResponseValues(){
        return getExpectDataFromCase();
    }

    private List<ResponseParameter> getExpectDataFromCase(){
        Response response = this.getResponse();
        if(null == response){
            return null;
        }
        return response.getParameters();
    }

}
