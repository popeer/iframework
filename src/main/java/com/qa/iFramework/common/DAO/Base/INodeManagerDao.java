package com.qa.iFramework.common.DAO.Base;

import com.qa.iFramework.common.DTO.NodeManagerDto;

import java.util.List;

/**
 * Created by haijia on 8/8/18.
 */
public interface INodeManagerDao {
    List<NodeManagerDto> findNodeFlags(String path);
    void updateNodeFlag(NodeManagerDto nodeManagerDto);
    void addNodeFlag(NodeManagerDto nodeManagerDto);
    void deleteNodeFlag(long id);
    List<NodeManagerDto> findBizCases();
}
