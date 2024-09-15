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

package com.taotao.cloud.auth.biz.strategy.local;

import com.taotao.cloud.auth.biz.strategy.AbstractStrategyPermissionDetailsService;
import com.taotao.cloud.auth.biz.strategy.user.SysPermission;
import com.taotao.boot.security.spring.core.domain.TtcPermission;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

/**
 * <p>本地权限服务 </p>
 *
 */
public class LocalPermissionDetailsService extends AbstractStrategyPermissionDetailsService {

    private final SysPermissionService sysPermissionService;

    public LocalPermissionDetailsService(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    @Override
    public List<TtcPermission> findAll() {
        List<SysPermission> authorities = sysPermissionService.findAll();

        if (CollectionUtils.isNotEmpty(authorities)) {
            return toEntities(authorities);
        }

        return new ArrayList<>();
    }
}
