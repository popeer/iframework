package com.chanjet.chanapp.qa.iFramework.common.mapper;

import com.chanjet.chanapp.qa.iFramework.common.DTO.FlagsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by haijia on 8/7/18.
 */
public interface IFlagsMapper {
    List<FlagsDto> findFlagID(@Param("name") String name);
    List<FlagsDto> findAll();
}
