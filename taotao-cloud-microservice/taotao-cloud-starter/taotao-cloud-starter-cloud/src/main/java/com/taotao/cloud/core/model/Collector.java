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
package com.taotao.cloud.core.model;

import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.NumberUtil;
import com.taotao.cloud.core.model.Callable.Func0;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义采集器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:30:39
 */
public class Collector {

	private CoreProperties coreProperties;
	/**
	 * map
	 */
	private final Map<String, Object> map = new ConcurrentHashMap<>();

	/**
	 * lock
	 */
	private final Object lock = new Object();

	public Collector(CoreProperties coreProperties) {
		this.coreProperties = coreProperties;
	}

	/**
	 * get
	 *
	 * @param key  key
	 * @param type type
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-02 20:31:00
	 */
	protected Object get(String key, Class type) {
		if (!map.containsKey(key)) {
			synchronized (lock) {
				if (!map.containsKey(key)) {
					Object obj = createFactory(key, type);
					map.put(key, obj);
				}
			}
		}
		return map.get(key);
	}

	/**
	 * createFactory
	 *
	 * @param key  key
	 * @param type type
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-02 20:31:04
	 */
	private Object createFactory(String key, Class type) {
		try {
			Object obj = type.newInstance();
			if (type == Hook.class) {
				Hook hook = (Hook) obj;
				hook.setCoreProperties(coreProperties);
				hook.setKey(key);
				hook.setMaxLength(PropertyUtil.getPropertyCache(key + ".length", 10));
			}
			return obj;
		} catch (Exception e) {
			LogUtil.error(e);
			throw new BaseException(e);
		}
	}

	/**
	 * sum
	 *
	 * @param key key
	 * @return {@link com.taotao.cloud.core.model.Collector.Sum }
	 * @author shuigedeng
	 * @since 2021-09-02 20:31:08
	 */
	public Sum sum(String key) {
		return (Sum) get(key, Sum.class);
	}

	/**
	 * hook
	 *
	 * @param key key
	 * @return {@link com.taotao.cloud.core.model.Collector.Hook }
	 * @author shuigedeng
	 * @since 2021-09-02 20:31:13
	 */
	public Hook hook(String key) {
		return (Hook) get(key, Hook.class);
	}

	/**
	 * call
	 *
	 * @param key key
	 * @return {@link com.taotao.cloud.core.model.Collector.Call }
	 * @author shuigedeng
	 * @since 2021-09-02 20:31:16
	 */
	public Call call(String key) {
		return (Call) get(key, Call.class);
	}

	/**
	 * value
	 *
	 * @param key key
	 * @return {@link com.taotao.cloud.core.model.Collector.Value }
	 * @author shuigedeng
	 * @since 2021-09-02 20:31:18
	 */
	public Value value(String key) {
		return (Value) get(key, Value.class);
	}

	/**
	 * Value
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:31:26
	 */
	public static class Value {

		/**
		 * value
		 */
		protected Object value;

		public void set(Object value) {
			this.value = value;
		}

		public Object get() {
			return this.value;
		}

		public void reset() {
			this.value = null;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}


	/**
	 * 累加
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:31:36
	 */
	public static class Sum {

		/**
		 * sum
		 */
		protected AtomicInteger sum = new AtomicInteger(0);

		public void add(int count) {
			sum.addAndGet(count);
		}

		public int get() {
			return sum.get();
		}

		public void reset() {
			sum.set(0);
		}

		public AtomicInteger getSum() {
			return sum;
		}

		public void setSum(AtomicInteger sum) {
			this.sum = sum;
		}
	}

	/**
	 * 拦截
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:31:46
	 */
	public static class Hook {

		private CoreProperties coreProperties;

