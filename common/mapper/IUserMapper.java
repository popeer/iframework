package com.chanjet.chanapp.qa.iFramework.common.mapper;

import com.chanjet.chanapp.qa.iFramework.common.DTO.UserDto;

import java.util.List;

/**
 * Created by bailin on 2016/11/30.
 */
public interface IUserMapper {
    List<UserDto> findUser(String name);
    void updateUser(UserDto user);
    void addUser(UserDto user);
    void deleteUser(long id);
}
