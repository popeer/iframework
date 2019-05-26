package com.qa.iFramework.common.DAO.Base;

import com.qa.iFramework.common.DTO.ModuleDto;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IModuleDao {
    List<ModuleDto> findModule(String name);
    public  void updateModule(ModuleDto module);
    public  void addModule(ModuleDto module);
    public void deleteModule(long id);
}
