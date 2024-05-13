package com.taotao.cloud.cache.support.listener.slow;

import com.taotao.cloud.cache.api.ICacheSlowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 慢日志监听工具类
 * @author shuigedeng
 * @since 0.0.9
 */
public final class CacheSlowListeners {

    private CacheSlowListeners(){}

    /**
     * 无
     * @return 监听类列表
     * @since 0.0.9
     */
    public static List<ICacheSlowListener> none() {
        return new ArrayList<>();
    }

    /**
     * 默认实现
     * @return 默认
     * @since 0.0.9
     */
    public static ICacheSlowListener defaults() {
        return new CacheSlowListener();
    }

}
