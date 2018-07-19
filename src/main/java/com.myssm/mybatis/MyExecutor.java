package com.myssm.mybatis;

import com.myssm.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyExecutor {

    static final List<String> BASETYPE = new ArrayList<>();
    static {
        BASETYPE.add("java.lang.String");
        BASETYPE.add("java.lang.Integer");
        BASETYPE.add("java.lang.Long");
    }

    public <T> List<T> query(MapperInfo mapperInfo,Object param) throws SQLException {
        String sql = mapperInfo.getSql();
        String paramType = mapperInfo.getParamType();
        List<String> paramMapping = mapperInfo.getParamMapping();
        String reurnType = mapperInfo.getReurnType();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
             connection = DataSourceFactory.getConnection();
             preparedStatement = connection.prepareStatement(sql);
            //String类型
            if (BASETYPE.contains(paramType)){
                preparedStatement.setObject(1,param);
            }else {
                for (int i=0;i<paramMapping.size();i++){
                    String s = paramMapping.get(i);
                    Object fieldValue = getFieldValue(param, s);
                    preparedStatement.setObject(i+1,fieldValue);
                }
            }
            resultSet = preparedStatement.executeQuery();
            Class<?> clazz = Class.forName(reurnType);
            Object instance = null;
            List<T> list = new ArrayList<>();
            while (resultSet.next()){
                 instance = clazz.newInstance();
                 Field[] declaredFields = clazz.getDeclaredFields();
                 for (Field field : declaredFields){
                     ReflectUtils.setField(instance,field,resultSet.getObject(field.getName()));
                 }
                list.add((T) instance);
             }
             return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }finally {
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }
    return null;
    }

    private Object getFieldValue(Object param, String s){
        Class<?> aClass = param.getClass();
        try {
            Field field = aClass.getDeclaredField(s);
            field.setAccessible(true);
            return field.get(param);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
