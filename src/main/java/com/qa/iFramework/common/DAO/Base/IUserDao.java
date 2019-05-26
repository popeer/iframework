package com.qa.iFramework.common.DAO.Base;

import com.qa.iFramework.common.DTO.UserDto;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IUserDao {
    List<UserDto> findUserByName(String name);
    void updateUserById(UserDto user);
    void addUser(UserDto user);
    void deleteUser(long id);
}
