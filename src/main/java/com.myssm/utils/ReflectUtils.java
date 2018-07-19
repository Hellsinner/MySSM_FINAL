package com.myssm.utils;

import java.lang.reflect.Field;

/**
 * 反射相关类
 */
public class ReflectUtils {

    public static void setField(Object instance, Field field,Object beanFiledInstance){
        try {
            field.setAccessible(true);
            field.set(instance,beanFiledInstance);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setField(Object instance,Field field,String fileType,Object inject){

    }
}
