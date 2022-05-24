/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.processor.factories;


import com.taotao.cloud.processor.common.MultiSetMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;

/**
 * spring boot 自动化配置工具类
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-24 10:22:59
 */
class FactoriesFiles {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	/**
	 * 读取 spring.factories 文件
	 *
	 * @param fileObject FileObject
	 * @return MultiSetMap
	 * @throws IOException 异常信息
	 */
	protected static MultiSetMap<String, String> readFactoriesFile(FileObject fileObject,
		Elements elementUtils) throws IOException {
		// 读取 spring.factories 内容
		Properties properties = new Properties();
		try (InputStream input = fileObject.openInputStream()) {
			properties.load(input);
		}
		MultiSetMap<String, String> multiSetMap = new MultiSetMap<>();
		Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
		for (Map.Entry<Object, Object> objectEntry : entrySet) {
			String key = (String) objectEntry.getKey();
			String value = (String) objectEntry.getValue();
			if (value == null || value.trim().isEmpty()) {
				continue;
			}
			// 解析 spring.factories
			String[] values = value.split(",");
			Set<String> valueSet = Arrays.stream(values)
				.filter(v -> !v.isEmpty())
				.map(String::trim)
				// 校验是否删除文件
				.filter((v) -> Objects.nonNull(elementUtils.getTypeElement(v)))
				.collect(Collectors.toSet());
			multiSetMap.putAll(key.trim(), valueSet);
		}
		return multiSetMap;
	}

	/**
	 * 读取已经存在的 AutoConfiguration imports
	 *
	 * @param fileObject FileObject
	 * @return Set
	 * @throws IOException IOException
	 */
	protected static Set<String> readAutoConfigurationImports(FileObject fileObject)
		throws IOException {
		Set<String> set = new HashSet<>();
		try (
			InputStream input = fileObject.openInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input))
		) {
			reader.lines()
				.map(String::trim)
				.filter(line -> !line.startsWith("#"))
				.forEach(set::add);
		}
		return set;
	}

	/**
	 * 写出 spring.factories 文件
	 *
	 * @param factories factories 信息
	 * @param output    输出流
	 * @throws IOException 异常信息
	 */
	protected static void writeFactoriesFile(MultiSetMap<String, String> factories,
		OutputStream output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
		writer.write("# Generated by mica-auto www.dreamlu.net\n");
		Set<String> keySet = factories.keySet();
		for (String key : keySet) {
			Set<String> values = factories.get(key);
			if (values == null || values.isEmpty()) {
				continue;
			}
			writer.write(key);
			writer.write("=\\\n  ");
			StringJoiner joiner = new StringJoiner(",\\\n  ");
			for (String value : values) {
				joiner.add(value);
			}
			writer.write(joiner.toString());
			writer.newLine();
		}
		writer.flush();
	}

	/**
	 * 写出 spring-devtools.properties
	 *
	 * @param projectName 项目名
	 * @param output      输出流
	 * @throws IOException 异常信息
	 */
	protected static void writeDevToolsFile(String projectName,
		OutputStream output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
		// restart.include.mica-cloud=/mica-cloud[\\w\\d-.]+\\.jar
		String format = "restart.include.%s=/%s[\\\\w\\\\d-.]+\\\\.jar";
		writer.write("# Generated by mica-auto www.dreamlu.net\n");
		writer.write(String.format(format, projectName, projectName));
		writer.flush();
	}

	/**
	 * 写出 AutoConfiguration imports
	 *
	 * @param allAutoConfigurationImports allAutoConfigurationImports
	 * @param output                      OutputStream
	 * @throws IOException IOException
	 */
	protected static void writeAutoConfigurationImportsFile(Set<String> allAutoConfigurationImports,
		OutputStream output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
		StringJoiner joiner = new StringJoiner("\n");
		for (String configurationImport : allAutoConfigurationImports) {
			joiner.add(configurationImport);
		}
		writer.write(joiner.toString());
		writer.flush();
	}

}
