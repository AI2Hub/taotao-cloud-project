package com.taotao.cloud.cache.listener;


import com.taotao.cloud.cache.api.ICacheSlowListener;
import com.taotao.cloud.cache.api.ICacheSlowListenerContext;

/**
 * @author shuigedeng
 * @since 0.0.9
 */
public class MySlowListener implements ICacheSlowListener {

    @Override
    public void listen(ICacheSlowListenerContext context) {
        System.out.println("【慢日志】name: " + context.methodName());
    }

    @Override
    public long slowerThanMills() {
        return 0;
    }

}
