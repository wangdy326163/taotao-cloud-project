package com.taotao.cloud.oss.artislong.core.qingyun;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.qingyun.model.QingYunOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version QingYunOssProperties.java, v 1.0 2022/3/10 23:52 chenmin Exp $
 * Created on 2022/3/10
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.QINGYUN)
public class QingYunOssProperties extends QingYunOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, QingYunOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(QingYunOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, QingYunOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, QingYunOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
