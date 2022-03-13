package com.taotao.cloud.order.api.enums.cart;

/**
 * 购物车类型
 */
public enum CartTypeEnum {

	/**
	 * 购物车
	 */
	CART,
	/**
	 * 立即购买
	 */
	BUY_NOW,
	/**
	 * 虚拟商品
	 */
	VIRTUAL,
	/**
	 * 拼团
	 */
	PINTUAN,
	/**
	 * 积分
	 */
	POINTS,
	/**
	 * 砍价商品
	 */
	KANJIA;

	public String getPrefix() {
		return "{" + this.name() + "}_";
	}

}
