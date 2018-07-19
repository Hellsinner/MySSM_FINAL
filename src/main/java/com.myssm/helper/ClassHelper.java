package com.myssm.helper;

import com.myssm.annocation.MyController;
import com.myssm.annocation.MyService;
import com.myssm.utils.ClassUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 获取带注解的类
 */
public class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        CLASS_SET = ClassUtils.getClassSet();
    }

    public static Set<Class<?>> getAnnoSet(){
        if (CollectionUtils.isEmpty(CLASS_SET)){
            return null;
        }
        Set<Class<?>> annoSet = new HashSet<>();
        for (Class clazz : CLASS_SET){
            if (clazz.isAnnotationPresent(MyController.class)||
                    clazz.isAnnotationPresent(MyService.class)){
                annoSet.add(clazz);
            }
        }
        return annoSet;
    }
}