		/**
		 * 当前正在处理数
		 */
		protected AtomicLong current = new AtomicLong(0);
		/**
		 * 最近每秒失败次数
		 */
		protected AtomicLong lastErrorPerSecond = new AtomicLong(0);
		/**
		 * 最近每秒成功次数
		 */
		protected AtomicLong lastSuccessPerSecond = new AtomicLong(0);
		/**
		 * sortList
		 */
		protected SortList sortList = new SortList();
		/**
		 * lastMinTimeSpan
		 */
		protected double lastMinTimeSpan = 0;
		/**
		 * sortListPerMinute
		 */
		protected SortList sortListPerMinute = new SortList();
		/**
		 * lastMinTimeSpanPerMinute
		 */
		protected double lastMinTimeSpanPerMinute = 0;
		/**
		 * maxLength
		 */
		protected Integer maxLength = 10;
		/**
		 * lastSecond
		 */
		protected volatile Long lastSecond = 0L;
		/**
		 * lastMinute
		 */
		protected volatile Long lastMinute = 0L;
		/**
		 * method
		 */
		protected Method method;
		/**
		 * key
		 */
		private String key;

		/**
		 * getCurrent
		 *
		 * @return {@link java.lang.Long }
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:05
		 */
		public Long getCurrent() {
			return current.get();
		}

		/**
		 * getLastErrorPerSecond
		 *
		 * @return {@link java.lang.Long }
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:08
		 */
		public Long getLastErrorPerSecond() {
			return lastErrorPerSecond.get();
		}

		/**
		 * getLastSuccessPerSecond
		 *
		 * @return {@link java.lang.Long }
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:12
		 */
		public Long getLastSuccessPerSecond() {
			return lastSuccessPerSecond.get();
		}

		/**
		 * run
		 *
		 * @param tag        tag
		 * @param obj        obj
		 * @param methodName methodName
		 * @param params     params
		 * @return {@link java.lang.Object }
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:15
		 */
		public Object run(String tag, Object obj, String methodName, Object[] params) {
			if (method == null) {
				Optional<Method> find = Arrays.stream(obj.getClass().getMethods())
					.filter(c -> methodName.equalsIgnoreCase(c.getName())).findFirst();
				if (!find.isPresent()) {
					throw new BaseException("未找到方法:" + obj.getClass().getName() + "下" + methodName);
				}
				method = find.get();
			}

			return run(tag, () -> {
				try {
					return method.invoke(obj, params);
				} catch (Exception exp) {
					throw new BaseException(exp);
				}
			});
		}

		/**
		 * run
		 *
		 * @param tag    tag
		 * @param action action
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:19
		 */
		public void run(String tag, Callable.Action0 action) {
			run(tag, () -> {
				action.invoke();
				return null;
			});
		}

		/**
		 * run
		 *
		 * @param tag  tag
		 * @param func func
		 * @param <T>  T
		 * @return T
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:24
		 */
		public <T> T run(String tag, Callable.Func0<T> func) {
			try {
				if (Objects.isNull(coreProperties) || !coreProperties.isCollectHookEnabled()) {
					return func.invoke();
				}

				current.getAndIncrement();
				//每秒重新计数,不用保证十分精确
				long second = System.currentTimeMillis() / 1000;
				if (second != lastSecond) {
					lastSecond = second;
					lastErrorPerSecond.set(0);
					lastSuccessPerSecond.set(0);
					if (lastSecond / 60 != lastMinute) {
						lastMinute = lastSecond / 60;
						sortListPerMinute.removeMore(0);
					}
				}

				long start = System.currentTimeMillis();
				T result = func.invoke();
				long timeSpan = System.currentTimeMillis() - start;
				insertOrUpdate(tag, timeSpan);
				insertOrUpdatePerMinute(tag, timeSpan);
				lastSuccessPerSecond.getAndIncrement();
				return result;
			} catch (Exception exp) {
				lastErrorPerSecond.getAndIncrement();
				throw exp;
			} finally {
				current.getAndDecrement();
			}
		}

		/**
		 * insertOrUpdate
		 *
		 * @param info     info
		 * @param timeSpan timeSpan
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:38
		 */
		protected void insertOrUpdate(Object info, double timeSpan) {
			if (info == null || timeSpan < lastMinTimeSpan) {
				return;
			}

			try {
				sortList.add(new SortInfo(info, timeSpan, timeSpan, new AtomicLong(1)));
				sortList.removeMore(this.maxLength);
				SortInfo last = sortList.getLast();
				if (last != null) {
					lastMinTimeSpan = last.getTime();
				}
			} catch (Exception exp) {
				LogUtil.error(exp, "Collector hook 保存耗时统计出错");
			}
		}

