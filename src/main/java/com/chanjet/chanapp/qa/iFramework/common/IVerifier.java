package com.chanjet.chanapp.qa.iFramework.common;

import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.ResponseParameter;
import com.chanjet.chanapp.qa.iFramework.common.xml.Entity.Step;
import com.chanjet.chanapp.qa.iFramework.Entity.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by haijia on 11/21/16.
 */
public interface IVerifier {
    /**
     * @Auther: haijia
     * @Description:
     * @param result 该步骤的对象
     * @param responseParams 期望值组
     * @param entry excel数据源的期望值
     * @param preExecutedResults 该步之前步骤的结果集合
     * @Date: 1/4/18 16:04
     */
    Result VerifyResult(Step result, List<ResponseParameter> responseParams, Map<String, String> entry, List<Object> preExecutedResults, CommandEntity commandEntity) throws Exception;
}
