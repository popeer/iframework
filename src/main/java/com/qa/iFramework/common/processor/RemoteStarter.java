package com.qa.iFramework.common.processor;

import com.alibaba.citrus.util.collection.ArrayHashMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.iFramework.Entity.ErrorInfo;
import com.qa.iFramework.Entity.Result;
import com.qa.iFramework.Entity.RunStatus;
import com.qa.iFramework.Entity.TestCase;
import com.qa.iFramework.common.DAO.Base.*;
import com.qa.iFramework.common.DTO.*;
import com.qa.iFramework.common.IDataManager;
import com.qa.iFramework.common.IRemoteStarter;
import com.qa.iFramework.common.IVerifier;
import com.qa.iFramework.common.Util.DateUtil;
import com.qa.iFramework.common.Util.ExceptionCodes;
import com.qa.iFramework.common.Util.StringUtils;
import com.qa.iFramework.common.xml.Entity.TestCaseNode;
import com.qa.iFramework.common.xml.XmlTcm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.*;

import static com.qa.iFramework.common.processor.Driver.sysPro;

/**
 * Created by haijia on 6/27/17.
 */
@Path("/iframework")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_FORM_URLENCODED+"; charset=UTF-8", MediaType.WILDCARD })
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
public class RemoteStarter implements IRemoteStarter {
    private static Logger log = LogManager.getLogger(RemoteStarter.class);

    @Resource
    private ISummaryDao ISummaryDaoImpl;

    @Resource
    private IVersionDao IVersionDaoImpl;

    @Resource
    private IResultDao IResultDaoImpl;

    @Resource
    private IFlagsDao iFlagsDao;

    @Resource
    private INodeManagerDao iNodeManagerDao;

    @Resource
    private XmlTcm xmlTcm;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    Driver driver;

    public IVerifier getVerifier() {
        return verifier;
    }

    public void setVerifier(IVerifier verifier) {
        this.verifier = verifier;
    }


    IVerifier verifier;

    public IDataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(IDataManager dataManager) {
        this.dataManager = dataManager;
    }


    IDataManager dataManager;

    @GET
    @Path("irun")
    public String run (
            @QueryParam("dburl") String dburl,
            @QueryParam("uid") String uid,
            @QueryParam("pwd") String pwd,
            @QueryParam("dn") String dn,
            @QueryParam("pn") String productName,
            @QueryParam("dns") String dns,
            @QueryParam("path") String path,
            @QueryParam("user") String operator,
            @QueryParam("data") String dataSource,
            @QueryParam("pre") String preResults) {

        if(StringUtils.isEmptyOrSpace(path)){
            log.warn("Path is a must but you don't!!!");
            return "Path is a must but you don't!!!";
        }

        if(StringUtils.isEmptyOrSpace(productName)){
            log.warn("pn(productName) is a must but you don't!!!");
            return "pn(productName) is a must but you don't!!!";
        }

        CommandEntity commandEntity = new CommandEntity();

        if(!StringUtils.isEmptyOrSpace(dburl)){
            commandEntity.setDbURL(dburl);
        }

        if(!StringUtils.isEmptyOrSpace(uid)){
            commandEntity.setUid(uid);
        }

        if(!StringUtils.isEmptyOrSpace(pwd)){
            commandEntity.setPwd(pwd);
        }

        if(!StringUtils.isEmptyOrSpace(dn)){
            commandEntity.setDomainName(dn);
        }

        if(!StringUtils.isEmptyOrSpace(productName)){
            JSONArray pn = new JSONArray();
            pn.add(productName.toLowerCase());
            commandEntity.setProductName(pn.toString().toLowerCase());
        }

        if(!StringUtils.isEmptyOrSpace(dns)){
            commandEntity.setDns(dns.toLowerCase());
        }

        if(!StringUtils.isEmptyOrSpace(operator)){
            commandEntity.setUser(operator.toLowerCase());
        }

        if(!StringUtils.isEmptyOrSpace(dataSource)){
            commandEntity.setDataSource(dataSource);
        }

        if(!StringUtils.isEmptyOrSpace(preResults)){
            commandEntity.setPreResults(preResults);
        }

        List<File> files = new ArrayList<File>();
        try{
            //获取指定路径下全部文件及子文件夹下全部测试用例文，填充files对象
            Driver.getFileList(path, files);
        } catch (Exception ex){
            log.info(ex);
            Result result = new Result();
            result.setResult(false);
            result.setDateTime(DateUtil.getCurrentTime());
            result.setError(new ErrorInfo(ExceptionCodes.FileException, ex.getMessage(), "{}"));
            return result.toString();
        }

        try{
            return driver.Execute(files, verifier, dataManager, commandEntity);
        } catch (Exception ex){
            log.error(ex);
            if(null != ex.getMessage() && StringUtils.isNotEmpty(ex.getMessage())){
                return ex.getMessage();
            }
            return ex.toString();
        }
    }


