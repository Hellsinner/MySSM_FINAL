package com.myssm.mybatis;

import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;

public class MySqlSession {
    private MyExecutor executor = new MyExecutor();

    public <T> T selectOne(MapperInfo mapperInfo,Object param) throws SQLException {
        List<T> list = selectList(mapperInfo, param);
        if (!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    public <T> List<T> selectList(MapperInfo mapperInfo,Object param) throws SQLException {
        return executor.query(mapperInfo,param);
    }

    public <T> T getMapper(Class clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},
                new MapperProxy(this));
    }
}
