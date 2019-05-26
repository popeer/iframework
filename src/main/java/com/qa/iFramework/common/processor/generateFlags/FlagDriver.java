package com.qa.iFramework.common.processor.generateFlags;

import com.qa.iFramework.common.processor.Driver;
import com.qa.iFramework.common.xml.XmlTcm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haijia on 8/7/18.
 */
public class FlagDriver {
    private static Logger log = LogManager.getLogger(FlagDriver.class);

    @Resource
    private XmlTcm xmlTcm;




    public static void main(String[] args) throws Exception{



        generateFlags("testCases/NLP/ocr-weixin");
    }

    public static String generateFlags(String path){

        List<File> files = new ArrayList<File>();

        try{
            Driver.getFileList(path, files);
        } catch (Exception ex){
            log.info(ex);
            return ex.getMessage();
        }

        try{
            for(File file : files){
//                TestCase testCase = xmlTcm.GetTestCase(file.getPath());
//                TestCaseNode node = (TestCaseNode) testCase;
//                if(StringUtils.isEmptyOrSpace(node.getFlags())){
//                    break;
//                }
//                String[] flags = node.getFlags().split(",");
//                if(null != flags && 0 < flags.length){
//                    for (String flag : flags) {
//
//                    }
//                }
            }
        } catch (Exception ex){
            log.error(ex.getStackTrace());
        }

        return "done";
    }
}
