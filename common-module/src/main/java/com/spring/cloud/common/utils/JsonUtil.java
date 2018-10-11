package com.spring.cloud.common.utils;





import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * JSON处理类
 *
 * @author Hom
 * @time 2017/6/30.
 */
public class JsonUtil {

    public JsonUtil(){

    }

    /**
     * 对象转String
     *
     * @param obj 待转换对象
     * @return String
     */
    public static String objectToString(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 对象/集合转String
     *
     * @param objects 待转换对象/集合
     * @return
     *
     * Created by Hom  on 2017/7/1 12:52
     */
    public static String objectToArray(Object objects) {
        JSONArray jsonArray = JSONArray.fromObject(objects);
        return jsonArray.toString();
    }

    /**
     * JSONString转对象
     *
     * @param jsonStr json字符串
     * @param clazz 目标对象Class
     * @param <T>
     * @return T
     *
     * Created by Hom  on 2017/7/1 12:53
     */
    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
        if(Objects.equals(null,jsonStr)) {
            return newInstance(clazz);
        } else {
            return JSON.parseObject(jsonStr, clazz);
        }
    }


    /**
     * JSONString转List集合
     *
     * @param jsonStr json字符串
     * @param clazz 目标对象Class
     * @param <T>
     * @return List
     *
     * Created by Hom  on 2017/7/1 12:54
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> clazz) {
        if(Objects.equals(null,jsonStr)) {
            return new ArrayList<T>();
        } else {
            return JSON.parseArray(jsonStr, clazz);
        }
    }

    /**
     * null时返回初始化实例
     *
     * @param clazz 目标对象Class
     * @param <T>
     * @return T
     */
    private static <T> T newInstance(Class<T> clazz) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
    }
}
