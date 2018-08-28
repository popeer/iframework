package com.chanjet.chanapp.qa.iFramework.common.DAO.Base;

import com.chanjet.chanapp.qa.iFramework.common.DTO.NodeManagerDto;

import java.util.List;

/**
 * Created by haijia on 8/8/18.
 */
public interface INodeManagerDao {
    List<NodeManagerDto> findNodeFlags(String path);
    void updateNodeFlag(NodeManagerDto nodeManagerDto);
    void addNodeFlag(NodeManagerDto nodeManagerDto);
    void deleteNodeFlag(long id);
}
