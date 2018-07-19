package com.myssm.mybatis;

import com.myssm.utils.ClassUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanMapperXml {

    public static void main(String[] args) {
        List<MapperInfo> mapperInfoList = buildMapper("UserMapper.xml");
        System.out.println(mapperInfoList);
    }

    public static List<MapperInfo> buildMapper(String mapperPath){
        List<MapperInfo> mapperInfoList = new ArrayList<>();


        //创建SAXReader
        SAXReader reader = new SAXReader();

        Document document = null;

        try {
            document = reader.read(ClassUtils.getClassLoader().getResourceAsStream(mapperPath));
        }catch (Exception e){
            e.printStackTrace();
        }

        //获取根节点
        Element root = document.getRootElement();

        String namespace = root.attributeValue("namespace");

        //获取所有的节点
        List<Element> elements = root.elements();
        for (Element element : elements){
            MapperInfo mapperInfo = new MapperInfo();
            mapperInfo.setNameSpace(namespace);
            List<String> paramMapping = new ArrayList<>();
            mapperInfo.setType(element.getName());
            mapperInfo.setId(element.attributeValue("id"));
            mapperInfo.setParamType(element.attributeValue("paramType"));
            mapperInfo.setReurnType(element.attributeValue("resultType"));
            String sql = element.getTextTrim();
            sql = getParam(sql, paramMapping);
            mapperInfo.setSql(sql);
            mapperInfo.setParamMapping(paramMapping);
            mapperInfoList.add(mapperInfo);
        }
        return mapperInfoList;
    }


    public static String getParam(String sql,List<String> params){
        String rgex = "#\\{.*?}";
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(sql);
        while (m.find()) {
            String group = m.group();
            String s = convertToStr(group);
            params.add(s);
        }

        String s = m.replaceAll("?");
        return s;
    }

    private static String convertToStr(String group) {
        String reg = "(?<=#\\{).*(?=})";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(group);
        if(!matcher.find())
            throw new RuntimeException();
        return matcher.group().trim();
    }
}
