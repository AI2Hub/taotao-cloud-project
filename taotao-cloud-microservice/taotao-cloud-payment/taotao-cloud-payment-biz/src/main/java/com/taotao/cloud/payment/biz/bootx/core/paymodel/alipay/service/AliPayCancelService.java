package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.alipay.AliPayApi;
import com.taotao.cloud.payment.biz.bootx.code.paymodel.AliPayCode;
import com.taotao.cloud.payment.biz.bootx.core.pay.local.AsyncRefundLocal;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 支付宝支付取消和退款
 * @author xxm
 * @date 2021/4/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCancelService {

    /**
     * 关闭支付
     */
    public void cancelRemote(Payment payment){
        // 只有部分需要调用支付宝网关进行关闭
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setOutTradeNo(String.valueOf(payment.getId()));

        try {
            AlipayTradeCancelResponse response = AliPayApi.tradeCancelToResponse(model);
            log.info(JSONUtil.toJsonStr(response));
            if (!Objects.equals(AliPayCode.SUCCESS,response.getCode())){
                log.error("网关返回撤销失败: {}",response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("关闭订单失败:",e);
            throw new PayFailureException("关闭订单失败");
        }
    }

    /**
     * 退款
     */
    public void refund(Payment payment, BigDecimal amount){
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo(String.valueOf(payment.getId()));
        refundModel.setRefundAmount(amount.toPlainString());

        // 设置退款号
        AsyncRefundLocal.set(IdUtil.getSnowflakeNextIdStr());
        refundModel.setOutRequestNo(AsyncRefundLocal.get());
        try {
            AlipayTradeRefundResponse response = AliPayApi.tradeRefundToResponse(refundModel);
            log.info(JSONUtil.toJsonStr(response));
            if (!Objects.equals(AliPayCode.SUCCESS,response.getCode())){
                AsyncRefundLocal.setErrorMsg(response.getSubMsg());
                AsyncRefundLocal.setErrorCode(response.getCode());
                log.error("网关返回退款失败: {}",response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("订单退款失败:",e);
            AsyncRefundLocal.setErrorMsg(e.getErrMsg());
            AsyncRefundLocal.setErrorCode(e.getErrCode());
            throw new PayFailureException("订单退款失败");
        }
    }
}


