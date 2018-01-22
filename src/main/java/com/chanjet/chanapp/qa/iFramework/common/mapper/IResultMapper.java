package com.chanjet.chanapp.qa.iFramework.common.mapper;

import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 * 
 */
public interface IResultMapper {
//    List<ResultDto> findResults(ResultDto result);

//    void updateResult(ResultDto result);

    void addResult(ResultDto result);

//    void addResult(@Param("user_id")long userID,
//                   @Param("module_id")long moduleID,
//                   @Param("version_id")long versionID,
//                   @Param("is_passed")boolean isPassed,
//                   @Param("case_info")String caseInfo);

//    void deleteResult(long id);

    List<ResultDto> getResult(Integer version_id);
}
