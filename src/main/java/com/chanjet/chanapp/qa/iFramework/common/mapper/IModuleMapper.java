package com.chanjet.chanapp.qa.iFramework.common.mapper;

import com.chanjet.chanapp.qa.iFramework.common.DTO.ModuleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IModuleMapper {
    List<ModuleDto> findModule(@Param("name")String name);
    void  updateModule(ModuleDto module);
    void addModule(ModuleDto module);
    void deleteModule(long id);
}
