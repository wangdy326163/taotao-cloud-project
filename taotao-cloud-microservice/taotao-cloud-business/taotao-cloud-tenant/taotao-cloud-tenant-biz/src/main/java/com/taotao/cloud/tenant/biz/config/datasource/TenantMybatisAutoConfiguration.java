package com.taotao.cloud.tenant.biz.config.datasource;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.tangyh.basic.database.datasource.BaseMybatisConfiguration;
import top.tangyh.basic.database.properties.DatabaseProperties;

/**
 * 配置一些 Mybatis 常用重用拦截器
 *
 * @author zuihou
 * @date 2017-11-18 0:34
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({DatabaseProperties.class})
public class TenantMybatisAutoConfiguration extends BaseMybatisConfiguration {

	public TenantMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
		super(databaseProperties);
	}

}
