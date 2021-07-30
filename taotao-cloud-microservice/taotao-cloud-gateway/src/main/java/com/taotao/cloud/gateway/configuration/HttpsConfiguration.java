/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.gateway.configuration;

import com.taotao.cloud.gateway.properties.HttpsProperties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

/**
 * 配置http服务，使其即支持http又支持https服务（https通过配置文件配置）
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 22:11
 */
@Configuration
@ConditionalOnProperty(prefix = HttpsProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = false)
public class HttpsConfiguration {

	@Resource
	private HttpHandler httpHandler;

	@Resource
	private WebServer webServer;

	@PostConstruct
	public void start(HttpsProperties httpsProperties) {
		NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory(httpsProperties.getPort());
		WebServer webServer = factory.getWebServer(httpHandler);
		webServer.start();
	}

	@PreDestroy
	public void stop() {
		webServer.stop();
	}

}
