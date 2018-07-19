package com.myssm.helper;

import com.myssm.annocation.MyAutowried;
import com.myssm.annocation.MyValue;
import com.myssm.utils.ClassUtils;
import com.myssm.utils.ProUtils;
import com.myssm.utils.ReflectUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IocHelper {

    private static final Map<Class,Object> InterfaceToImpl = new HashMap<>();
    /**
     * 依赖注入
     */
    public static void ioc(){
        Map<Class, Object> beanMap = BeanHelper.getBeanMap();
        if (MapUtils.isEmpty(beanMap))
            return;
        for (Map.Entry<Class,Object> entry : beanMap.entrySet()){
            Class clazz = entry.getKey();
            Object instance = entry.getValue();
            Field[] fields = clazz.getDeclaredFields();
            if (!ArrayUtils.isEmpty(fields)){
                for (Field field : fields){
                    if (field.isAnnotationPresent(MyAutowried.class)){
                        Class<?> fieldType = field.getType();
                        Object o = beanMap.get(fieldType);
                        if (o==null){
                            Class<?> implClass = findImpl(fieldType);
                            Object in = beanMap.get(implClass);
                            ReflectUtils.setField(instance,field,in);
                        }else{
                            ReflectUtils.setField(instance,field,o);
                        }
                    }else if (field.isAnnotationPresent(MyValue.class)){
                        Class<?> fieldType = field.getType();

                        String typeSimpleName = fieldType.getSimpleName();
                        MyValue annotation = field.getAnnotation(MyValue.class);

                        String fieldName = annotation.value();
                        String convertName = convertName(fieldName);
                        injectField(typeSimpleName,instance,field,convertName);

                    }
                }
            }
        }
    }

    private static String convertName(String fieldName) {
        String reg = "(?<=\\$\\{).*(?=})";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(fieldName);
        if(!matcher.find())
            throw new RuntimeException();
        return matcher.group().trim();
    }

    private static Class<?> findImpl(Class<?> fieldType) {
        Class implClass = null;
        Set<Class<?>> classSet = ClassUtils.getClassSet();

        for (Class clazz : classSet) {
            Class[] interfaces = clazz.getInterfaces();

            int index = ArrayUtils.indexOf(interfaces, fieldType);
            if (index != -1) {
                implClass = clazz;
                break;
            }
        }
        return implClass;
    }

    private static void injectField(String typeSimpleName, Object instance, Field field, String fieldName){
        switch (typeSimpleName){
            case "int" : case  "Integer" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getInt(fieldName,typeSimpleName));
                break;
            case "short" : case "Short" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getShort(fieldName,typeSimpleName));
                break;
            case  "byte" : case "Byte" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getByte(fieldName,typeSimpleName));
                break;
            case  "float" : case "Float" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getFloat(fieldName,typeSimpleName));
                break;
            case  "double" : case "Double" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getDouble(fieldName,typeSimpleName));
                break;
            case  "char" : case "Character" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getChar(fieldName,typeSimpleName));
                break;
            case  "long" : case "Long" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getLong(fieldName,typeSimpleName));
                break;
            case  "boolean" : case "Boolean" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getBoolean(fieldName,typeSimpleName));
                break;
            case  "String" :
                ReflectUtils.setField(instance,field,
                        ProUtils.getString(fieldName));
                break;
        }
    }
}
