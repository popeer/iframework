package com.qa.iFramework.common.DAO.Base.impl;

import com.qa.iFramework.common.DAO.Base.IVersionDao;
import com.qa.iFramework.common.DTO.VersionDto;
import com.qa.iFramework.common.mapper.IVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
//@Service("versionService")
@Repository
public class IVersionDaoImpl implements IVersionDao {
    @Autowired
    private IVersionMapper vm;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public VersionDto findVersion(long id) {
        if(id!=0)
        {
            return this.vm.findVersion(id);
        }
        return null;
    }

    @Override
    public long generateVersion()
    {
        long id=this.vm.generateVersionId();
        this.vm.addVersion(id);
        return id;
    }

    @Override
    public List<VersionDto> findVersionByPn(String pn, String date){
        return this.vm.findVersionByPn(pn, date);
    }

    @Override
    public int addVersionPn(VersionDto versionDto){
        return this.vm.addVersionPn(versionDto);
    }

    @Override
    public void addVersion(long id){
        this.vm.addVersion(id);
    }

    @Override
    public List<VersionDto> findRunnedNumbers(){
        return this.vm.findRunnedNumbers();
    }
}
