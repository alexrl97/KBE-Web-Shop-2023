package com.kbe.web_shop.utils;

import com.google.gson.Gson;

public class Helper {

    public static boolean notNull(Object obj){
        return obj != null;
    }

    public static String toJsonString(Object obj) {
        return new Gson().toJson(obj);
    }
}
