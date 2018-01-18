package com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl;

import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.ISummaryDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.SummaryDto;
import com.chanjet.chanapp.qa.iFramework.common.mapper.ISummaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by haijia on 9/22/17.
 */
@Repository
public class ISummaryDaoImpl implements ISummaryDao {
    @Autowired
    private ISummaryMapper sm;

    @Override
    public void addSummary(SummaryDto summaryDto){
        this.sm.addSummary(summaryDto);
    }

    @Override
    public List<SummaryDto> findSummary(String productname, String rundate){

        return this.sm.findSummary(productname, rundate);
    }

    @Override
    public List<SummaryDto> findLatestSummary(String productname){
        return this.sm.findLatestSummary(productname);
    }

    @Override
    public List<SummaryDto> findTotalSummaryByDate(String rundate){
        return this.sm.findTotalSummaryByDate(rundate);
    }
}
