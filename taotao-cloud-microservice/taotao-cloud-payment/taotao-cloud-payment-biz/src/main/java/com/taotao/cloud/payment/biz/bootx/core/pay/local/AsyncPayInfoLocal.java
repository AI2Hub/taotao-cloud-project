package com.taotao.cloud.payment.biz.bootx.core.pay.local;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.taotao.cloud.payment.biz.bootx.dto.pay.AsyncPayInfo;

/**
* 异步支付线程变量
* @author xxm  
* @date 2021/4/21 
*/
public final class AsyncPayInfoLocal {
    private static final ThreadLocal<AsyncPayInfo> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置
     */
    public static void set(AsyncPayInfo asyncPayInfo) {
        THREAD_LOCAL.set(asyncPayInfo);
    }

    /**
     * 获取
     */
    public static AsyncPayInfo get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
