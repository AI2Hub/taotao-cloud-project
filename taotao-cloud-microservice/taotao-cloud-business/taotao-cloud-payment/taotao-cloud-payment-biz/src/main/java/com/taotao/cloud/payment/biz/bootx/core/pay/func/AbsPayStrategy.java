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

import com.taotao.cloud.payment.biz.bootx.core.pay.result.PaySyncResult;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象支付策略基类 同步支付 异步支付 错误处理 关闭支付 撤销支付 支付网关同步 退款
 *
 * @author xxm
 * @date 2020/12/11
 */
@Getter
@Setter
public abstract class AbsPayStrategy {

    /** 支付对象 */
    private Payment payment = null;

    /** 支付参数 */
    private PayParam payParam = null;

    /** 支付方式参数 支付参数中的与这个不一致, 以这个为准 */
    private PayModeParam payMode = null;

    /**
     * 策略标示
     *
     * @see PayChannelCode
     */
    public abstract int getType();

    /** 初始化支付的参数 */
    public void initPayParam(Payment payment, PayParam payParam) {
        this.payment = payment;
        this.payParam = payParam;
    }

    /** 支付前处理 */
    public void doBeforePay() {
        this.doBeforePayHandler();
    }

    /** 支付前对Payment的处理 包含必要的校验以及对Payment对象的创建和保存操作 */
    protected void doBeforePayHandler() {}

    /** 支付操作 */
    public abstract void doPayHandler();

    /** 支付成功的处理方式 */
    public void doSuccessHandler() {}

    /** 支付失败的处理方式 */
    public void doErrorHandler(ExceptionInfo exceptionInfo) {}

    /** 异步支付成功的处理方式 */
    public void doAsyncSuccessHandler(Map<String, String> map) {}

    /** 异步支付失败的处理方式, 默认使用支付失败的处理方式 同步支付方式调用时同 this#doErrorHandler */
    public void doAsyncErrorHandler(ExceptionInfo exceptionInfo) {
        this.doErrorHandler(exceptionInfo);
    }

    /** 撤销支付操作，支付交易返回失败或支付系统超时，调用该接口撤销交易 默认为关闭本地支付记录 */
    public void doCancelHandler() {
        this.doCloseHandler();
    }

    /** 关闭本地支付记录 */
    public abstract void doCloseHandler();

    /** 退款 */
    public abstract void doRefundHandler();

    /**
     * 异步支付单与支付网关进行状态比对
     *
     * @see PaySyncStatus
     */
    public PaySyncResult doSyncPayStatusHandler() {
        return new PaySyncResult();
    }
}
