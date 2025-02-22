SET 'execution.checkpointing.interval' = '10s';
SET 'table.exec.state.ttl'= '8640000';
SET 'table.exec.mini-batch.enabled' = 'true';
SET 'table.exec.mini-batch.allow-latency' = '60s';
SET 'table.exec.mini-batch.size' = '10000';
SET 'table.local-time-zone' = 'Asia/Shanghai';
SET 'table.exec.sink.not-null-enforcer'='DROP';

CREATE TABLE sku_attr_value_full_mq (
    `id` bigint NOT NULL  COMMENT '编号',
    `attr_id` bigint  NULL COMMENT '属性id（冗余)',
    `value_id` bigint  NULL COMMENT '属性值id',
    `sku_id` bigint  NULL COMMENT 'skuid',
    `attr_name` string  NULL COMMENT '属性名',
    `value_name` string  NULL COMMENT '属性值名称',
    PRIMARY KEY(`id`) NOT ENFORCED
) WITH (
    'connector' = 'mysql-cdc',
    'scan.startup.mode' = 'earliest-offset',
    'hostname' = '192.168.244.129',
    'port' = '3306',
    'username' = 'root',
    'password' = '',
    'database-name' = 'gmall',
    'table-name' = 'sku_attr_value',
    'server-time-zone' = 'Asia/Shanghai'
      );

CREATE CATALOG iceberg_catalog WITH (
    'type' = 'iceberg',
    'metastore' = 'hive',
    'uri' = 'thrift://192.168.244.129:9083',
    'hive-conf-dir' = '/opt/software/apache-hive-3.1.3-bin/conf',
    'hadoop-conf-dir' = '/opt/software/hadoop-3.1.3/etc/hadoop',
    'warehouse' = 'hdfs:////user/hive/warehouse'
);

use CATALOG iceberg_catalog;

create  DATABASE IF NOT EXISTS iceberg_ods;


CREATE TABLE IF NOT EXISTS iceberg_ods.ods_sku_attr_value_full(
    `id` bigint NOT NULL  COMMENT '编号',
    `attr_id` bigint  NULL COMMENT '属性id（冗余)',
    `value_id` bigint  NULL COMMENT '属性值id',
    `sku_id` bigint  NULL COMMENT 'skuid',
    `attr_name` string  NULL COMMENT '属性名',
    `value_name` string  NULL COMMENT '属性值名称',
    PRIMARY KEY (`id`) NOT ENFORCED
    );

INSERT INTO iceberg_ods.ods_sku_attr_value_full  /*+ OPTIONS('upsert-enabled'='true') */(
    `id`,
    `attr_id`,
    `value_id`,
    `sku_id`,
    `attr_name`,
    `value_name`
)
select
    `id`,
    `attr_id`,
    `value_id`,
    `sku_id`,
    `attr_name`,
    `value_name`
from default_catalog.default_database.sku_attr_value_full_mq;
