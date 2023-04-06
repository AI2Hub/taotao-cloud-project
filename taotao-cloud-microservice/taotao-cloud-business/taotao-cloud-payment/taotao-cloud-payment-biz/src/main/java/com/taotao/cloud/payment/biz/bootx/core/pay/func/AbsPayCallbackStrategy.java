/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.bootx.core.pay.func;

import cn.hutool.json.JSONUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.taotao.cloud.payment.biz.bootx.core.notify.dao.PayNotifyRecordManager;
import com.taotao.cloud.payment.biz.bootx.core.notify.entity.PayNotifyRecord;
import com.taotao.cloud.payment.biz.bootx.core.pay.result.PayCallbackResult;
import com.taotao.cloud.payment.biz.bootx.core.pay.service.PayCallbackService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付回调处理抽象接口
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbsPayCallbackStrategy {
    protected static final ThreadLocal<Map<String, String>> PARAMS = new TransmittableThreadLocal<>();
    private final RedisClient redisClient;
    private final PayNotifyRecordManager payNotifyRecordManager;
    private final PayCallbackService payCallbackService;

    /** 支付回调 */
    public String payCallback(Map<String, String> params) {
        PARAMS.set(params);
        try {
            log.info("支付回调处理: {}", params);
            // 验证消息
            if (!this.verifyNotify()) {
                return null;
            }
            // 去重处理
            if (!this.duplicateChecker()) {
                return null;
            }
            // 调用统一回调处理
            PayCallbackResult result = payCallbackService.callback(this.getPaymentId(), this.getTradeStatus(), params);
            // 记录回调记录
            this.saveNotifyRecord(result);
        } finally {
            PARAMS.remove();
        }
        return this.getReturnMsg();
    }

    /**
     * 支付类型
     *
     * @see PayChannelCode
     */
    public abstract int getPayChannel();

    /** 去重处理 */
    public boolean duplicateChecker() {
        // 判断10秒内是否已经回调处理
        String key = "payment:callback:duplicate:" + ":" + this.getPaymentId();
        return redisClient.setIfAbsent(key, "", 10 * 1000);
    }

    /** 验证信息格式 */
    public abstract boolean verifyNotify();

    /** 获取paymentId */
    public abstract Long getPaymentId();

    /**
     * 获取支付状态
     *
     * @see PayStatusCode
     */
    public abstract int getTradeStatus();

    /** 返回响应结果 */
    public abstract String getReturnMsg();

    /** 保存回调记录 */
    public void saveNotifyRecord(PayCallbackResult result) {
        PayNotifyRecord payNotifyRecord = new PayNotifyRecord()
                .setNotifyInfo(JSONUtil.toJsonStr(PARAMS.get()))
                .setNotifyTime(LocalDateTime.now())
                .setPaymentId(this.getPaymentId())
                .setPayChannel(this.getPayChannel())
                .setStatus(result.getCode())
                .setMsg(result.getMsg());
        payNotifyRecordManager.save(payNotifyRecord);
    }
}
