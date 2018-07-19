package com.myssm.common;

import com.myssm.annocation.MyAutowried;
import com.myssm.annocation.MyService;

import java.util.List;
@MyService
public class TestServiceImpl implements TestService{

    @MyAutowried
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findByNo(String no) {
        return userMapper.findByNo(no);
    }
}
