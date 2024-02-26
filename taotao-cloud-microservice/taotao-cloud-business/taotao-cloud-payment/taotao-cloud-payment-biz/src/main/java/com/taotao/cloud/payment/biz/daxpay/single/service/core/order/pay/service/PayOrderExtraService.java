package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayOrderExtraManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.pay.PayOrderExtraDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2024/1/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderExtraService {
    private final PayOrderExtraManager payOrderExtraManager;

    /**
     * 查询详情
     */
    public PayOrderExtraDto findById(Long id){
        return payOrderExtraManager.findById(id).map(PayOrderExtra::toDto).orElseThrow(()->new DataNotExistException("支付订单扩展信息不存在"));
    }
}
