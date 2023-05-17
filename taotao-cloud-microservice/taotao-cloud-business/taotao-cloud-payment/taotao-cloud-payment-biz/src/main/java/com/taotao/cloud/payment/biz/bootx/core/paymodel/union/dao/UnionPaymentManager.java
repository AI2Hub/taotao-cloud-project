package com.taotao.cloud.payment.biz.bootx.core.paymodel.union.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.daxpay.core.paymodel.union.entity.UnionPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @date 2022/3/11
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UnionPaymentManager extends BaseManager<UnionPaymentMapper, UnionPayment> {

}
