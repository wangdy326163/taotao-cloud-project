package com.taotao.cloud.promotion.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.query.CouponSearchParams;
import com.taotao.cloud.promotion.api.vo.CouponVO;
import com.taotao.cloud.promotion.biz.entity.Coupon;

/**
 * 优惠券业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:27
 */
public interface CouponService extends AbstractPromotionsService<Coupon> {

	/**
	 * 领取优惠券
	 *
	 * @param couponId   优惠券id
	 * @param receiveNum 领取数量
	 * @since 2022-04-27 16:43:27
	 */
	void receiveCoupon(String couponId, Integer receiveNum);

	/**
	 * 使用优惠券
	 *
	 * @param couponId 优惠券id
	 * @param usedNum  使用数量
	 * @since 2022-04-27 16:43:27
	 */
	void usedCoupon(String couponId, Integer usedNum);

	/**
	 * 获取优惠券展示实体
	 *
	 * @param searchParams 查询参数
	 * @param page         分页参数
	 * @return {@link IPage }<{@link CouponVO }>
	 * @since 2022-04-27 16:43:27
	 */
	IPage<CouponVO> pageVOFindAll(CouponSearchParams searchParams, PageVO page);

	/**
	 * 获取优惠券展示详情
	 *
	 * @param couponId 优惠券id
	 * @return {@link CouponVO }
	 * @since 2022-04-27 16:43:27
	 */
	CouponVO getDetail(String couponId);

}
