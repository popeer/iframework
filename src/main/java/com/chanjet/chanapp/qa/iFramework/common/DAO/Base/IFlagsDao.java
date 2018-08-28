package com.chanjet.chanapp.qa.iFramework.common.DAO.Base;

import com.chanjet.chanapp.qa.iFramework.common.DTO.FlagsDto;

import java.util.List;

/**
 * Created by haijia on 8/7/18.
 */
public interface IFlagsDao {
    List<FlagsDto> findFlagID(String name);
    List<FlagsDto> findAll();
}
