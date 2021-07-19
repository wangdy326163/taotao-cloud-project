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
package com.taotao.cloud.web.mvc.converter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.common.enums.BaseEnum;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

/**
 * Integer枚举转化器
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/29 14:06
 */
public class IntegerToEnumConverter<T extends BaseEnum> implements Converter<Integer, T> {

	private final Map<Integer, T> enumMap = MapUtil.newHashMap();

	public IntegerToEnumConverter(Class<T> enumType) {
		T[] enums = enumType.getEnumConstants();
		for (T e : enums) {
			enumMap.put(e.getCode(), e);
		}
	}

	@Override
	public T convert(Integer source) {
		T t = enumMap.get(source);
		if (ObjectUtil.isNull(t)) {
			throw new IllegalArgumentException("无法匹配对应的枚举类型");
		}
		return t;
	}
}