    @GET
    @Path("isummary")
    public String getSummary(@QueryParam("pn") String productname, @QueryParam("date") @DefaultValue("2017-09-26 10:13") String rundate){

        if(StringUtils.isEmptyOrSpace(productname) && StringUtils.isEmptyOrSpace(rundate)){
            log.error("Need to set pn or date or both");
            return "Need to set pn or date or both";
        }

        List<SummaryDto> summaryDtoList = null;
        if(!StringUtils.isEmptyOrSpace(productname) && !StringUtils.isEmptyOrSpace(rundate)){
            //通过产品线名称和日期来过滤
            summaryDtoList = ISummaryDaoImpl.findSummary(productname, rundate);

        } else if(!StringUtils.isEmptyOrSpace(productname) && StringUtils.isEmptyOrSpace(rundate)){
            //通过产品线名称查找最近的产品线执行统计
            summaryDtoList = ISummaryDaoImpl.findLatestSummary(productname);

        } else if(StringUtils.isEmptyOrSpace(productname) && !StringUtils.isEmptyOrSpace(rundate)){
            //通过日期查找全部产品线执行统计
            summaryDtoList = ISummaryDaoImpl.findTotalSummaryByDate(rundate);
        }

        return exchangeToResultString(summaryDtoList);
    }

    private String exchangeToResultString(List<SummaryDto> summaryDtoList){
        if(null == summaryDtoList){
            return "No summary returned from DB!!";
        }

        RunStatus runStatus = new RunStatus(0,0,0,0);
        for(SummaryDto summaryDto : summaryDtoList){
            runStatus.Passed += summaryDto.getPass();
            runStatus.Failed += summaryDto.getFail();
            runStatus.Skipped += summaryDto.getSkip();
        }

        return "Summary: Pass:" + runStatus.Passed + ", Failed: " + runStatus.Failed + ", Skipped: " + runStatus.Skipped;

    }

    @GET
    @Path("idict")
    public String getDirectoriesFiles(@QueryParam("pn") String productname) throws Exception{

        if(StringUtils.isEmptyOrSpace(productname)){
            return "pn is a must";
        }

        JSONObject result = new JSONObject();
        Driver.getDictFileList(productname,result);

        return result.toJSONString();
    }

    @GET
    @Path("ixml")
    public String showXml(@QueryParam("path") String path){

        if(StringUtils.isEmptyOrSpace(path)){
            return "path is a must";
        }

        return Driver.getXmlFile(path);
    }

    @GET
    @Path("ilist")
    public String getHistoryReportList(@QueryParam("pn")String productname, @QueryParam("date")String date){

        JSONArray result = new JSONArray();

        if(StringUtils.isEmptyOrSpace(productname)){
            JSONObject item = new JSONObject();
            item.put("error", "pn is a must");
            result.add(item);
            return result.toJSONString();
        }

        String dateParam = "";

        if(StringUtils.isEmptyOrSpace(date)){
            dateParam = DateUtil.format(new Date(), "yyyy-MM-dd");
        } else{
            dateParam = date;
        }

        List<VersionDto> versionDtoList = IVersionDaoImpl.findVersionByPn(productname.toLowerCase(), dateParam);
        if((null != versionDtoList) && 0 < versionDtoList.size()){

            for(VersionDto dto : versionDtoList){
                JSONObject item = new JSONObject();
                item.put("id", dto.getId());
                item.put("date", dto.getDate().toString());
                item.put("name", dto.getDate());
                result.add(item);
            }
            return result.toJSONString();
        }


        JSONObject item = new JSONObject();
        result.add(item);
        return result.toJSONString();
    }

