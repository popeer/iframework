package com.chanjet.chanapp.qa.iFramework.common;

import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;

import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public interface ITestCaseManager {

    List<TestCase> GetTestCases(String suite);

    TestCase GetTestCase(String path);

    TestCase GetTestCase(Integer caseID);

    void ExportTestCases(String suite, String fileName);
}
