package com.myssm.common;

import java.util.List;

public interface TestService {

    List<User> findAll();

    User findByNo(String no);
}
