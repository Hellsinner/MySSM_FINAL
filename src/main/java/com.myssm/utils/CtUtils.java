package com.myssm.utils;

import com.myssm.common.TestController;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class CtUtils {
    //获取增强方法对象
    public static CtMethod[] getCtMethods(Class clazz) throws NotFoundException {
        ClassPool aDefault = ClassPool.getDefault();
        CtClass ctClass = aDefault.get(clazz.getName());

        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

        return declaredMethods;
    }
    //获取参数名列表
    public static String[] getParamsName(CtMethod ctMethod) throws NotFoundException {
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        String[] paramNames = new String[ctMethod.getParameterTypes().length];
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr != null)  {
            int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++){
                paramNames[i] = attr.variableName(i + pos);
            }
            return paramNames;
        }
        return null;
    }

    public static void main(String[] args) throws NotFoundException {
        Class ca = TestController.class;

        ClassPool aDefault = ClassPool.getDefault();
        CtClass ctClass = aDefault.get("com.myssm.common.TestController");

        CtMethod add = ctClass.getDeclaredMethod("add");

        CtMethod[] ctMethods = getCtMethods(ca);

        CtClass[] parameterTypes = add.getParameterTypes();

        for (CtClass ctClass1 : parameterTypes){
            System.out.println(ctClass1.isArray());
        }
    }
}
