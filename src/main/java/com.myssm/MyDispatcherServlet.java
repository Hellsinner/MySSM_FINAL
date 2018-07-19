package com.myssm;

import com.myssm.annocation.MyController;
import com.myssm.annocation.MyRequestMapping;
import com.myssm.annocation.MyResponseBody;
import com.myssm.helper.BeanHelper;
import com.myssm.helper.IocHelper;
import com.myssm.utils.ClassUtils;
import com.myssm.utils.CtUtils;
import com.myssm.utils.JsonUtils;
import com.myssm.utils.ProUtils;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDispatcherServlet extends HttpServlet {

    private static final List<String> BaseType = new ArrayList<>();

    static {
        BaseType.add("int");
        BaseType.add("Integer");
        BaseType.add("Double");
        BaseType.add("double");
        BaseType.add("Long");
        BaseType.add("long");
    }

    private static final Map<String,Method> handlerMapping = new HashMap<>();
    private static final Map<String, CtMethod> paramMapping = new HashMap<>();
    private static final Map<String,Object> controllerMap = new HashMap<>();
    private static final String Prefix = "/WEB-INF/view/";
    private static final String Suffix = ".jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        ProUtils proUtils = null;

        try {
            //加载配置文件
            proUtils = new ProUtils("pro.properties");
            String base_package = proUtils.getString("BASE_PACKAGE");
            ClassUtils.doScanner(base_package);
            ClassUtils.doClass();
            BeanHelper.doInstance();
            IocHelper.ioc();
            //获取Mapping映射
            initHandlerMapping();
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initHandlerMapping() throws NotFoundException {
        Map<Class, Object> beanMap = BeanHelper.getBeanMap();
        if (MapUtils.isEmpty(beanMap))
            return;
        for (Map.Entry<Class,Object> entry : beanMap.entrySet()){

            Class clazz = entry.getKey();
            if (!clazz.isAnnotationPresent(MyController.class))
                continue;
            Object instance = entry.getValue();

            String baseUrl = "";
            if (clazz.isAnnotationPresent(MyRequestMapping.class)){
                MyRequestMapping annotation = (MyRequestMapping) clazz.getAnnotation(MyRequestMapping.class);
                baseUrl += annotation.value();
            }

            Method[] methods = clazz.getDeclaredMethods();
            CtMethod[] ctMethods = CtUtils.getCtMethods(clazz);

            for (int i=0;i<methods.length;i++){
                Method method = methods[i];
                if (!method.isAnnotationPresent(MyRequestMapping.class))
                    continue;
                MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
                String url = annotation.value();
                url = (baseUrl + "/" +url).replaceAll("/+","/");
                handlerMapping.put(url,method);
                paramMapping.put(url,ctMethods[i]);
                controllerMap.put(url,instance);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dispathcer(req,resp);
        }catch (Exception e){
            resp.getWriter().write("500! Server Exception");
        }
    }

    public void dispathcer(HttpServletRequest req, HttpServletResponse resp) throws IOException, NotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (MapUtils.isEmpty(handlerMapping))
            return;
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        uri = uri.replace(contextPath,"").replaceAll("/+","/");
        if (uri.endsWith("/")){
            uri = uri.substring(0,uri.length()-1);
        }
        if (!handlerMapping.containsKey(uri)){
            resp.getWriter().write("404 Not Found");
            return;
        }

        Method method = handlerMapping.get(uri);
        CtMethod ctMethod = paramMapping.get(uri);

        Map<String,String[]> parameterMap = req.getParameterMap();

        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] paramsName = CtUtils.getParamsName(ctMethod);

        Object[] paramValues = new Object[parameterTypes.length];
        //参数绑定
        for (int i=0;i<parameterTypes.length;i++){
            Class<?> parameterType = parameterTypes[i];
            String requestParam = parameterType.getSimpleName();

            if (requestParam.equals("HttpServletRequest")){
                paramValues[i] = req;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")){
                paramValues[i] = resp;
                continue;
            }
            if (requestParam.equals("HttpSession")){
                paramValues[i] = req.getSession();
                continue;
            }

            if (parameterType.isArray()){
                paramValues[i] = parameterMap.get(paramsName[i]);
                continue;
            }

            //如果是基本数据类型(包括包装类型)

            if (requestParam.equals("int") || requestParam.equals("Integer")){
                paramValues[i] = getInt(parameterMap,paramsName[i]);
                continue;
            }
            if (requestParam.equals("long") || requestParam.equals("Long")){
                paramValues[i] = getLong(parameterMap,paramsName[i]);
                continue;
            }
            if (requestParam.equals("double") || requestParam.equals("Double")){
                paramValues[i] = getDouble(parameterMap,paramsName[i]);
                continue;
            }
            if (requestParam.equals("String")){
                paramValues[i] = getString(parameterMap,paramsName[i]);
                continue;
            }

            //对象
            Object object = getObject(parameterMap, parameterType);
            paramValues[i] = object;
        }
        //调用方法
        try {
            Object invoke = method.invoke(controllerMap.get(uri), paramValues);
            //带有MyResponseBody,使用Json工具类转换返回对象
            if (method.isAnnotationPresent(MyResponseBody.class)){
                String json = JsonUtils.ObjectToJson(invoke);
                PrintWriter writer = resp.getWriter();
                writer.write(json);
            }
            //返回String，说明是视图名
            if (invoke instanceof String) {
                String view = (String) invoke;
                String path = Prefix + view + Suffix;
                req.getRequestDispatcher(path).forward(req,resp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int getInt(Map<String,String[]> map,String name){
        String[] strings = map.get(name);
        if (ArrayUtils.isEmpty(strings))
            return 0;
        return Integer.parseInt(strings[0]);
    }
    private static long getLong(Map<String,String[]> map,String name){
        String[] strings = map.get(name);
        if (ArrayUtils.isEmpty(strings))
            return 0;
        return Long.parseLong(strings[0]);
    }
    private static double getDouble(Map<String,String[]> map,String name){
        String[] strings = map.get(name);
        if (ArrayUtils.isEmpty(strings))
            return 0;
        return Double.parseDouble(strings[0]);
    }
    private static String getString(Map<String,String[]> map,String name){
        String[] strings = map.get(name);
        if (ArrayUtils.isEmpty(strings))
            return null;
        return strings[0];
    }
    private static Object getObject(Map<String,String[]> map,Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object bean = clazz.newInstance();
        BeanUtils.populate(bean,map);
        return bean;
    }
}
