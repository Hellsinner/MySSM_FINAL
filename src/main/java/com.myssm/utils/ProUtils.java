package com.myssm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具类
 */
public class ProUtils {
    private static Properties properties = new Properties();

    private static final Map<String,String> PROP_MAP = new HashMap<>();

    public ProUtils(String proName) throws NoSuchFileException {
        InputStream resource = ClassUtils.getClassLoader().getResourceAsStream(proName);
        try {
            properties.load(resource);
            for (String key : properties.stringPropertyNames()){
                PROP_MAP.put(key,properties.getProperty(key));
            }
        }catch (Exception e){
            throw new NoSuchFileException(proName);
        }finally {
            if (resource!=null){
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getString(String property){
        return PROP_MAP.get(property);
    }

    public static int getInt(String key,String typeName) {
        if (properties.containsKey(key)){
            return Integer.parseInt(getString(key));
        }
        return 0;
    }

    public static boolean getBoolean(String key,String typeName) {
        if (properties.containsKey(key)){
            return Boolean.parseBoolean(getString(key));
        }
        return false;
    }

    public static short getShort(String key,String typeName) {
        if (properties.containsKey(key)){
            return Short.parseShort(getString(key));
        }
        return (short)0;
    }

    public static long getLong(String key,String typeName) {
        if (properties.containsKey(key)){
            return Long.parseLong(getString(key));
        }
        return (long)0;
    }

    public static float getFloat(String key,String typeName) {
        if (properties.containsKey(key)){
            return Float.parseFloat(getString(key));
        }
        return (float) 0;
    }

    public static double getDouble(String key,String typeName) {
        if (properties.containsKey(key)){
            return Double.parseDouble(getString(key));
        }
        return (double) 0;
    }

    public static byte getByte(String key,String typeName) {
        if (properties.containsKey(key)){
            return Byte.parseByte(getString(key));
        }
        return (byte) 0;
    }

    public static char getChar(String key,String typeName) {
        if (properties.containsKey(key)){
            return getString(key).charAt(0);
        }
        return '0';
    }



    public static void main(String[] args) throws NoSuchFileException {
        ProUtils proUtils = new ProUtils("pro.properties");
        String base_package = proUtils.getString("BASE_PACKAGE");
        System.out.println(base_package);
    }
}
