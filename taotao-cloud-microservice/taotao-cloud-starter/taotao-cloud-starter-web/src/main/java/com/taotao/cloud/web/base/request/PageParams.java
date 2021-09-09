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
package com.taotao.cloud.web.base.request;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.common.constant.StrPoolConstant;
import com.taotao.cloud.common.utils.AntiSqlFilterUtils;
import com.taotao.cloud.data.mybatis.plus.entity.Entity;
import com.taotao.cloud.data.mybatis.plus.entity.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页参数
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:17:47
 */
@Schema(name = "PageParams", description = "分页参数")
public class PageParams<T> {

	/**
	 * 查询参数
	 */
	@NotNull(message = "查询对象model不能为空")
	@Schema(description = "查询参数", example = "10", required = true)
	private T model;

	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5)
	@Max(value = 100)
	private long size = 10;

	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0)
	@Max(value = Integer.MAX_VALUE)
	private long current = 1;

	/**
	 * 排序
	 */
	@Schema(description = "排序,默认createTime", allowableValues = "id,createTime,updateTime", example = "id")
	private String sort = SuperEntity.FIELD_ID;

	/**
	 * 排序规则
	 */
	@Schema(description = "排序规则, 默认descending", allowableValues = "descending,ascending", example = "descending")
	private String order = "descending";

	/**
	 * 扩展参数
	 */
	@Schema(description = "扩展参数")
	private Map<String, Object> extra = new HashMap<>(16);

	/**
	 * 支持多个字段排序，用法： eg.1, 参数：{order:"name,id", order:"descending,ascending" }。 排序： name desc, id asc
	 * eg.2, 参数：{order:"name", order:"descending,ascending" }。 排序： name desc eg.3,
	 * 参数：{order:"name,id", order:"descending" }。 排序： name desc
	 *
	 * @return {@link com.baomidou.mybatisplus.core.metadata.IPage }
	 * @author shuigedeng
	 * @since 2021-09-02 21:19:05
	 */
	@JsonIgnore
	public <E> IPage<E> buildPage() {
		PageParams params = this;
		//没有排序参数
		if (StrUtil.isEmpty(params.getSort())) {
			return new Page(params.getCurrent(), params.getSize());
		}

		Page<E> page = new Page(params.getCurrent(), params.getSize());

		List<OrderItem> orders = new ArrayList<>();
		String[] sortArr = StrUtil.splitToArray(params.getSort(), StrPoolConstant.COMMA.charAt(0));
		String[] orderArr = StrUtil.splitToArray(params.getOrder(), StrPoolConstant.COMMA.charAt(0));

		int len = Math.min(sortArr.length, orderArr.length);
		for (int i = 0; i < len; i++) {
			String humpSort = sortArr[i];
			// 简单的 驼峰 转 下划线
			String underlineSort = StrUtil.toUnderlineCase(humpSort);

			// 除了 create_time 和 updateTime 都过滤sql关键字
			if (!StrUtil.equalsAny(humpSort, SuperEntity.CREATE_TIME, Entity.UPDATE_TIME)) {
				underlineSort = AntiSqlFilterUtils.getSafeValue(underlineSort);
			}

			orders.add(
					StrUtil.equalsAny(orderArr[i], "ascending", "ascend") ? OrderItem.asc(
							underlineSort)
							: OrderItem.desc(underlineSort));
		}

		page.setOrders(orders);

		return page;
	}

	/**
	 * 计算当前分页偏移量
	 *
	 * @return long
	 * @author shuigedeng
	 * @since 2021-09-02 21:19:16
	 */
	@JsonIgnore
	public long offset() {
		long current = this.current;
		if (current <= 1L) {
			return 0L;
		}
		return (current - 1) * this.size;
	}

	/**
	 * put
	 *
	 * @param key   key
	 * @param value value
	 * @return {@link com.taotao.cloud.web.base.request.PageParams }
	 * @author shuigedeng
	 * @since 2021-09-02 21:19:25
	 */
	@JsonIgnore
	public PageParams<T> put(String key, Object value) {
		if (this.extra == null) {
			this.extra = new HashMap<>(16);
		}
		this.extra.put(key, value);
		return this;
	}

	/**
	 * putAll
	 *
	 * @param extra extra
	 * @return {@link com.taotao.cloud.web.base.request.PageParams }
	 * @author shuigedeng
	 * @since 2021-09-02 21:19:30
	 */
	@JsonIgnore
	public PageParams<T> putAll(Map<String, Object> extra) {
		if (this.extra == null) {
			this.extra = new HashMap<>(16);
		}
		this.extra.putAll(extra);
		return this;
	}

	public PageParams() {
	}

	public PageParams(T model, long size, long current, String sort, String order,
			Map<String, Object> extra) {
		this.model = model;
		this.size = size;
		this.current = current;
		this.sort = sort;
		this.order = order;
		this.extra = extra;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public static <T> PageParamsBuilder<T> builder() {
		return new PageParamsBuilder<>();
	}

	public static final class PageParamsBuilder<T> {

		private T model;
		private long size = 10;
		private long current = 1;
		private String sort = SuperEntity.FIELD_ID;
		private String order = "descending";
		private Map<String, Object> extra = new HashMap<>(16);

		private PageParamsBuilder() {
		}

		public static PageParamsBuilder aPageParams() {
			return new PageParamsBuilder();
		}

		public PageParamsBuilder model(T model) {
			this.model = model;
			return this;
		}

		public PageParamsBuilder size(long size) {
			this.size = size;
			return this;
		}

		public PageParamsBuilder current(long current) {
			this.current = current;
			return this;
		}

		public PageParamsBuilder sort(String sort) {
			this.sort = sort;
			return this;
		}

		public PageParamsBuilder order(String order) {
			this.order = order;
			return this;
		}

		public PageParamsBuilder extra(Map<String, Object> extra) {
			this.extra = extra;
			return this;
		}

		public PageParams build() {
			PageParams pageParams = new PageParams();
			pageParams.setModel(model);
			pageParams.setSize(size);
			pageParams.setCurrent(current);
			pageParams.setSort(sort);
			pageParams.setOrder(order);
			pageParams.setExtra(extra);
			return pageParams;
		}
	}
}
