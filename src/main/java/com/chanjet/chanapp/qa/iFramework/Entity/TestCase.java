package com.chanjet.chanapp.qa.iFramework.Entity;

import com.alibaba.fastjson.JSONArray;
import com.chanjet.chanapp.qa.iFramework.Entity.RunStatus;
import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.ITestCase;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import com.chanjet.chanapp.qa.iFramework.common.xml.Parser;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 11/21/16.
 */
public class TestCase implements ITestCase {
    public static final String PassResultValue = "Pass";
    public static final String FailResultValue = "Fail";
    public static final String BlockResultValue = "Block";
    public static final String SkipResultValue = "Skip";
    public static final String NotRunYetResultValue = "NotRunYet";

    private String _caseId;

    private String _caseIdForLogger;

    private String _title = "";

    private String _result = NotRunYetResultValue;

    private Hashtable _properties = new Hashtable();

    private String _asmPath;

    private Hashtable _requirements = new Hashtable();

    private String _lastWarning = "";

    private boolean IsMultiResultTest = false;

    public String get_caseId() {
        return _caseId;
    }

    public void set_caseId(String _caseId) {
        this._caseId = _caseId;
    }

    public String get_caseIdForLogger() {
        return _caseIdForLogger;
    }

    public void set_caseIdForLogger(String _caseIdForLogger) {
        this._caseIdForLogger = _caseIdForLogger;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_result() {
        return _result;
    }

    public void set_result(String _result) {
        this._result = _result;
    }

    public Hashtable get_properties() {
        return _properties;
    }

    public void set_properties(Hashtable _properties) {
        this._properties = _properties;
    }

    public String get_asmPath() {
        return _asmPath;
    }

    public void set_asmPath(String _asmPath) {
        this._asmPath = _asmPath;
    }

    public Hashtable get_requirements() {
        return _requirements;
    }

    public void set_requirements(Hashtable _requirements) {
        this._requirements = _requirements;
    }

    public String get_lastWarning() {
        return _lastWarning;
    }

    public void set_lastWarning(String _lastWarning) {
        this._lastWarning = _lastWarning;
    }

    public boolean isMultiResultTest() {
        return IsMultiResultTest;
    }

    public void setMultiResultTest(boolean multiResultTest) {
        IsMultiResultTest = multiResultTest;
    }

    @Override
    public void BaseStartup(){

    }

    @Override
    public void BaseCleanup(){

    }

    @Override
    public Object Run(List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception{
        return null;
    }

    @Override
    public JSONArray Run(IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity) throws Exception{
        return null;
    }

    @Override
    public JSONArray Run(IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity, RunStatus runStatus) throws Exception{
        return null;
    }

    @Override
    public Object Run(){
        return null;
    }

    @Override
    public void CleanUp(){

    }
}
