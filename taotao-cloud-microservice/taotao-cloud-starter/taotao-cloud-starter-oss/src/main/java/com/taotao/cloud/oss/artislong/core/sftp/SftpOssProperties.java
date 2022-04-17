package com.taotao.cloud.oss.artislong.core.sftp;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.sftp.model.SftpOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version SftpOssProperties.java, v 1.1 2021/11/16 15:32 chenmin Exp $
 * Created on 2021/11/16
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.SFTP)
public class SftpOssProperties extends SftpOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, SftpOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(SftpOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, SftpOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, SftpOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
