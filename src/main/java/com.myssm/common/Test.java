package com.myssm.common;

import com.myssm.annocation.MyRequestMapping;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NotFoundException {
        String[] adds = getMethodVariableName("com.myssm.common.TestController", "add");
        for (String name : adds){
            System.out.println(name);
        }



        Class ca = TestController.class;

        ClassPool aDefault = ClassPool.getDefault();
        CtClass ctClass = aDefault.get("com.myssm.common.TestController");

        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

        Method[] methods = ca.getDeclaredMethods();

        System.out.println(declaredMethods.length+"----"+methods.length);
    }

    public static String[] getMethodVariableName(String classname,String methodname){
        try{
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(classname);
            CtMethod[] declaredMethods = cc.getDeclaredMethods();
            for (CtMethod ctMethod : declaredMethods){
                if (!ctMethod.hasAnnotation(MyRequestMapping.class))
                    continue;
                CtClass[] parameterTypes = ctMethod.getParameterTypes();
                for (CtClass ctClass : parameterTypes){

                }
            }

            CtMethod cm = cc.getDeclaredMethod(methodname);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            String[] paramNames = new String[cm.getParameterTypes().length];
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null)  {
                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                for (int i = 0; i < paramNames.length; i++){
                    paramNames[i] = attr.variableName(i + pos);
                }
                return paramNames;
            }
        }catch(Exception e){
            System.out.println("getMethodVariableName fail "+e);
        }
        return null;
    }

}
