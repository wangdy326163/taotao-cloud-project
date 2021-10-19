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
package com.taotao.cloud.uc.biz.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.uc.api.bo.role.RoleBO;
import com.taotao.cloud.uc.api.service.ISysRoleResourceService;
import com.taotao.cloud.uc.api.service.ISysRoleService;
import com.taotao.cloud.uc.biz.entity.QSysRole;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.entity.SysRoleResource;
import com.taotao.cloud.uc.biz.mapper.SysRoleMapper;
import com.taotao.cloud.uc.biz.mapstruct.RoleMapper;
import com.taotao.cloud.uc.biz.repository.ISysRoleRepository;
import com.taotao.cloud.uc.biz.repository.impl.SysRoleRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SysRoleServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:46:26
 */
@Service
public class SysRoleServiceImpl extends
	BaseSuperServiceImpl<SysRoleMapper, SysRole, SysRoleRepository, ISysRoleRepository, Long>
	implements ISysRoleService<SysRole, Long> {

	private final static QSysRole SYS_ROLE = QSysRole.sysRole;

	private final ISysRoleResourceService<SysRoleResource, Long> sysRoleResourceService;

	public SysRoleServiceImpl(
		ISysRoleResourceService<SysRoleResource, Long> sysRoleResourceService) {
		this.sysRoleResourceService = sysRoleResourceService;
	}

	@Override
	public Boolean existRoleByCode(String code) {
		BooleanExpression predicate = SYS_ROLE.code.eq(code);
		return cr().exists(predicate);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveRoleResources(Long roleId, Set<Long> resourceIds) {
		return sysRoleResourceService.saveRoleResource(roleId, resourceIds);
	}

	@Override
	public List<RoleBO> findAllRoles() {
		List<SysRole> roles = ir().findAll();
		return RoleMapper.INSTANCE.rolesToBos(roles);
	}

	@Override
	public List<RoleBO> findRoleByUserIds(Set<Long> userIds) {
		List<SysRole> roles = cr().findRoleByUserIds(userIds);
		return RoleMapper.INSTANCE.rolesToBos(roles);
	}

	@Override
	public List<RoleBO> findRoleByCodes(Set<String> codes) {
		List<SysRole> roles = cr().findRoleByCodes(codes);
		return RoleMapper.INSTANCE.rolesToBos(roles);
	}

}
