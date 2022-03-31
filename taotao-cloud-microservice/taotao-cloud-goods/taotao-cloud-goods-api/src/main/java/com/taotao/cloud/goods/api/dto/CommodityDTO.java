package com.taotao.cloud.goods.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 直播商品DTO 用于获取直播商品状态时使用
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityDTO {

	/**
	 * 商品ID
	 */
	private Integer goods_id;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 地址
	 */
	private String url;
	/**
	 * 审核状态
	 */
	private Integer audit_status;
}
