
package com.minfo.quanmei.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;


public class JSONUtil {


    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();



    public static  Gson getGson(){
        return  gson;
    }



    public static <T> T fromJson(String json,Class<T> clz){
        try {
            return gson.fromJson(json, clz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T fromJson(String json,Type type){

        try {
            return gson.fromJson(json, type);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
