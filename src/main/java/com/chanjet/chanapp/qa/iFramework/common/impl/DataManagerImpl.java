package com.chanjet.chanapp.qa.iFramework.common.impl;

import com.chanjet.chanapp.qa.iFramework.Entity.Result;
import com.chanjet.chanapp.qa.iFramework.Entity.TestCase;
import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.IResultDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;
import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by haijia on 12/10/16.
 */
public class DataManagerImpl implements IDataManager{
    protected static Logger log = LogManager.getLogger(DataManagerImpl.class);

    @Resource
    IResultDao iResultDao;

//    @Resource
//    IVersionDaoImpl iVersionDao;

    public DataManagerImpl(){
    }

    /**
     * @Auther: haijia
     * @Description:
     * @param result
     * @param versionID 报告ID，一个报告id对应多份用例结果
     * @Date: 12/11/17 13:45
     */
    @Override
    public ResultDto storeExecuteResult(Result result, CommandEntity commandEntity){

        //4. insert into DB.
        ResultDto data = new ResultDto();
        try{
            data.setIs_passed(result.getResult());
            data.setUser_id(StringUtils.isEmpty(result.getOperator()) ? "noUser" : result.getOperator());
            data.setModule_id(StringUtils.isEmpty(result.getInterfaceName()) ? "noModuleId" : result.getInterfaceName());
            data.setNode_name(StringUtils.isEmpty(result.getNode_name()) ? "noNodeName" : result.getNode_name());
            data.setStep_name(StringUtils.isEmpty(result.getStep_name()) ? "noStepName" : result.getStep_name());
            data.setRundatetime(StringUtils.isEmpty(result.getDateTime()) ? DateUtil.formatDate(new Date()) : result.getDateTime());
            data.setEnvironment(StringUtils.isEmpty(result.getEnvironment()) ? "noEnvironment" : result.getEnvironment());
            data.setVersion_id(null == commandEntity.getVersionID()? 0 : commandEntity.getVersionID());
            data.setCase_info(result.getError().toString());
            data.setModule_desc(result.getModule_desc());
            iResultDao.addResult(data);
            //因为必须返回json格式，而caseinfo是strin，所以重新赋值为ErrorInfo对象
            data.setCase_info(result.getError());
        } catch (Exception ex){
            log.error("############");
            log.error(ex.getMessage());
            return data;
        }

        return data;
    }

    public boolean storeSummary(){



        return false;
    }

    public List<String> queryCasesName(){
        return null;
    }

    public List<TestCase> queryCases(String path){
        return null;
    }

    public int insertCasesToDB(List<TestCase> cases){
        return 1;
    }
}
