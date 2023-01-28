package com.taotao.cloud.media.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.media.api.feign.IFeignMediaService;
import com.taotao.cloud.media.api.model.vo.FileVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignMediaFallback implements FallbackFactory<IFeignMediaService> {

	@Override
	public IFeignMediaService create(Throwable throwable) {
		return new IFeignMediaService() {
			@Override
			public Result<FileVO> findFileById(Long id) {
				LogUtils.error("调用findFileById异常：{0}", throwable, id);
				return Result.fail("调用findFileById异常", 500);
			}
		};
	}
}
