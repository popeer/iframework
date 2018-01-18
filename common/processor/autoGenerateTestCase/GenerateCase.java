package com.chanjet.chanapp.qa.iFramework.common.processor.autoGenerateTestCase;

import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.RequestParameter;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;

/**
 * Created by haijia on 4/28/17.
 */
public class GenerateCase {
    private static Logger log = LogManager.getLogger(GenerateCase.class);
    public static void generateNewTestCase(Step step){
        String fileName = System.getProperty("user.dir") + "/src/main/resources/testCases/Bizsvc/ActiveProduct.xml";

        try{
            SAXReader reader = new SAXReader();
            Document doc = reader.read(fileName);
            Element element = doc.getRootElement();

            Element xmlStep = (Element) element.selectSingleNode("/TestCaseNode/Step");
            xmlStep.attribute("url").setValue(step.getUrl());
            for(RequestParameter parameter : step.getRequest().getParameters()) {
                Element requestParameter = (Element) element.selectSingleNode("/TestCaseNode/Step/Request");
                requestParameter.addElement("Parameter").addAttribute("name", parameter.getName()).addAttribute("value", parameter.getValue());
            }

            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/src/main/resources/testCases/Bizsvc/TemplateOutput.xml");
            OutputFormat format = OutputFormat.createPrettyPrint();

            XMLWriter writer = new XMLWriter(System.out, format);
            writer.setWriter(fileWriter);
            writer.write(doc);
            writer.close();

        } catch (Exception ex){
            log.error(ex.getMessage());
        }
    }
}
