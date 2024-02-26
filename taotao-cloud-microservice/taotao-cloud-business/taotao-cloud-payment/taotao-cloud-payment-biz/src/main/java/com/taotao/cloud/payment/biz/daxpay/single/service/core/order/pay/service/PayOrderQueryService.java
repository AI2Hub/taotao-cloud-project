package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.QueryPayParam;
import cn.bootx.platform.daxpay.result.order.PayChannelOrderResult;
import cn.bootx.platform.daxpay.result.order.PayOrderResult;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.convert.PayOrderConvert;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayChannelOrderManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayOrderExtraManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayOrderManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.pay.PayOrderDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.order.PayOrderQuery;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 支付查询服务
 * @author xxm
 * @since 2024/1/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderQueryService {
    private final PayOrderManager payOrderManager;
    private final PayOrderExtraManager payOrderExtraManager;
    private final PayChannelOrderManager payChannelOrderManager;

    /**
     * 分页
     */
    public PageResult<PayOrderDto> page(PageParam pageParam, PayOrderQuery param) {
        Page<PayOrder> page = payOrderManager.page(pageParam, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public Optional<PayOrder> findById(Long paymentId) {
        return payOrderManager.findById(paymentId);
    }

    /**
     * 根据业务号查询
     */
    public Optional<PayOrder> findByBusinessNo(String businessNo) {
        return payOrderManager.findByBusinessNo(businessNo);
    }

    /**
     * 查询支付记录
     */
    public PayOrderResult queryPayOrder(QueryPayParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getBusinessNo()) && Objects.isNull(param.getPaymentId())){
            throw new ValidationFailedException("业务号或支付单ID不能都为空");
        }

        // 查询支付单
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderManager.findById(param.getPaymentId())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderManager.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }

        // 查询扩展数据
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(payOrder.getId())
                .orElseThrow(() -> new PayFailureException("支付订单不完整"));

        // 查询通道数据
        List<PayChannelOrder> orderChannelList = payChannelOrderManager.findAllByPaymentId(payOrder.getId());

        List<PayChannelOrderResult> channels = orderChannelList.stream()
                .map(PayOrderConvert.CONVERT::convertResult)
                .collect(Collectors.toList());

        PayOrderResult payOrderResult = new PayOrderResult();
        BeanUtil.copyProperties(payOrder, payOrderResult);
        payOrderResult.setPaymentId(payOrder.getId());
        payOrderResult.setDescription(payOrderExtra.getDescription())
                .setChannels(channels);

        return payOrderResult;
    }
}
