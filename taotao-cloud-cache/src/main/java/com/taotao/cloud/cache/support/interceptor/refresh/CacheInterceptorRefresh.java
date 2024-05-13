package com.taotao.cloud.cache.support.interceptor.refresh;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheInterceptor;
import com.taotao.cloud.cache.api.ICacheInterceptorContext;

/**
 * 刷新
 *
 * @author shuigedeng
 * @since 0.0.5
 */
public class CacheInterceptorRefresh<K,V> implements ICacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorRefresh.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        log.debug("Refresh start");
        final ICache<K,V> cache = context.cache();
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
    }

}
