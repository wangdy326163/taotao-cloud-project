package com.taotao.cloud.web.mvc.converter;

import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_TIME_FORMAT;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;


/**
 * 解决入参为 Date类型
 *
 * @author zuihou
 * @date 2019-04-30
 */
public class String2LocalTimeConverter extends BaseDateConverter<LocalTime> implements
	Converter<String, LocalTime> {

	protected static final Map<String, String> FORMAT = new LinkedHashMap(5);

	static {
		FORMAT.put(DEFAULT_TIME_FORMAT, "^\\d{1,2}:\\d{1,2}:\\d{1,2}$");
	}

	@Override
	protected Map<String, String> getFormat() {
		return FORMAT;
	}

	@Override
	public LocalTime convert(String source) {
		return super
			.convert(source, (key) -> LocalTime.parse(source, DateTimeFormatter.ofPattern(key)));
	}
}
