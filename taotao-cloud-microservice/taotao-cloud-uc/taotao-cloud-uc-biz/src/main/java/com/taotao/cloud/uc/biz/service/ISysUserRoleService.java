package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.uc.biz.entity.SysUserRole;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;
import java.util.Set;

/**
 * 用户-角色服务类
 *
 * @since 2020/4/30 13:20
 */
public interface ISysUserRoleService extends
	BaseSuperService<SysUserRole, Long> {

	/**
	 * 添加用户-角色对应关系
	 *
	 * @param userId
	 * @param roleIds
	 * @return java.lang.Boolean
	 * @version 1.0.0
	 * @since 2020/10/21 09:20
	 */
	Boolean saveUserRoles(Long userId, Set<Long> roleIds);
}