    @GET
    @Path("ireport")
    public String getReport(@QueryParam("id")String reportID){

        if(StringUtils.isEmptyOrSpace(reportID)){
            return "id is a must";
        }

        List<ResultDto> resultDtos = IResultDaoImpl.getResult(Integer.valueOf(reportID));

        JSONObject reportJsonObject = new JSONObject();
        JSONArray reports = new JSONArray();
        if(null != resultDtos && 0 < resultDtos.size()){
            for(ResultDto dto : resultDtos){
                dto.setCase_info(JSONObject.parse(dto.getCase_info().toString()));
                reports.add(dto);
            }

            reportJsonObject.put("result", reports);
            return reportJsonObject.toJSONString();
        }

        return "Not Found report";
    }

    @POST
    @Path("ieditxml")
    public boolean editXml(@FormParam("path") String path, @FormParam("content") String content){

        if(StringUtils.isEmptyOrSpace(path)){
            return false;
        }

        if(StringUtils.isEmptyOrSpace(content)){
            return false;
        }

        return Driver.editXmlFile(path, content);
    }

    @GET
    @Path("iflag")
    public String runFlagCases(@QueryParam("path")String path,
                               @QueryParam("flag")String flags,
                               @QueryParam("user") String user){
        List<NodeManagerDto> runDtos = new ArrayList<NodeManagerDto>();

        try {
            if(StringUtils.isEmptyOrSpace(path)){
                return "path is a must!";
            }

            if(StringUtils.isEmptyOrSpace(flags)){
                return "flags is a must!";
            }

            String[] flagsArray = flags.split(",");

            //从数据库里找到指定路径path下的全部用例的path和flags
            List<NodeManagerDto> dtos = iNodeManagerDao.findNodeFlags(path);

            if(null != dtos && 0 < dtos.size()) {
                for (NodeManagerDto dto : dtos) {
                    for(String flag : flagsArray) {
                        if (dto.getFlags().contains((flag))) {
                            runDtos.add(dto);
                        }
                    }
                }
            }
        } catch (Exception ex){
            log.error("get flag of cases from DB BUT encountering Exception: " + ex);
            return ex.getMessage();
        }

        List<File> files = new ArrayList<File>();
        JSONArray pn = new JSONArray();
        try {
            for (NodeManagerDto dto: runDtos) {
                File directory = null;
                String strPath = dto.getPath();
                if((null == sysPro.getProperty("filelocation")) || StringUtils.isEmptyOrSpace(sysPro.getProperty("filelocation"))){
                    directory = new File(strPath);

                } else {
                    if(!strPath.startsWith(sysPro.getProperty("filelocation") )) {
                        directory = new File(sysPro.getProperty("filelocation") + strPath);
                    } else{
                        directory = new File(strPath);
                    }
                }

                if(null == directory || StringUtils.isEmptyOrSpace(directory.getAbsolutePath())){
                    return "path is not exist!";
                }
                strPath = directory.getAbsolutePath();
                files.add(new File(strPath));
                if(!StringUtils.isEmptyOrSpace(dto.getPn())){
                    if(!pn.contains(dto.getPn().toLowerCase())){
                        pn.add(dto.getPn().toLowerCase());
                    }
                }

            }
        }catch (Exception ex){
            log.error("read files ecountering Exception: " + ex);
            return ex.getMessage();
        }

        CommandEntity commandEntity = new CommandEntity();
        if(!StringUtils.isEmptyOrSpace(user)){
            commandEntity.setUser(user.toLowerCase());
        }

        if(0 < pn.size()){
            commandEntity.setProductName(pn.toString());
        } else{
            pn.add("ignore");
        }

        try{
            return driver.Execute(files, verifier, dataManager, commandEntity);
        } catch (Exception ex){
            log.error(ex);
            if(null != ex.getMessage() && StringUtils.isNotEmpty(ex.getMessage())){
                return ex.getMessage();
            }
            return ex.toString();
        }
    }

