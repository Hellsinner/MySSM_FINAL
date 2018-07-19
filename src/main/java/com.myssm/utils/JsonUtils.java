package com.myssm.utils;

import com.google.gson.Gson;

/**
 * 转换Json工具
 */
public class JsonUtils {

    private static final Gson  GSON = new Gson();

    public static String ObjectToJson(Object object){
        if (object==null)
            return null;
        return GSON.toJson(object);
    }
}
