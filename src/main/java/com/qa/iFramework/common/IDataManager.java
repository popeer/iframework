package com.qa.iFramework.common;

import com.qa.iFramework.Entity.Result;
import com.qa.iFramework.Entity.TestCase;
import com.qa.iFramework.common.DTO.ResultDto;
import com.qa.iFramework.common.processor.CommandEntity;

import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public interface IDataManager {

    List<String> queryCasesName();

    List<TestCase> queryCases(String path);

    int insertCasesToDB(List<TestCase> cases);

    ResultDto storeExecuteResult(Result result, CommandEntity commandEntity);
}
