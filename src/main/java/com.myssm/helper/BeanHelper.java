package com.myssm.helper;

import com.myssm.common.UserMapper;
import com.myssm.mybatis.MySqlSession;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实例化对象
 */
public class BeanHelper {
    private static MySqlSession sqlSession = new MySqlSession();

    private static final Map<Class,Object> BEAN_MAP = new HashMap<>();

    public static Map<Class,Object> getBeanMap(){
        return BEAN_MAP;
    }

    public static void doInstance(){
        Set<Class<?>> annoSet = ClassHelper.getAnnoSet();
        if (CollectionUtils.isEmpty(annoSet)){
            return;
        }
        Class cl = UserMapper.class;
        //实例化MapperProxy
        BEAN_MAP.put(cl,sqlSession.getMapper(cl));

        for (Class clazz : annoSet){
            try {
                Object instance = clazz.newInstance();
                BEAN_MAP.put(clazz,instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
