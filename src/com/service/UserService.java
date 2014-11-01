package com.service;

import org.springframework.stereotype.Service;

import com.mapper.dao.UserMapper;
import com.model.User;

@Service
public class UserService {
    UserMapper userMapper;

    public UserMapper getUserMapper() {
        return userMapper;
    }

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public int insert(User user) {
        return userMapper.insertSelective(user);
    }

}
