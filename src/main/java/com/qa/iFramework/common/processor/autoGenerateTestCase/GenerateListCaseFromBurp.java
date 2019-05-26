package com.qa.iFramework.common.processor.autoGenerateTestCase;

import com.qa.iFramework.common.xml.Entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
/**
 * Created by haijia on 6/13/17.
 */
public class GenerateListCaseFromBurp {
    private static Logger log = LogManager.getLogger(GenerateListCaseFromBurp.class);

    public static void main(String[] args) throws Exception{

        TestCaseNode node = new TestCaseNode();
        List<Step> steps = new ArrayList<Step>();

        File f = new File("/Users/haijia/Downloads/GZQOnline");


        SAXReader reader = new SAXReader();

        Document doc = reader.read(f);

        Element root = doc.getRootElement();

        Element foo;

        try{
            for (Iterator i = root.elementIterator("item"); i.hasNext();) {
//                RequestDocument document = new RequestDocument();
                foo = (Element) i.next();
//                document.setGet_or_post(foo.elementText("method"));
//                document.setPath(foo.elementText("path"));
//                document.setHost(foo.elementText("host"));
                Step subStep = new Step();
                subStep.setUrl(foo.elementText("url"));
                subStep.setRule(foo.elementText("method"));
                String encryptedParameters = foo.elementText("request");
                String unencryptedParameters = new String(Base64.decodeBase64(encryptedParameters));
                Request request = parseRequest(unencryptedParameters);
                subStep.setRequest(request);
                steps.add(subStep);
            }

            generateNewTestCase(steps);

        } catch (Exception e) {
        e.printStackTrace();
        }
}

    private static Request parseRequest(String rawRequest){
        Request request = new Request();
        String newRequest = "";
        if(rawRequest.startsWith("POST")){
            String[] rawRequestArray = rawRequest.split("\n");
            if(0 < rawRequestArray.length){
                newRequest = rawRequestArray[rawRequestArray.length - 1];
            }

            String[] params = newRequest.split("&");
            for(String s : params){
                String[] keyValue = s.split("=");
                RequestParameter requestParameter = new RequestParameter();
                requestParameter.setName(keyValue[0]);
                if(keyValue.length == 2){
                    requestParameter.setValue(keyValue[1]);
                } else {
                    requestParameter.setValue("");
                }

                request.addParameters(requestParameter);
            }
        } else if(rawRequest.startsWith("GET")){

        }

        return request;
    }

    public Step parseRequestText(String path) throws Exception{
        FileInputStream inputStream;
        BufferedReader bufferedReader;

        inputStream = new FileInputStream(path);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        RequestDocument document = new RequestDocument();

        String str = null;
        while((str = bufferedReader.readLine()) != null)
        {
            log.info(str);
//            parseHeader(str, document);
        }

//        Step step = initCaseRequest(document);
//        generateNewTestCase(step);

        //close
        inputStream.close();
        bufferedReader.close();

        return null;
    }

    public static void generateNewTestCase(List<Step> steps){
//        String fileName = System.getProperty("user.dir") + "/testCases/Bizsvc/ActiveProduct.xml";
        String fileName = "/opt/iframework/testCases/Bizsvc/ActiveProduct.xml";
        try{
            SAXReader reader = new SAXReader();
            Document doc = reader.read(fileName);
            Element element = doc.getRootElement();

            int index = 0;
            for(Step step : steps) {
                Element xmlStep = (Element) element.selectSingleNode("/TestCaseNode/Step[1]");
                Element newStep = xmlStep.createCopy();
                element.add(newStep);
                newStep.attribute("url").setValue(step.getUrl());
                newStep.attribute("rule").setValue(step.getRule());
                newStep.attribute("name").setValue("step_" + index++);
                for (RequestParameter parameter : step.getRequest().getParameters()) {
                    Element requestParameter = (Element) element.selectSingleNode("/TestCaseNode/Step[last()]/Request");
                    requestParameter.addElement("Parameter").addAttribute("name", parameter.getName()).addAttribute("value", parameter.getValue());
                }

            }

            Random r = new Random();
//            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/testCases/TPlus/coverCIA" + r.nextInt(100) + ".xml");
            FileWriter fileWriter = new FileWriter( "/opt/iframework/testCases/GZQ/gzq" + r.nextInt(100) + ".xml");
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
