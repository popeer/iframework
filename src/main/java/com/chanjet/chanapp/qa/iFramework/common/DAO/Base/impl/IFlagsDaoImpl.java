package com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl;

import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.IFlagsDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.FlagsDto;
import com.chanjet.chanapp.qa.iFramework.common.mapper.IFlagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haijia on 8/7/18.
 */
@Repository
public class IFlagsDaoImpl implements IFlagsDao, Serializable {
    @Autowired
    private IFlagsMapper flagsMapper;

    @Override
    public List<FlagsDto> findFlagID(String name) {
        if (name !=null && !name.equals("")) {
            return this.flagsMapper.findFlagID(name);
        }
        return null;
    }

    @Override
    public List<FlagsDto> findAll(){
        return this.flagsMapper.findAll();
    }
}
