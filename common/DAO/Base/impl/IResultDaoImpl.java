package com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl;

import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.IResultDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.ResultDto;
import com.chanjet.chanapp.qa.iFramework.common.mapper.IResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
//@Service("resultService")
@Repository
public class IResultDaoImpl implements IResultDao, Serializable {
    @Autowired
    private IResultMapper rm;

//    @Override
//    public List<ResultDto> findResults(ResultDto result) {
//        if(result!=null)
//        {
//            return this.rm.findResults(result);
//        }
//        return null;
//    }
//
//    @Override
//    public void updateResult(ResultDto result) {
//        if(result!=null)
//        {
//            this.rm.updateResult(result);
//        }
//    }

    @Override
    public void addResult(ResultDto result) {
//        if(result!=null)
//        {
//            this.rm.addResult(result.getUser_id(), result.getModule_id(), result.getVersion_id(), result.is_passed(),
//                    result.getCase_info());
//        }

//        this.rm.addResult(result.getUser_id(), result.getModule_id(),result.getVersion_id(),result.is_passed(),
//                result.getCase_info());
        this.rm.addResult(result);
    }

//    @Override
//    public void deleteResult(long id) {
//        if(id!=0)
//        {
//            this.rm.deleteResult(id);
//        }
//    }

    @Override
    public List<ResultDto> getResult(Integer version_id){
        return this.rm.getResult(version_id);
    }
}
