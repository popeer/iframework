package com.qa.iFramework.common;

import com.qa.iFramework.Entity.TestCase;

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
