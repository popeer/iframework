package com.qa.iFramework.common.DAO.Base.impl;

import com.qa.iFramework.common.DAO.Base.IModuleDao;
import com.qa.iFramework.common.DTO.ModuleDto;
import com.qa.iFramework.common.mapper.IModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
//@Service("moduleService")
@Repository
public class IModuleDaoImpl implements IModuleDao {
    @Autowired
    private IModuleMapper mm;

    @Override
    public List<ModuleDto> findModule(String name) {
        if (name !=null && !name.equals("")) {
            return this.mm.findModule(name);
        }
        return null;
    }

    @Override
    public void updateModule(ModuleDto module) {
        if(module !=null )
        {
            this.mm.updateModule(module);
        }
    }

    @Override
    public void addModule(ModuleDto module) {
        if(module !=null &&  !module.getName().equals(""))
        {
            this.mm.addModule(module);
        }
    }

    @Override
    public void deleteModule(long id) {
        if(id !=0)
        {
            this.mm.deleteModule(id);
        }
    }
}
