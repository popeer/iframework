package com.qa.iFramework.common.xml;

import com.qa.iFramework.Entity.TestCase;
import com.qa.iFramework.common.xml.Entity.TestCaseNode;
import com.qa.iFramework.common.xml.NodeInstance;
import com.qa.iFramework.common.xml.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import sun.net.ResourceManager;

import java.io.*;
import java.util.*;

/**
 * Created by haijia on 11/21/16.
 */
public class Config {
    private static Logger log = LogManager.getLogger(Config.class);
    private Document m_xdoc = null; //the config file itself (in xml)
    private String DEFAULT_GROUP = "Strings";       //the default String group
    private ResourceManager m_resourceManager;      // used to retrieve resources
    private static Dictionary<String, String> m_Parameters;

    public Config() {
    }

    public Config(String configFile) throws Exception {
        Document m_xdoc = DocumentHelper.parseText(configFile);
    }

    private static void InternalWarn(String message) {


        log.warn(message);

    }

    private static void InternalComment(String message) {
        // This should never happen but if it does write to the console since we have
        // nowhere else to write
        log.info(message);

    }

    private static String GetCustomParameter(String key) {
        String paramValue = "";

        if (null != m_Parameters) {
            // Look for the parameter on the command line to the invoker
            if (m_Parameters.get(key) != "") {
                paramValue = m_Parameters.get(key);
            }
        }

        return paramValue;
    }

    public TestCase GetSuite(String path) throws Exception{
        InputStream xmlsource;
//        if((null == sysPro.getProperty("filelocation")) || StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))){
//            xmlsource  = ClassLoader.getSystemResourceAsStream(path);
//        } else {
//            File caseFile = new File(sysPro.getProperty("filelocation") + path);
//            xmlsource = new FileInputStream(caseFile);
//        }
        File caseFile = new File(path);
        xmlsource = new FileInputStream(caseFile);
        String xmlDodumentString = inputStream2String(xmlsource);
        Parser parser = new Parser();
        parser.initXmlRule();
        TestCaseNode xmlTestCaseNode = parser.digester.parse(new StringReader(xmlDodumentString));

        return xmlTestCaseNode;
    }

    public String GetString(String key) {
        return null;
    }

    public String GetString(String key, String group, int localeId, boolean expandStringVars) {
        return null;
    }

    private String expandConfigVariables(String text, String group, int localeId) {
        return text;
    }


    public String GetString(String key, String group, int localeId)
    {
        return GetString(key, group, localeId, true);
    }


    public String GetString(String key, int localeId) {
        return GetString(key, DEFAULT_GROUP, localeId);
    }

    public String GetString(String key, String path) {
        return null;
    }

    public HashMap<String, String> GetStrings(String group) {
        return GetStrings(group);
    }

    public List<NodeInstance> GetSteps(String group, String xmlFile) {
        List<NodeInstance> result = new ArrayList<NodeInstance>();

        return result;
    }

    public  Dictionary GetStrings(String group, int localeId) {

        return null;
    }

    public String[] GetGroups(String group) {
        return null;
    }

    private void load(String configFile, String rootSearchDir) throws Exception {

        try
        {
            //load file

            parseIncludes(rootSearchDir);
        }
        catch (Exception e)
        {
            throw new Exception("Could not load config file " + configFile, e);
        }
    }


    private String locateFile(String searchDir, String filename) {

        return null;
    }

    private void parseIncludes(String rootSearchDir) {

    }

    static private String getGroupXPath(String group) {
        return null;
    }

    private Dictionary ParseParamters(String param) {
        return null;
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }

    public static Properties sysPro;

    static{
        InputStream inputStream = null;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream("properties/config.properties");
            sysPro = new Properties();
            sysPro.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
//            System.exit(-1);
            log.error(e.getMessage());
        }
    }

}
