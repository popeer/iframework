package com.chanjet.chanapp.qa.iFramework.common.processor;

import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;
import com.chanjet.chanapp.qa.iFramework.common.ITestCaseManager;
import com.chanjet.chanapp.qa.iFramework.common.xml.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public class TestCaseManagerBase implements ITestCaseManager {
    private static Logger log = LogManager.getLogger(TestCaseManagerBase.class);
    @Resource
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    /// <summary>
    /// Gets a particular test case by its ID
    /// </summary>
    /// <param name="caseID"></param>
    /// <returns></returns>
    public TestCase GetTestCase(String path) {
        config.GetGroups(path);
        return null;
    }

    public TestCase GetTestCase(Integer caseID){
        return null;
    }

    /// <summary>
    /// Gets a list of test cases in a suite
    /// </summary>
    /// <param name="suite"></param>
    /// <returns></returns>
    public List<TestCase> GetTestCases(String suite) { return null; }

    /// <summary>
    /// Export a test case list into a file
    /// </summary>
    /// <param name="suite"></param>
    /// <param name="fileName"></param>
    public void ExportTestCases(String suite, String fileName)
    {
        try
        {
            Collection<TestCase> al = GetTestCases(suite);

            //// TODO: 11/22/16  
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }


}
