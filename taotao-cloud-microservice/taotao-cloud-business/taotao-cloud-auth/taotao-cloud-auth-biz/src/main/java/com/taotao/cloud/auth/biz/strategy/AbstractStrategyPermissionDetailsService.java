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

package com.taotao.cloud.auth.biz.strategy;

import com.taotao.cloud.auth.biz.strategy.user.SysPermission;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusPermission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 抽象的StrategyAuthorityDetailsService </p>
 *
 * @date : 2022/4/1 19:09
 */
public abstract class AbstractStrategyPermissionDetailsService implements StrategyPermissionDetailsService {

	protected List<HerodotusPermission> toEntities(List<SysPermission> permissions) {
		return permissions.stream().map(this::toEntity).collect(Collectors.toList());
	}

	protected HerodotusPermission toEntity(SysPermission object) {
		HerodotusPermission herodotusPermission = new HerodotusPermission();
		herodotusPermission.setPermissionId(object.getPermissionId());
		herodotusPermission.setPermissionCode(object.getPermissionCode());
		herodotusPermission.setPermissionName(object.getPermissionName());
		return herodotusPermission;
	}
}
