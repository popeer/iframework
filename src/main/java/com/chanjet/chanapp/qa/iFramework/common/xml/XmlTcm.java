package com.chanjet.chanapp.qa.iFramework.common.xml;

import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;
import com.chanjet.chanapp.qa.iFramework.common.ITestCaseManager;
import com.chanjet.chanapp.qa.iFramework.common.processor.TestCaseManagerBase;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.TestCaseNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public class XmlTcm extends TestCaseManagerBase implements ITestCaseManager {

    public XmlTcm()
    {
//        this.setConfig(new Config());
    }

    public TestCase GetTestCase(String path) {
        try {
            return getConfig().GetSuite(path);
        } catch (Exception ex){

        }
        return new TestCase();

    }

    /// <summary>
    /// Gets a test case from an ID
    /// </summary>
    /// <param name="caseID"></param>
    /// <returns></returns>
    public TestCase GetTestCase(Integer caseID)
    {
        TestCase tc = new TestCase();
        tc.set_caseId(caseID.toString());

        if (getConfig() != null)
            tc.set_title(getConfig().GetString(caseID.toString()));

        return tc;
    }

    /// <summary>
    /// Gets an array of test cases
    /// </summary>
    /// <param name="testCaseNode"></param>
    /// <returns>a template of all the test cases listed for this run; if none found, then empty</returns>
    public List<TestCase> GetTestCases(TestCaseNode testCaseNode)
    {
        String parameterValue;
        List<TestCase> testCases = new ArrayList<TestCase>();


        return testCases;

    } // GetTestCases


    /// <summary>
    /// Exports test cases to a file .  Not implemented since the cases are already in the file
    /// </summary>
    /// <param name="suite"></param>
    /// <param name="fileName"></param>
    public void ExportTestCases(String suite, String fileName)
    {
    }

}
