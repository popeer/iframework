package com.chanjet.chanapp.qa.iFramework.common.DAO.Base;

import com.chanjet.chanapp.qa.iFramework.common.DTO.VersionDto;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IVersionDao {
    VersionDto findVersion(long id);
    void addVersion(long id);
    int addVersionPn(VersionDto versionDto);
    List<VersionDto> findVersionByPn(String pn, String date);
    long generateVersion();
}
