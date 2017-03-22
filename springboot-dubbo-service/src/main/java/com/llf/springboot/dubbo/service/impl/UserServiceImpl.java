package com.llf.springboot.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.llf.springboot.dubbo.mapper.UserMapper;
import com.llf.springboot.dubbo.model.User;
import com.llf.springboot.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by PhychoLee on 2017/2/23.
 */
@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User sayHi(String name) {
        return userMapper.getUserByName(name);
    }
}
