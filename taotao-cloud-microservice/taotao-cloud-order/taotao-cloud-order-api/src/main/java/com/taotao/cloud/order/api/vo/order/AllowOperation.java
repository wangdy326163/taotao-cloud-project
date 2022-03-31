package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.text.CharSequenceUtil;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单可进行的操作
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单可进行的操作")
public class AllowOperation implements Serializable {

	private static final long serialVersionUID = -5109440403955543227L;

	@Schema(description = "可以取消")
	private Boolean cancel = false;

	@Schema(description = "可以支付")
	private Boolean pay = false;

	@Schema(description = "可以发货")
	private Boolean ship = false;

	@Schema(description = "可以收货")
	private Boolean rog = false;

	@Schema(description = "是否允许查看物流信息")
	private Boolean showLogistics = false;

	@Schema(description = "是否允许更改收货人信息")
	private Boolean editConsignee = false;

	@Schema(description = "是否允许更改价格")
	private Boolean editPrice = false;

	@Schema(description = "是否可以进行核销")
	private Boolean take = false;


	/**
	 * 根据各种状态构建对象
	 *
	 * @param order
	 */
	public AllowOperation(Order order) {

		//获取订单类型
		String status = order.getOrderStatus();
		String payStatus = order.getPayStatus();
		//编辑订单价格 未付款并且是新订单
		if (payStatus.equals(PayStatusEnum.UNPAID.name()) && status.equals(
			OrderStatusEnum.UNPAID.name())) {
			this.editPrice = true;
		}

		//新订单
		if (CharSequenceUtil.equalsAny(status, OrderStatusEnum.UNPAID.name(),
			OrderStatusEnum.PAID.name(), OrderStatusEnum.UNDELIVERED.name())) {
			this.cancel = true;
		}
		//新订单，允许支付
		this.pay = status.equals(OrderStatusEnum.UNPAID.name()) && payStatus.equals(
			PayStatusEnum.UNPAID.name());

		//可编辑订单收件人信息=实物订单 && 订单未发货 && 订单未取消
		this.editConsignee = order.getOrderType().equals(OrderTypeEnum.NORMAL.name())
			&& order.getDeliverStatus().equals(DeliverStatusEnum.UNDELIVERED.name())
			&& !status.equals(OrderStatusEnum.CANCELLED.name());

		//是否允许被发货
		this.ship = editConsignee && status.equals(OrderStatusEnum.UNDELIVERED.name());

		//是否允许被收货
		this.rog = status.equals(OrderStatusEnum.DELIVERED.name());

		//是否允许查看物流信息
		this.showLogistics =
			order.getDeliverStatus().equals(DeliverStatusEnum.DELIVERED.name()) && status.equals(
				OrderStatusEnum.DELIVERED.name());

		this.take =
			order.getOrderType().equals(OrderTypeEnum.VIRTUAL.name()) && order.getOrderStatus()
				.equals(OrderStatusEnum.TAKE.name());
	}

	/**
	 * 根据各种状态构建对象
	 *
	 * @param order
	 */
	public AllowOperation(OrderSimpleVO order) {

		//获取订单类型
		String status = order.getOrderStatus();
		String payStatus = order.getPayStatus();
		//编辑订单价格 未付款并且是新订单
		if (payStatus.equals(PayStatusEnum.UNPAID.name()) && status.equals(
			OrderStatusEnum.UNPAID.name())) {
			this.editPrice = true;
		}

		//新订单
		if (CharSequenceUtil.equalsAny(status, OrderStatusEnum.UNPAID.name(),
			OrderStatusEnum.PAID.name(), OrderStatusEnum.UNDELIVERED.name())) {
			this.cancel = true;

		}
		//新订单，允许支付
		this.pay = status.equals(OrderStatusEnum.UNPAID.name());

		//订单未发货，就可以编辑收货人信息
		this.editConsignee = order.getDeliverStatus().equals(DeliverStatusEnum.UNDELIVERED.name());

		//是否允许被发货
		this.ship = editConsignee && status.equals(OrderStatusEnum.UNDELIVERED.name());

		//是否允许被收货
		this.rog = status.equals(OrderStatusEnum.DELIVERED.name());

		//是否允许查看物流信息
		this.showLogistics =
			order.getDeliverStatus().equals(DeliverStatusEnum.DELIVERED.name()) && status.equals(
				OrderStatusEnum.DELIVERED.name());

		this.take =
			order.getOrderType().equals(OrderTypeEnum.VIRTUAL.name()) && order.getOrderStatus()
				.equals(OrderStatusEnum.TAKE.name());
	}


}
