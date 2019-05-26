package com.qa.iFramework.common.mapper;

import com.qa.iFramework.common.DTO.NodeManagerDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by haijia on 8/8/18.
 */
public interface INodeManagerMapper {
    List<NodeManagerDto> findNodeFlags(@Param("path")String path);
    void updateNodeFlag(NodeManagerDto nodeManagerDto);
    void addNodeFlag(NodeManagerDto nodeManagerDto);
    void deleteNodeFlag(Integer id);
    List<NodeManagerDto> findBizCases();
}
