package com.chanjet.chanapp.qa.iFramework.common;

import com.chanjet.chanapp.qa.iFramework.Entity.Result;
import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;
import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;

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
