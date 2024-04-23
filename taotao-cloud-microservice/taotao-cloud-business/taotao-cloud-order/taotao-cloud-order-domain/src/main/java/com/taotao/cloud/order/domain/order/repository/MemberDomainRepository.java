package com.taotao.cloud.order.domain.order.repository;

import com.taotao.cloud.member.domain.member.entity.MemberEntity;

public interface MemberDomainRepository {
	/**
	 * 新增部门.
	 *
	 * @param dept 部门对象
	 */
	void create(MemberEntity dept);

	/**
	 * 修改部门.
	 *
	 * @param dept 部门对象
	 */
	void modify(MemberEntity dept);

	/**
	 * 根据ID删除部门.
	 *
	 * @param ids IDS
	 */
	void remove(Long[] ids);
}