		/**
		 * insertOrUpdatePerMinute
		 *
		 * @param info     info
		 * @param timeSpan timeSpan
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:40
		 */
		protected void insertOrUpdatePerMinute(Object info, double timeSpan) {
			if (info == null || timeSpan < lastMinTimeSpanPerMinute) {
				return;
			}

			try {
				sortListPerMinute.add(new SortInfo(info, timeSpan, timeSpan, new AtomicLong(1)));
				sortListPerMinute.removeMore(this.maxLength);
				SortInfo last = sortListPerMinute.getLast();
				if (last != null) {
					lastMinTimeSpanPerMinute = last.getTime();
				}
			} catch (Exception exp) {
				LogUtil.error(exp, "Collector hook 保存耗时统计出错");
			}
		}

		/**
		 * 最长耗时列表n条
		 *
		 * @return {@link com.taotao.cloud.core.model.Collector.SortList }
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:47
		 */
		public SortList getMaxTimeSpanList() {
			return sortList;
		}

		/**
		 * 最长耗时列表n条每分钟
		 *
		 * @return {@link com.taotao.cloud.core.model.Collector.SortList }
		 * @author shuigedeng
		 * @since 2021-09-02 20:34:56
		 */
		public SortList getMaxTimeSpanListPerMinute() {
			return sortListPerMinute;
		}


		public void setCurrent(AtomicLong current) {
			this.current = current;
		}

		public void setLastErrorPerSecond(AtomicLong lastErrorPerSecond) {
			this.lastErrorPerSecond = lastErrorPerSecond;
		}

		public void setLastSuccessPerSecond(AtomicLong lastSuccessPerSecond) {
			this.lastSuccessPerSecond = lastSuccessPerSecond;
		}

		public SortList getSortList() {
			return sortList;
		}

		public void setSortList(SortList sortList) {
			this.sortList = sortList;
		}

		public double getLastMinTimeSpan() {
			return lastMinTimeSpan;
		}

		public void setLastMinTimeSpan(double lastMinTimeSpan) {
			this.lastMinTimeSpan = lastMinTimeSpan;
		}

		public SortList getSortListPerMinute() {
			return sortListPerMinute;
		}

		public void setSortListPerMinute(SortList sortListPerMinute) {
			this.sortListPerMinute = sortListPerMinute;
		}

		public double getLastMinTimeSpanPerMinute() {
			return lastMinTimeSpanPerMinute;
		}

		public void setLastMinTimeSpanPerMinute(double lastMinTimeSpanPerMinute) {
			this.lastMinTimeSpanPerMinute = lastMinTimeSpanPerMinute;
		}

		public Integer getMaxLength() {
			return maxLength;
		}

		public void setMaxLength(Integer maxLength) {
			this.maxLength = maxLength;
		}

		public Long getLastSecond() {
			return lastSecond;
		}

		public void setLastSecond(Long lastSecond) {
			this.lastSecond = lastSecond;
		}

		public Long getLastMinute() {
			return lastMinute;
		}

		public void setLastMinute(Long lastMinute) {
			this.lastMinute = lastMinute;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public CoreProperties getCoreProperties() {
			return coreProperties;
		}

		public void setCoreProperties(CoreProperties coreProperties) {
			this.coreProperties = coreProperties;
		}
	}

	/**
	 * Collector
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:35:15
	 */
	public static class Call {

		/**
		 * func
		 */
		private Callable.Func0<Object> func;

		/**
		 * 设置回调
		 */
		public void set(Callable.Func0<Object> func) {
			this.func = func;
		}

		/**
		 * 返回结果
		 */
		public Object run() {
			return this.func.invoke();
		}

		public Func0<Object> getFunc() {
			return func;
		}

		public void setFunc(Func0<Object> func) {
			this.func = func;
		}
	}

	/**
	 * SortInfo
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:35:26
	 */
	public static class SortInfo implements Comparable<SortInfo> {

