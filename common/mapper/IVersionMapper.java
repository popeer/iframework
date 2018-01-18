package com.chanjet.chanapp.qa.iFramework.common.mapper;

import com.chanjet.chanapp.qa.iFramework.common.DTO.VersionDto;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IVersionMapper {
    VersionDto findVersion(long id);
    long generateVersionId();
    void addVersion(long id);
    int addVersionPn(VersionDto versionDto);
    List<VersionDto> findVersionByPn(@Param("pn")String pn, @Param("date") String date);
}
