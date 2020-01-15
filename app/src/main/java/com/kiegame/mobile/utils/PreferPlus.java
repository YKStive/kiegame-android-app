package com.kiegame.mobile.utils;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description: 轻量对象储存
 */
public class PreferPlus {

    /**
     * 储存对象
     *
     * @param key    键
     * @param object 值
     */
    public static void put(String key, Object object) {
        Prefer.put(key, JSON.toJson(object));
    }

    /**
     * 获取对象
     *
     * @param key   键
     * @param clazz 对象类型
     * @param <T>   泛型
     * @return 值
     */
    public static <T> T get(String key, Class<T> clazz) {
        String json = Prefer.get(key, "");
        return JSON.toObject(json, clazz);
    }

    /**
     * 删除对象
     *
     * @param key 键
     */
    public static void remove(String key) {
        Prefer.remove(key);
    }
}
