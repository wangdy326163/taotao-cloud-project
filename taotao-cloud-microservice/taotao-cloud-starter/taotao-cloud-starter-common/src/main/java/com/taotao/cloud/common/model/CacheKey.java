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
package com.taotao.cloud.common.model;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * 缓存 key 封装
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheKey {

	/**
	 * redis key
	 */
	@NonNull
	private String key;

	/**
	 * 超时时间 秒
	 */
	private Duration expire;

	public CacheKey(final @NonNull String key) {
		this.key = key;
	}


}
