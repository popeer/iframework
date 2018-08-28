package com.chanjet.chanapp.qa.iFramework.common.processor.generateFlags;

import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl.IFlagsDaoImpl;
import com.chanjet.chanapp.qa.iFramework.common.DTO.FlagsDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by haijia on 8/7/18.
 */
public class FlagsUtil {
    @Autowired
    IFlagsDaoImpl iFlagsDao;

    public List<FlagsDto> getFlagID(String flag){
        List<FlagsDto> flagsDtos = iFlagsDao.findFlagID(flag);
        return flagsDtos;
    }
}
