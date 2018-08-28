package com.chanjet.chanapp.qa.iFramework.common;

import com.alibaba.fastjson.JSONArray;
import com.chanjet.chanapp.qa.iFramework.Entity.RunStatus;
import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import com.chanjet.chanapp.qa.iFramework.common.xml.Parser;

import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 11/21/16.
 */
public interface ITestCase {

    void BaseStartup();

    void BaseCleanup();

    Object Run(List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception;

    Object Run();

    JSONArray Run(IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity) throws Exception;

    JSONArray Run(IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity, RunStatus runStatus) throws Exception;

    void CleanUp();
}
