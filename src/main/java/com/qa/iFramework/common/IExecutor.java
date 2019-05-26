package com.qa.iFramework.common;

import com.qa.iFramework.common.xml.Entity.Step;
import com.qa.iFramework.common.xml.Parser;

import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 11/21/16.
 */
public interface IExecutor {
    Object Execute(Step step, List<Object> preExecutedResults, Parser parser, Map<String, String> entry) throws Exception;
}
