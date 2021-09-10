package com.taotao.cloud.health.collect;


import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;

/**
 * 系统连接池采集任务
 *
 * @author: chejiangyi
 * @version: 2019-07-30 21:14
 **/
public class MonitorThreadPoolCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;
	private Collector collector;

	public MonitorThreadPoolCollectTask(Collector collector, CollectTaskProperties properties) {
		this.collector = collector;
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getThreadPollTimeSpan();
	}

	@Override
	public String getDesc() {
		return "监控线程池采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.threadpool.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isThreadPollEnabled();
	}

	@Override
	protected Object getData() {
		//if (ContextUtil.getBean(
		//	ReflectionUtil.classForName("com.taotao.cloud.core.monitor.MonitorSystem"), false)
		//	== null) {
		//	return null;
		//}

		MonitorThreadPoolInfo info = new MonitorThreadPoolInfo();
		info.setSystemActiveCount(
			(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.active.count").run());
		info.setSystemCorePoolSize(
			(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.core.poolSize").run());
		info.setSystemPoolSizeLargest(
			(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.poolSize.largest")
				.run());
		info.setSystemPoolSizeMax(
			(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.poolSize.max").run());
		info.setSystemPoolSizeCount(
			(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.poolSize.count").run());
		info.setSystemQueueSize(
			(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.queue.size").run());
		info.setSystemTaskCount(
			(Long) this.collector.call("taotao.cloud.core.monitor.threadpool.task.count").run());
		info.setSystemTaskCompleted(
			(Long) this.collector.call("taotao.cloud.core.monitor.threadpool.task.completed").run());

		Hook hook = this.collector.hook("taotao.cloud.core.monitor.threadpool.hook");
		info.setSystemTaskHookCurrent(hook.getCurrent());
		info.setSystemTaskHookError(hook.getLastErrorPerSecond());
		info.setSystemTaskHookSuccess(hook.getLastSuccessPerSecond());
		info.setSystemTaskHookList(hook.getMaxTimeSpanList().toText());
		info.setSystemTaskHookListPerMinute(hook.getMaxTimeSpanListPerMinute().toText());
		return info;
	}


	private static class MonitorThreadPoolInfo {
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.active.count", desc = "系统线程池活动线程数")
		private Integer systemActiveCount;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.core.poolSize", desc = "系统线程池核心线程数")
		private Integer systemCorePoolSize;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.poolSize.largest", desc = "线程池历史最大线程数")
		private Integer systemPoolSizeLargest;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.poolSize.max", desc = "线程池最大线程数")
		private Integer systemPoolSizeMax;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.poolSize.count", desc = "线程池当前线程数")
		private Integer systemPoolSizeCount;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.queue.size", desc = "线程池当前排队等待任务数")
		private Integer systemQueueSize;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.count", desc = "线程池历史任务数")
		private Long systemTaskCount;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.completed", desc = "线程池已完成任务数")
		private Long systemTaskCompleted;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.error", desc = "线程池拦截上一次每秒出错次数")
		private Long systemTaskHookError;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.success", desc = "线程池拦截上一次每秒成功次数")
		private Long systemTaskHookSuccess;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.current", desc = "线程池拦截当前执行任务数")
		private Long systemTaskHookCurrent;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.list.detail", desc = "线程池拦截历史最大耗时任务列表")
		private String systemTaskHookList;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.list.minute.detail", desc = "线程池拦截历史最大耗时任务列表(每分钟)")
		private String systemTaskHookListPerMinute;

		public MonitorThreadPoolInfo() {
		}

		public MonitorThreadPoolInfo(Integer systemActiveCount, Integer systemCorePoolSize,
			Integer systemPoolSizeLargest, Integer systemPoolSizeMax,
			Integer systemPoolSizeCount, Integer systemQueueSize, Long systemTaskCount,
			Long systemTaskCompleted, Long systemTaskHookError, Long systemTaskHookSuccess,
			Long systemTaskHookCurrent, String systemTaskHookList,
			String systemTaskHookListPerMinute) {
			this.systemActiveCount = systemActiveCount;
			this.systemCorePoolSize = systemCorePoolSize;
			this.systemPoolSizeLargest = systemPoolSizeLargest;
			this.systemPoolSizeMax = systemPoolSizeMax;
			this.systemPoolSizeCount = systemPoolSizeCount;
			this.systemQueueSize = systemQueueSize;
			this.systemTaskCount = systemTaskCount;
			this.systemTaskCompleted = systemTaskCompleted;
			this.systemTaskHookError = systemTaskHookError;
			this.systemTaskHookSuccess = systemTaskHookSuccess;
			this.systemTaskHookCurrent = systemTaskHookCurrent;
			this.systemTaskHookList = systemTaskHookList;
			this.systemTaskHookListPerMinute = systemTaskHookListPerMinute;
		}

		public Integer getSystemActiveCount() {
			return systemActiveCount;
		}

		public void setSystemActiveCount(Integer systemActiveCount) {
			this.systemActiveCount = systemActiveCount;
		}

		public Integer getSystemCorePoolSize() {
			return systemCorePoolSize;
		}

		public void setSystemCorePoolSize(Integer systemCorePoolSize) {
			this.systemCorePoolSize = systemCorePoolSize;
		}

		public Integer getSystemPoolSizeLargest() {
			return systemPoolSizeLargest;
		}

		public void setSystemPoolSizeLargest(Integer systemPoolSizeLargest) {
			this.systemPoolSizeLargest = systemPoolSizeLargest;
		}

		public Integer getSystemPoolSizeMax() {
			return systemPoolSizeMax;
		}

		public void setSystemPoolSizeMax(Integer systemPoolSizeMax) {
			this.systemPoolSizeMax = systemPoolSizeMax;
		}

		public Integer getSystemPoolSizeCount() {
			return systemPoolSizeCount;
		}

		public void setSystemPoolSizeCount(Integer systemPoolSizeCount) {
			this.systemPoolSizeCount = systemPoolSizeCount;
		}

		public Integer getSystemQueueSize() {
			return systemQueueSize;
		}

		public void setSystemQueueSize(Integer systemQueueSize) {
			this.systemQueueSize = systemQueueSize;
		}

		public Long getSystemTaskCount() {
			return systemTaskCount;
		}

		public void setSystemTaskCount(Long systemTaskCount) {
			this.systemTaskCount = systemTaskCount;
		}

		public Long getSystemTaskCompleted() {
			return systemTaskCompleted;
		}

		public void setSystemTaskCompleted(Long systemTaskCompleted) {
			this.systemTaskCompleted = systemTaskCompleted;
		}

		public Long getSystemTaskHookError() {
			return systemTaskHookError;
		}

		public void setSystemTaskHookError(Long systemTaskHookError) {
			this.systemTaskHookError = systemTaskHookError;
		}

		public Long getSystemTaskHookSuccess() {
			return systemTaskHookSuccess;
		}

		public void setSystemTaskHookSuccess(Long systemTaskHookSuccess) {
			this.systemTaskHookSuccess = systemTaskHookSuccess;
		}

		public Long getSystemTaskHookCurrent() {
			return systemTaskHookCurrent;
		}

		public void setSystemTaskHookCurrent(Long systemTaskHookCurrent) {
			this.systemTaskHookCurrent = systemTaskHookCurrent;
		}

		public String getSystemTaskHookList() {
			return systemTaskHookList;
		}

		public void setSystemTaskHookList(String systemTaskHookList) {
			this.systemTaskHookList = systemTaskHookList;
		}

		public String getSystemTaskHookListPerMinute() {
			return systemTaskHookListPerMinute;
		}

		public void setSystemTaskHookListPerMinute(String systemTaskHookListPerMinute) {
			this.systemTaskHookListPerMinute = systemTaskHookListPerMinute;
		}
	}
}
