package com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl;

import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.INodeManagerDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.NodeManagerDto;
import com.chanjet.chanapp.qa.iFramework.common.mapper.INodeManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by haijia on 8/8/18.
 */
@Repository
public class INodeManagerDaoImpl implements INodeManagerDao {
    @Autowired
    INodeManagerMapper iNodeManagerMapper;

    @Override
    public List<NodeManagerDto> findNodeFlags(String path){
        if (path !=null && !path.equals("")) {
            return this.iNodeManagerMapper.findNodeFlags(path);
        }
        return null;
    }

    @Override
    public void updateNodeFlag(NodeManagerDto nodeManagerDto){
        if(nodeManagerDto !=null )
        {
            this.iNodeManagerMapper.updateNodeFlag(nodeManagerDto);
        }
    }

    @Override
    public void addNodeFlag(NodeManagerDto nodeManagerDto){
        if(nodeManagerDto !=null &&  !nodeManagerDto.getName().equals(""))
        {
            this.iNodeManagerMapper.addNodeFlag(nodeManagerDto);
        }
    }

    @Override
    public void deleteNodeFlag(long id){

    }
}
