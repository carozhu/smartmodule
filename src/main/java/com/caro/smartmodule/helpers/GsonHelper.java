package com.caro.smartmodule.helpers;

import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GsonUtils.java
 * Gson解析json的工具类，用于解析json字符串，或把bean转化为json字符串
 *
 * @author LiuSong  http://blog.csdn.net/ls703/article/details/41822209
 * @date 2014-12-9
 * @version V1.0
 */

/**
 * 在作者之上做了一些改进
 *
 * @author caro useage 请参照GSON用法和该包readme示例
 * 使用注意事项:在写class bean时,不管json节点如何命名.都应要保持一致,
 * eg: 数组节点为 banners.
 * 应该写为:
 * list<banners>  banners;//变量名一定要一致
 */
public class GsonHelper {


    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public GsonHelper() {

    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    public static Object jsonToBean(String jsonStr, Class<?> cl) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return obj;
    }


    /**
     * Type listType1 = (Type) new TypeToken<List<WinnerNoticeBean>>(){}.getType();
     * 将json格式转换成list对象，并准确指定类型
     * @param jsonStr
     * @param type
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, java.lang.reflect.Type type) {
        List<T> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 按章节点得到相应的内容
     *
     * @param jsonString
     *            json字符串
     * @param note
     *            节点
     * @return 节点对应的内容
     */
    public static String getNoteJsonString(String jsonString, String note) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        if (TextUtils.isEmpty(note)) {
            return null;
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if (element.isJsonNull()) {
            return null;
        }
        return element.getAsJsonObject().get(note).toString();
    }

    /**
     * 按照节点得到节点内容，然后传化为相对应的bean数组
     *
     * @param jsonString
     *            原json字符串
     * @param note
     *            节点标签
     * @param beanClazz
     *            要转化成的bean class
     * @return 返回bean的数组
     */
    public static <T> List<T> parserJsonToArrayBeans(String jsonString,
                                                     String note, Class<T> beanClazz) {
        String noteJsonString = getNoteJsonString(jsonString, note);
        return parserJsonToArrayBeans(noteJsonString, beanClazz);
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString
     *            json字符串
     * @param beanClazz
     *            集合里存入的数据对象
     * @return 含有目标对象的集合
     *
     *
     */
    public static <T> List<T> parserJsonToArrayBeans(String jsonString,
                                                     Class<T> beanClazz) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            return null;
        }
        if (!jsonElement.isJsonArray()) {
            return null;
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<T> beans = new ArrayList<T>();
        for (JsonElement jsonElement2 : jsonArray) {
            T bean = new Gson().fromJson(jsonElement2, beanClazz);
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 把相对应节点的内容封装为对象
     *
     * @param jsonString
     *            json字符串
     * @param @beanClazz
     *            要封装成的目标对象
     * @return 目标对象
     */
    public static <T> T parserJsonToArrayBean(String jsonString, Class<T> clazzBean) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
            //throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            //throw new RuntimeException("json字符串为空");
            return null;
        }
        if (!jsonElement.isJsonObject()) {
            //throw new RuntimeException("json不是一个对象");
            return null;
        }
        return new Gson().fromJson(jsonElement, clazzBean);
    }

    /**
     * 把相对应节点的内容封装为对象
     *
     * @param jsonString
     *            json字符串
     * @param @beanClazz
     *            要封装成的目标对象
     * @return 目标对象
     */
    public static <T> T parserJsonToBean(String jsonString,
                                         Class<T> clazzBean) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
            //throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            //throw new RuntimeException("json字符串为空");
            return null;
        }
        if (!jsonElement.isJsonObject()) {
            //throw new RuntimeException("json不是一个对象");
            return null;
        }
        return new Gson().fromJson(jsonElement, clazzBean);
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString
     *            json字符串
     * @param note
     *            json标签
     * @param @beanClazz
     *            集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public static <T> T parserJsonToArrayBean(String jsonString, String note,
                                              Class<T> clazzBean) {
        String noteJsonString = getNoteJsonString(jsonString, note);
        return parserJsonToArrayBean(noteJsonString, clazzBean);
    }

    /**
     * 把bean对象转化为json字符串
     *
     * @param obj
     *            bean对象
     * @return 返回的是json字符串
     */
    public static String toJsonString(Object obj) {
        if (obj != null) {
            return new Gson().toJson(obj);
        } else {
            throw new RuntimeException("对象不能为空");
        }
    }


    /**
     * 将对象转成json字符串 并使用url编码
     * @param o
     * @return
     */
    public static String formatURLString(Object o) {
        try {
            return URLEncoder.encode(formatURLString(o), "utf-8");
        } catch (Exception e) {
            return null;
        }
    }


    /***********************GSON  更简洁的封装 ****************************************/
    /**
     * https://github.com/finddreams/Android_BaseLib/blob/master/src/com/finddreams/baselib/utils/GsonUtil.java
     * @param object
     * @return
     */
    public static String createGsonString(Object object) {
        Gson gson = new Gson();
        String gsonString = gson.toJson(object);
        return gsonString;
    }


    public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString, cls);
        return t;
    }


    public static <T> List<T> changeGsonToList(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }


    public static <T> List<Map<String, T>> changeGsonToListMaps(
            String gsonString) {
        List<Map<String, T>> list = null;
        Gson gson = new Gson();
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }


    public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
        Map<String, T> map = null;
        Gson gson = new Gson();
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }
}
