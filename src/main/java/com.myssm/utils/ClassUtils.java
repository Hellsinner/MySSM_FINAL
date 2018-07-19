package com.myssm.utils;

import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassUtils {

    private static final List<String> CLASS_NAME_LIST = new ArrayList<>(50);

    private static final Set<Class<?>> CLASS_SET = new HashSet<>();

    public static List<String> getClassNameList(){
        return CLASS_NAME_LIST;
    }

    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取类的全路径名
     */
    public static void doScanner(String base_package){

        URL url = getClassLoader().getResource("/"+base_package.replaceAll("\\.","/"));

        File dir = new File(url.getFile());

        for (File file : dir.listFiles()){
            if (file.isDirectory()){
                doScanner(base_package+"."+file.getName());
            }else {
                String className = base_package + "." + file.getName().replace(".class","");
                CLASS_NAME_LIST.add(className);
            }
        }
    }

    /**
     * //获取类的Class对象
     */
    public static void doClass() {
        if (CollectionUtils.isEmpty(CLASS_NAME_LIST)){
            return;
        }
        for (String className : CLASS_NAME_LIST){
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
                CLASS_SET.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
