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
package com.taotao.cloud.rabbitmq.producer;

import com.taotao.cloud.rabbitmq.common.DetailResponse;

/**
 * 消息处理者
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/29 15:55
 */
public interface MessageProcess<T> {

	/**
	 * 处理消息
	 *
	 * @param message message
	 * @author dengtao
	 * @since 2020/9/29 15:56
	 */
	DetailResponse process(T message);
}
