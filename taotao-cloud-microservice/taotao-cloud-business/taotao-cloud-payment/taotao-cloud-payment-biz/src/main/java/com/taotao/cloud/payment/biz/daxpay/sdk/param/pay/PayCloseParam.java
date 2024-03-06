package com.taotao.cloud.payment.biz.daxpay.sdk.param.pay;

import cn.bootx.platform.daxpay.sdk.model.pay.PayCloseModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayRequest;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付关闭参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayCloseParam extends DaxPayRequest<PayCloseModel> {

    /** 支付ID */
    private Long paymentId;

    /** 业务号 */
    private String businessNo;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /** 是否不启用异步通知 */
    private boolean notNotify;

    /** 异步通知地址 */
    private String notifyUrl;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/close";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<PayCloseModel> toModel(String json) {
        return JSONUtil.toBean(json, new TypeReference<DaxPayResult<PayCloseModel>>() {}, false);
    }
}
