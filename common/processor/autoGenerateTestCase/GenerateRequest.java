package com.chanjet.chanapp.qa.iFramework.common.processor.autoGenerateTestCase;

import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by haijia on 4/27/17.
 */
public class GenerateRequest {
    private static Logger log = LogManager.getLogger(GenerateRequest.class);
    final static String flag = "signed";

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
            parseHeader(str, document);
        }

       Step step = initCaseRequest(document);
        generateNewTestCase(step);

        //close
        inputStream.close();
        bufferedReader.close();

        return step;
    }

    public void parseResponseText(String path, Step step) throws Exception{
        FileInputStream inputStream;
        BufferedReader bufferedReader;

        inputStream = new FileInputStream(path);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        ResponseDocument document = new ResponseDocument();

        String str = null;
        while((str = bufferedReader.readLine()) != null)
        {
            log.info(str);
            parseResponse(str, document);
        }

        ResponseParameter responseParameter = new ResponseParameter();
//        responseParameter.setName();



    }

    public static void main(String[] args) throws Exception{
        TestCaseNode node = new TestCaseNode();
        List<Step> steps = new ArrayList<Step>();

        Step step = new Step();

        GenerateRequest r = new GenerateRequest();
        step = r.parseRequestText("/Users/haijia/Desktop/query1.txt");
    }

    private void parseHeader(String str, RequestDocument document){

            if(0 == str.length()) {
                document.setFlag(flag);
            } else if(str.startsWith("POST ")) {
                document.setGet_or_post("POST");
                document.setPath(str.replace("POST ", "").replace(" HTTP/1.0", ""));
            } else if(str.startsWith("GET ")) {
                document.setGet_or_post("GET");
                document.setPath(str.replace("GET ", "").replace(" HTTP/1.0", ""));
            } else if(str.startsWith("Host: ")) {
                document.setHost(str.replace("Host: ", ""));
            } else {
            if ("signed" == document.getFlag()) {
                document.setParams(str);
            }
        }
    }

    private void parseResponse(String str, ResponseDocument document){

        if(0 == str.length()) {
            document.setFlag(flag);
//        } else if(str.startsWith("POST ")) {
//            document.setGet_or_post("POST");
//            document.setPath(str.replace("POST ", "").replace(" HTTP/1.0", ""));
        } else {
            if ("signed" == document.getFlag()) {
                document.setResult(JSONObject.parseObject(str));
            }
        }
    }

    private Step initCaseRequest(RequestDocument document){

        Step step = new Step();
        step.setUrl(document.getHost() + document.getPath());

        Request request = parseRequest(document.getParams());

        step.setRequest(request);
        return step;
    }

    private Request parseRequest(String rawRequest){

        Request request = new Request();
        String[] params = rawRequest.split("&");
        for(String s : params){
            String[] keyValue = s.split("=");
            RequestParameter requestParameter = new RequestParameter();
            requestParameter.setName(keyValue[0]);
            requestParameter.setValue(keyValue[1]);
            request.addParameters(requestParameter);
        }

        return request;
    }

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

            Random r = new Random();
            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/src/main/resources/testCases/CIA/Cia" + r.nextInt(100) + ".xml");
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
