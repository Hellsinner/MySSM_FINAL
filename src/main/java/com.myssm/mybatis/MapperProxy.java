package com.myssm.mybatis;

import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class MapperProxy implements InvocationHandler {

    private MySqlSession sqlSession;

    public MapperProxy(MySqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<MapperInfo> mapperInfoList = ScanMapperXml.buildMapper("UserMapper.xml");
        if (!CollectionUtils.isEmpty(mapperInfoList)){
            for (MapperInfo mapperInfo : mapperInfoList){
                if (method.getDeclaringClass().getName().equals(mapperInfo.getNameSpace()) && method.getName().equals(mapperInfo.getId())){
                    if (args==null){
                        switch (mapperInfo.getType()){
                            case "select" :
                                        Class<?> returnType = method.getReturnType();
                                        if (Collection.class.isAssignableFrom(returnType)){
                                            return sqlSession.selectList(mapperInfo,null);
                                        }else {
                                            return sqlSession.selectOne(mapperInfo,null);
                                        }
                            case "update" :
                                        break;
                            case "delete" :
                                        break;
                        }
                    }else if (args.length==1){
                        switch (mapperInfo.getType()){
                            case "select" :
                                Class<?> returnType = method.getReturnType();
                                if (Collection.class.isAssignableFrom(returnType)){
                                    return sqlSession.selectList(mapperInfo,args[0]);
                                }else {
                                    return sqlSession.selectOne(mapperInfo,args[0]);
                                }
                            case "update" :
                                break;
                            case "delete" :
                                break;
                        }
                    }else {
                        throw new RuntimeException();
                    }
                }
            }
        }
        return null;
    }
}
