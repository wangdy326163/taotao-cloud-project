package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserFallbackImpl;
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台菜单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignResourceService", value = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignUserFallbackImpl.class)
public interface IFeignMenuService {
    /**
     * 根据角色code列表获取角色列表
     *
     * @param codes
     * @return com.taotao.cloud.core.model.Result<java.util.List < com.taotao.cloud.sys.api.vo.resource.ResourceVO>>
     * @author shuigedeng
     * @since 2020/10/21 15:24
     * @version 1.0.0
     */
    @GetMapping("/resource/info/codes")
    Result<List<MenuQueryVO>> findResourceByCodes(@RequestParam(value = "codes") Set<String> codes);

}
