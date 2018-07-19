package com.myssm.common;

import java.util.List;

public interface UserMapper {
    User findUser(User user);

    User findByNo(String no);

    List<User> findByType(int type);

    List<User> findAll();
}
