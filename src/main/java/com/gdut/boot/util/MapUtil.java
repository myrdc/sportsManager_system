package com.gdut.boot.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/3/2716:00
 */

public class MapUtil {


    /**
     * 返回查询的值
     * @param map map
     * @param key key
     * @param function 函数，返回值
     * @param <K>
     * @param <V>
     * @return 查询的或者默认值
     */
    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<K, V> function){
        V value = map.get(key);
        if(value != null){
            return value;
        }
        return map.computeIfAbsent(key, function::apply);
    }

    //返回键值对
    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    private MapUtil() {
        super();
    }
}