		/**
		 * tag
		 */
		protected Object tag;
		/**
		 * time
		 */
		protected double time;
		/**
		 * maxTime
		 */
		protected double maxTime;
		/**
		 * count
		 */
		protected volatile AtomicLong count;

		public SortInfo() {
		}

		public SortInfo(Object tag, double time, double maxTime,
			AtomicLong count) {
			this.tag = tag;
			this.time = time;
			this.maxTime = maxTime;
			this.count = count;
		}

		@Override
		public int compareTo(SortInfo o) {
			if (o.time > this.time) {
				return 1;
			} else if (o.time < this.time) {
				return -1;
			} else {
				return 0;
			}

		}

		@Override
		public String toString() {
			return tag.toString() + ":" + this.time;
		}

		@Override
		public int hashCode() {
			return tag.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj.hashCode() == this.hashCode();
		}

		public Object getTag() {
			return tag;
		}

		public void setTag(Object tag) {
			this.tag = tag;
		}

		public double getTime() {
			return time;
		}

		public void setTime(double time) {
			this.time = time;
		}

		public double getMaxTime() {
			return maxTime;
		}

		public void setMaxTime(double maxTime) {
			this.maxTime = maxTime;
		}

		public AtomicLong getCount() {
			return count;
		}

		public void setCount(AtomicLong count) {
			this.count = count;
		}
	}

	/**
	 * SortList
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:35:51
	 */
	public static class SortList extends ConcurrentSkipListSet<SortInfo> {

		/**
		 * tagCache
		 */
		protected Map<Integer, Object> tagCache = new ConcurrentHashMap<>();

		@Override
		public boolean add(SortInfo sortInfo) {
			Integer hash = sortInfo.tag.hashCode();
			if (tagCache.containsKey(hash)) {
				Object value = tagCache.get(hash);
				if (value != null) {
					//累加
					SortInfo sort = ((SortInfo) value);
					sort.getCount().getAndIncrement();
					if (sort.time < sortInfo.time) {
						sort.maxTime = sortInfo.time;
					}
				}
				return false;
			}

			if (tagCache.size() > super.size()) {
				LogUtil.info("tag cache 缓存存在溢出风险");
			}

			if (super.add(sortInfo)) {
				tagCache.put(hash, sortInfo);
			}
			return true;
		}

		@Override
		public boolean remove(Object o) {
			Integer hash = ((SortInfo) o).tag.hashCode();
			tagCache.remove(hash);
			return super.remove(o);
		}

		/**
		 * getLast
		 *
		 * @return {@link com.taotao.cloud.core.model.Collector.SortInfo }
		 * @author shuigedeng
		 * @since 2021-09-02 20:36:02
		 */
		public SortInfo getLast() {
			try {
				if (!this.isEmpty()) {
					return this.last();
				}
			} catch (NoSuchElementException e) {
			}
			return null;
		}

		/**
		 * removeMore
		 *
		 * @param maxLength maxLength
		 * @author shuigedeng
		 * @since 2021-09-02 20:36:05
		 */
		public void removeMore(int maxLength) {
			int count = this.size();
			while (this.size() > maxLength) {
				count--;
				SortInfo last = this.pollLast();
				if (last != null) {
					this.remove(last);
				}
				if (count < -10) {
					LogUtil.error("[严重bug] remove more,item:" + (last != null ? last.toString()
						: " 长时间无法移除导致死循环"));
					break;
				}
			}
		}

		/**
		 * toText
		 *
		 * @return {@link java.lang.String }
		 * @author shuigedeng
		 * @since 2021-09-02 20:36:10
		 */
		public String toText() {
			StringBuilder sb = new StringBuilder();
			for (SortInfo sortInfo : this) {
				sb.append(String.format(" [耗时ms]%s [tag]%s [次数]%s [最大耗时ms]%s\r\n",
					NumberUtil.scale(sortInfo.time, 2),
					sortInfo.tag.toString(),
					sortInfo.count,
					NumberUtil.scale(sortInfo.maxTime, 2)));
			}
			return sb.toString();
		}
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public Object getLock() {
		return lock;
	}
}
