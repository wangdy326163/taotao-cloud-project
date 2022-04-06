package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单投诉查询参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单投诉查询参数")
public class OrderComplaintSearchParams {

	/**
	 * @see ComplaintStatusEnum
	 */
	@Schema(description = "交易投诉状态")
	private String status;

	@Schema(description = "订单号")
	private String orderSn;

	@Schema(description = "会员id")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商家id")
	private String storeId;

	@Schema(description = "商家名称")
	private String storeName;

	public LambdaQueryWrapper<OrderComplaint> lambdaQueryWrapper() {
		LambdaQueryWrapper<OrderComplaint> queryWrapper = new LambdaQueryWrapper<>();
		if (StrUtil.isNotEmpty(status)) {
			queryWrapper.eq(OrderComplaint::getComplainStatus, status);
		}
		if (StrUtil.isNotEmpty(orderSn)) {
			queryWrapper.eq(OrderComplaint::getOrderSn, orderSn);
		}
		if (StrUtil.isNotEmpty(storeName)) {
			queryWrapper.like(OrderComplaint::getStoreName, storeName);
		}
		if (StrUtil.isNotEmpty(storeId)) {
			queryWrapper.eq(OrderComplaint::getStoreId, storeId);
		}
		if (StrUtil.isNotEmpty(memberName)) {
			queryWrapper.like(OrderComplaint::getMemberName, memberName);
		}
		if (StrUtil.isNotEmpty(memberId)) {
			queryWrapper.eq(OrderComplaint::getMemberId, memberId);
		}
		queryWrapper.eq(OrderComplaint::getDeleteFlag, false);
		queryWrapper.orderByDesc(OrderComplaint::getCreateTime);
		return queryWrapper;
	}


}
