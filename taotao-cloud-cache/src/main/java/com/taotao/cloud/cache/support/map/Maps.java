package com.taotao.cloud.cache.support.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuigedeng
 * @since 0.0.3
 */
public final class Maps {

    private Maps(){}

    /**
     * hashMap 实现策略
     * @param <K> key
     * @param <V> value
     * @return map 实现
     * @since 0.0.3
     */
    public static <K,V> Map<K,V> hashMap() {
        return new HashMap<>();
    }

}