    @GET
    @Path("iset")
    public String generateFlags(@QueryParam("path")String path, @QueryParam("pn")String pn){
        if(StringUtils.isEmptyOrSpace(path)){
            return "no generate since Path is null.";
        }

        if(StringUtils.isEmptyOrSpace(pn)){
            return "no generate since pn is null.";
        }

        List<File> files = new ArrayList<File>();
        List<FlagsDto> allFlags = null;

        try{
            Driver.getFileList(path, files);

            if(null == files || 0 == files.size()){
                return "no generate since files is null.";
            }

            allFlags = iFlagsDao.findAll();
            if(null == allFlags || 0 == allFlags.size()){
                return "no generate since Flags table is null in DB.";
            }

        } catch (Exception ex){
            log.info(ex);
            return ex.getMessage();
        }

        HashMap<String, String> flagMap = new HashMap<>();
        for(FlagsDto dto : allFlags){
            flagMap.put(dto.getName(), String.valueOf(dto.getId()));
        }

        int update = 0;
        int insert = 0;
        int over = 0;

        JSONObject resultJsonObject = new JSONObject();

        for(File file : files) {
            try {
                TestCase testCase = xmlTcm.GetTestCase(file.getPath());
                TestCaseNode node = (TestCaseNode) testCase;
                if (StringUtils.isEmptyOrSpace(node.getFlags())) {
                    continue;
                } else {
                    String[] flags = node.getFlags().split(",");
                    if (null == flags || 0 == flags.length) {
                        continue;
                    }

                    String ids = translateFlagToIDs(flagMap, flags);

                    String subPath = initCasePath(file.getPath());
                    List<NodeManagerDto> dtos = iNodeManagerDao.findNodeFlags(subPath);
                    if (null == dtos || 0 == dtos.size()) {
                        NodeManagerDto dto = new NodeManagerDto();
                        dto.setName(node.getName());
                        dto.setFlags(ids);
                        dto.setPath(subPath);
                        dto.setName(node.getName());
                        dto.setNote(node.getStatus());
                        dto.setPn(pn.toLowerCase().trim());
                        iNodeManagerDao.addNodeFlag(dto);
                        insert++;
                        log.warn(dto.getPath() + " has been insert flags:" + flags.toString());
                    } else {
                        if (1 == dtos.size()) {
                            if (isUpdateNodeFlag(dtos.get(0), pn, ids)) {
                                NodeManagerDto dto = new NodeManagerDto();
                                dto.setId(dtos.get(0).getId());
                                dto.setName(node.getName());
                                dto.setFlags(ids);
                                dto.setPath(initCasePath(file.getPath()));
                                dto.setName(node.getName());
                                dto.setNote(node.getStatus());
                                dto.setPn(pn.toLowerCase().trim());
                                iNodeManagerDao.updateNodeFlag(dto);
                                update++;
                                log.warn(subPath + " has been update flags");
                            }
                        } else {
                            over++;
                            log.warn("Over 1 same path test cases against: " + subPath + " !!!");
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("file encountering against the path: " + file.getPath());
                log.error(ex);
            }
        }

        resultJsonObject.put("INSERT", insert);
        resultJsonObject.put("UPDATE", update);
        resultJsonObject.put("OVER", over);

        return resultJsonObject.toJSONString();
    }

    @GET
    @Path("icount")
    public String countFiles(@QueryParam("path")String path,
                             @QueryParam("flag")String flags){

        if(StringUtils.isEmptyOrSpace(path)){
            return "path is a must!";
        }

        if(StringUtils.isEmptyOrSpace(flags)){
            return "flags is a must!";
        }

        Map<String, Integer> count = new ArrayHashMap<>();

        try{
            List<NodeManagerDto> dtos = iNodeManagerDao.findNodeFlags(path);

            String[] flagsArray = flags.split(",");

            List<NodeManagerDto> matchedDtos = new ArrayList<NodeManagerDto>();

            if(null != dtos && 0 < dtos.size()) {
                for (NodeManagerDto dto : dtos) {
                    for(String flag : flagsArray) {
                        if (dto.getFlags().contains((flag))) {
                            matchedDtos.add(dto);
                        }
                    }
                }
            }

            int emptyPn = 0;
            for(NodeManagerDto dto : matchedDtos){
                if(!StringUtils.isEmptyOrSpace(dto.getPn())){
                    if(null != count.get(dto.getPn())){
                        count.put(dto.getPn(), count.get(dto.getPn()) + 1);
                    } else{
                        count.put(dto.getPn(), 0);
                    }
                } else{
                    count.put("emptyPn", emptyPn++);
                }
            }

        } catch (Exception ex){
            log.error(ex);
            return ex.getMessage();
        }

        return JSON.toJSONString(count);
    }

    @GET
    @Path("imap")
    public String flagMap(){

        List<FlagsDto> allFlags = null;
        JSONObject mapJson = new JSONObject();

        try {
            allFlags = iFlagsDao.findAll();
            for(FlagsDto dto : allFlags){
                mapJson.put(dto.getName().toLowerCase().trim(), dto.getId());
            }
        } catch (Exception ex){
            log.error(ex);
            return ex.getMessage();
        }

        return mapJson.toJSONString();
    }

    @GET
    @Path("istatis")
    public String statistics(){
        List<VersionDto> versionDtoList;
        List<NodeManagerDto> nodeManagerDtoList;

        try{
            versionDtoList = IVersionDaoImpl.findRunnedNumbers();
            nodeManagerDtoList = iNodeManagerDao.findBizCases();
        } catch (Exception ex){
            JSONObject item = new JSONObject();
            item.put("result", "SQL Exception");
            return item.toJSONString();
        }


        Map<String, Integer> versionMap = new HashMap<>();
        JSONObject versionObj = new JSONObject();

        if((null != versionDtoList) && 0 < versionDtoList.size()){
            for(VersionDto dto : versionDtoList){
                if(!versionMap.containsKey(dto.getPn())){
                    versionMap.put(dto.getPn(), 1);
                } else{
                    versionMap.put(dto.getPn(),versionMap.get(dto.getPn()) + 1);
                }
            }

            System.out.println(JSON.toJSONString(versionMap));
        }

        Map<String, Integer> nodeMap = new HashMap<>();
        if((null != versionDtoList) && 0 < versionDtoList.size()){
            for(NodeManagerDto dto : nodeManagerDtoList){
                if(!nodeMap.containsKey(dto.getPn())){
                    nodeMap.put(dto.getPn(), 1);
                } else{
                    nodeMap.put(dto.getPn(),nodeMap.get(dto.getPn()) + 1);
                }
            }

            System.out.println(JSON.toJSONString(nodeMap));
        }

        JSONObject item = new JSONObject();
        item.put("nodeMap", nodeMap);
        item.put("versionMap", versionMap);
        return item.toJSONString();
    }

    private boolean isUpdateNodeFlag(NodeManagerDto dto, String pn, String ids){
        String[] dbFlags = dto.getFlags().split(",");
        if(!compareStringArray(dbFlags, ids.split(","))){
            return true;
        }

        if(StringUtils.isNotEmpty(pn) && null == dto.getPn()){
            return true;
        }

        if (null != dto.getPn()
                && StringUtils.isNotEmpty(pn)
                && !dto.getPn().toLowerCase().equals(pn.toLowerCase().trim())){
            return true;
        }

        return false;
    }

    private String translateFlagToIDs(HashMap<String, String> flagMap, String[] flags){
        StringBuilder result = new StringBuilder();
        for (String flag : flags){
            if(null != flagMap.get(flag.toLowerCase().trim())){
                result.append(flagMap.get(flag.toLowerCase().trim())).append(",");
            }
        }

        if(result.toString().endsWith(",")){
            return result.substring(0, result.toString().length() - 1);
        }

        return result.toString();
    }

    private boolean compareStringArray(String[] array1, String[] array2){
        if(null == array1 || 0 == array1.length || null == array2 || 0 == array2.length){
            return true;
        }

        if(array1.length != array2.length){
            return false;
        }

        List<String> stringB = Arrays.asList(array2);
        for (String s1 : array1){
            if(!stringB.contains(s1)){
                return false;
            }
        }

        return true;
    }

    private String initCasePath(String path){
        if(path.startsWith("testCases/")){
            return path;
        }

        path = path.substring(path.indexOf("testCases/"), path.length());

        return path;
    }
}
