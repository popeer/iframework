package com.chanjet.chanapp.qa.iFramework.common.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.chanapp.qa.iFramework.common.Util.DateUtil;
import com.chanjet.chanapp.qa.iFramework.common.Util.StringUtils;
import com.chanjet.chanapp.qa.iFramework.Entity.RunStatus;
import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.IResultDao;
import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.ISummaryDao;
import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.IVersionDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;
import com.chanjet.chanapp.qa.iFramework.common.DTO.SummaryDto;
import com.chanjet.chanapp.qa.iFramework.common.DTO.VersionDto;
import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.IRemoteStarter;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

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
            @QueryParam("path") String path) {

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
            commandEntity.setProductName(productName.toLowerCase());
        }

        if(!StringUtils.isEmptyOrSpace(dns)){
            commandEntity.setDns(dns.toLowerCase());
        }

        try{
            return driver.Execute(path, verifier, dataManager, commandEntity);
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
        Driver.getFileList(productname,result);

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

    /**
     * @Auther: haijia
     * @Description: 创建或编辑测试用例文件
     * @param path
     * @param content
     * @Date: 12/4/17 16:20
     */
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

}
