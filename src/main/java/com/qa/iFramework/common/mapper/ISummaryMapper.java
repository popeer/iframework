package com.qa.iFramework.common.mapper;

import org.apache.ibatis.annotations.Param;
import com.qa.iFramework.common.DTO.SummaryDto;

import java.util.List;

/**
 * Created by haijia on 9/22/17.
 */
public interface ISummaryMapper {
    void addSummary(SummaryDto summaryDto);
    List<SummaryDto> findSummary(@Param("productname") String productname, @Param("rundate") String rundate);
    List<SummaryDto> findLatestSummary(@Param("productname") String productName);
    List<SummaryDto> findTotalSummaryByDate(@Param("rundate") String rundate);
}
