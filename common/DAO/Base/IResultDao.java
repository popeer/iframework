package com.chanjet.chanapp.qa.iFramework.common.DAO.Base;

import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IResultDao {

//    List<ResultDto> findResults(ResultDto result);

//    void updateResult(ResultDto result);

    void addResult(ResultDto result);

//    void addResult(long userID,
//               long moduleID,
//               long versionID,
//               boolean isPassed,
//               String caseInfo);

//    void deleteResult(long id);

    List<ResultDto> getResult(Integer version_id);

}
