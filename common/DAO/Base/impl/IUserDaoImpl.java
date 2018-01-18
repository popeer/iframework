package com.chanjet.chanapp.qa.iFramework.common.DAO.Base.impl;

import com.chanjet.chanapp.qa.iFramework.common.DAO.Base.IUserDao;
import com.chanjet.chanapp.qa.iFramework.common.DTO.UserDto;
import com.chanjet.chanapp.qa.iFramework.common.mapper.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
//@Service("userService")
@Repository
public class IUserDaoImpl implements IUserDao {
    @Autowired
    private IUserMapper um;
    /**
     *
     * @param name
     * @return List<UserDto>
     * 按照姓名查找user，不合法返回null
     */
    @Override
    public List<UserDto> findUserByName(String name) {
        if (name !=null && !name.equals("")) {
            return this.um.findUser(name);
        }
        return null;
    }
    /**
     *
     * @param user
     * 更新user信息，email，name不能为空
     */
    @Override
    public void updateUserById(UserDto user) {
        if(user !=null )
        {
            this.um.updateUser(user);
        }
    }

    /**
     *
     * @param user
     * 新增user信息，email，name不能为空
     */
    @Override
    public void addUser(UserDto user) {
        if(user !=null && !user.getEmail().equals("") && !user.getName().equals(""))
        {
            this.um.addUser(user);
        }
    }

    /**
     *
     * @param id
     * 删除用户信息
     */
    @Override
    public void deleteUser(long id) {
        if(id !=0)
        {
            this.um.deleteUser(id);
        }
    }
}
