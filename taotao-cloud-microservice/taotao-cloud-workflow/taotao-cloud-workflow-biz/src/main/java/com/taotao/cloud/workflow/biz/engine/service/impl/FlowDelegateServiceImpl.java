package com.taotao.cloud.workflow.biz.engine.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.workflow.api.common.base.Pagination;
import com.taotao.cloud.workflow.api.common.util.DateUtil;
import com.taotao.cloud.workflow.api.common.util.RandomUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.mapper.FlowDelegateMapper;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 流程委托
 */
@Service
public class FlowDelegateServiceImpl extends
	ServiceImpl<FlowDelegateMapper, FlowDelegateEntity> implements FlowDelegateService {

	@Override
	public List<FlowDelegateEntity> getList(Pagination pagination) {
		// 定义变量判断是否需要使用修改时间倒序
		boolean flag = false;
		QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(FlowDelegateEntity::getCreatorUserId, SecurityUtils.getUserId());
		if (!StrUtils.isEmpty(pagination.getKeyword())) {
			flag = true;
			queryWrapper.lambda().and(
				t -> t.like(FlowDelegateEntity::getFlowName, pagination.getKeyword())
					.or().like(FlowDelegateEntity::getToUserName, pagination.getKeyword())
			);
		}
		//排序
		queryWrapper.lambda().orderByAsc(FlowDelegateEntity::getFSortCode)
			.orderByDesc(FlowDelegateEntity::getCreatorTime);
		if (flag) {
			queryWrapper.lambda().orderByDesc(FlowDelegateEntity::getLastModifyTime);
		}
		Page page = new Page(pagination.getCurrentPage(), pagination.getPageSize());
		IPage<FlowDelegateEntity> flowDelegateEntityPage = this.page(page, queryWrapper);
		return pagination.setData(flowDelegateEntityPage.getRecords(), page.getTotal());
	}


	@Override
	public List<FlowDelegateEntity> getList() {
		QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(FlowDelegateEntity::getCreatorUserId, SecurityUtils.getUserId());
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public FlowDelegateEntity getInfo(String id) {
		QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(FlowDelegateEntity::getId, id);
		return this.getOne(queryWrapper);
	}

	@Override
	public void delete(FlowDelegateEntity entity) {
		this.removeById(entity.getId());
	}

	@Override
	public void create(FlowDelegateEntity entity) {
		entity.setId(RandomUtil.uuId());
		entity.setFSortCode(RandomUtil.parses());
		entity.setCreatorUserId(SecurityUtils.getUserId());
		this.save(entity);
	}

	@Override
	public List<FlowDelegateEntity> getUser(String userId) {
		return getUser(userId, null, null);
	}

	@Override
	public List<FlowDelegateEntity> getUser(String userId, String flowId, String creatorUserId) {
		Date thisTime = DateUtil.getNowDate();
		QueryWrapper<FlowDelegateEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().le(FlowDelegateEntity::getStartTime, thisTime)
			.ge(FlowDelegateEntity::getEndTime, thisTime);
		if (StrUtil.isNotEmpty(userId)) {
			queryWrapper.lambda().eq(FlowDelegateEntity::getFTouserid, userId);
		}
		if (StrUtil.isNotEmpty(flowId)) {
			queryWrapper.lambda().eq(FlowDelegateEntity::getFlowId, flowId);
		}
		if (StrUtil.isNotEmpty(creatorUserId)) {
			queryWrapper.lambda().eq(FlowDelegateEntity::getCreatorUserId, creatorUserId);
		}
		return this.list(queryWrapper);
	}

	@Override
	public boolean update(String id, FlowDelegateEntity entity) {
		entity.setId(id);
		entity.setLastModifyTime(new Date());
		entity.setLastModifyUserId(SecurityUtils.getUserId());
		return this.updateById(entity);
	}
}
