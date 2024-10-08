SET 'execution.checkpointing.interval' = '100s';
SET 'table.exec.state.ttl'= '8640000';
SET 'table.exec.mini-batch.enabled' = 'true';
SET 'table.exec.mini-batch.allow-latency' = '60s';
SET 'table.exec.mini-batch.size' = '10000';
SET 'table.local-time-zone' = 'Asia/Shanghai';
SET 'table.exec.sink.not-null-enforcer'='DROP';
SET 'table.exec.sink.upsert-materialize' = 'NONE';

CREATE CATALOG paimon_hive WITH (
    'type' = 'paimon',
    'metastore' = 'hive',
    'uri' = 'thrift://192.168.244.129:9083',
    'hive-conf-dir' = '/opt/software/apache-hive-3.1.3-bin/conf',
    'hadoop-conf-dir' = '/opt/software/hadoop-3.1.3/etc/hadoop',
    'warehouse' = 'hdfs:////user/hive/warehouse'
);

use CATALOG paimon_hive;

create  DATABASE IF NOT EXISTS dws;

CREATE TABLE IF NOT EXISTS dws.dws_trade_coupon_order_nd_full(
    `coupon_id`                BIGINT COMMENT '优惠券id',
    `k1`                       STRING COMMENT '分区字段',
    `coupon_name`              STRING COMMENT '优惠券名称',
    `coupon_type_code`         STRING COMMENT '优惠券类型id',
    `coupon_type_name`         STRING COMMENT '优惠券类型名称',
    `coupon_rule`              STRING COMMENT '优惠券规则',
    `start_date`               STRING COMMENT '发布日期',
    `original_amount_30d`      DECIMAL(16, 2) COMMENT '使用下单原始金额',
    `coupon_reduce_amount_30d` DECIMAL(16, 2) COMMENT '使用下单优惠金额',
    PRIMARY KEY (`coupon_id`,`k1` ) NOT ENFORCED
    )   PARTITIONED BY (`k1` ) WITH (
    'connector' = 'paimon',
    'metastore.partitioned-table' = 'true',
    'file.format' = 'parquet',
    'write-buffer-size' = '512mb',
    'write-buffer-spillable' = 'true' ,
    'partition.expiration-time' = '1 d',
    'partition.expiration-check-interval' = '1 h',
    'partition.timestamp-formatter' = 'yyyy-MM-dd',
    'partition.timestamp-pattern' = '$k1'
    );


INSERT INTO dws.dws_trade_coupon_order_nd_full(coupon_id, k1, coupon_name, coupon_type_code, coupon_type_name, coupon_rule, start_date, original_amount_30d, coupon_reduce_amount_30d)
select
    id,
    od.k1,
    coupon_name,
    coupon_type_code,
    coupon_type_name,
    benefit_rule,
    start_date,
    sum(split_original_amount),
    sum(split_coupon_amount)
from
    (
        select
            id,
            coupon_name,
            coupon_type_code,
            coupon_type_name,
            benefit_rule,
            date_format(start_time,'yyyy-MM-dd') start_date
        from dim.dim_coupon_full
    )cou
        left join
    (
        select
            coupon_id,
            k1,
            order_id,
            split_original_amount,
            split_coupon_amount
        from dwd.dwd_trade_order_detail_full
        where coupon_id is not null
    )od
    on cou.id=od.coupon_id
group by id,od.k1,coupon_name,coupon_type_code,coupon_type_name,benefit_rule,start_date;