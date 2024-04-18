package com.taotao.cloud.payment.biz.daxpay.single.core.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号审查工具类
 * @author yxc
 * @since 2024/4/15
 */
@Slf4j
public class OrderNoGenerateUtil {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();
    private final static long ORDER_MAX_LIMIT = 999999L;

    private static String machineNo;

    /**
     * 生成支付订单号
     */
    public static String trade() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        long id = ATOMIC_LONG.incrementAndGet();
        log.info("dataStr:{},id:{}", dateStr, id);
        orderNo.append("T").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    /**
     * 生成退款订单号
     */
    public static String refund() {
        StringBuilder orderNo = new StringBuilder();
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        long id = ATOMIC_LONG.incrementAndGet();
        log.info("dataStr:{},id:{}", dateStr, id);
        orderNo.append("R").append(dateStr).append(machineNo).append(String.format("%06d", Math.abs(id) % ORDER_MAX_LIMIT));
        return orderNo.toString();
    }

    public static void setMachineNo(String machineNo) {
        OrderNoGenerateUtil.machineNo = machineNo;
    }
}
