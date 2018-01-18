package com.chanjet.chanapp.qa.iFramework.common.DAO.Base;

import com.chanjet.chanapp.qa.iFramework.common.DTO.SummaryDto;

import java.util.List;

/**
 * Created by haijia on 9/22/17.
 */
public interface ISummaryDao {
    void addSummary(SummaryDto summaryDto);
    List<SummaryDto> findSummary(String productname, String rundate);
    List<SummaryDto> findLatestSummary(String productname);
    List<SummaryDto> findTotalSummaryByDate(String rundate);
}
