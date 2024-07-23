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

package com.taotao.cloud.goods.application.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.taotao.cloud.goods.application.command.goods.dto.GoodsAddCmd;
import com.taotao.cloud.goods.application.command.goods.dto.GoodsPageQry;
import com.taotao.cloud.goods.application.command.goods.dto.GoodsSkuSearchQry;
import com.taotao.cloud.goods.application.command.goods.dto.GoodsSkuStockUpdateCmd;
import com.taotao.cloud.goods.application.command.goods.dto.clientobject.GoodsSkuCO;
import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsPO;
import com.taotao.cloud.goods.infrastructure.persistent.po.GoodsSkuPO;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Map;

/**
 * 商品sku业务层
 *
 * @author shuigedeng
 * @version 2023.07
 * @see BaseSuperService
 * @since 2023-08-18 16:00:58
 */
public interface IGoodsSkuService extends BaseSuperService<GoodsSkuPO, Long> {
	/**
	 * 获取商品SKU缓存ID
	 *
	 * @param id SkuId
	 * @return {@link String }
	 * @since 2023-08-18 16:00:58
	 */
	default String getCacheKeys(Long id) {
		return CachePrefix.GOODS_SKU.getPrefix() + id;
	}

	/**
	 * 获取商品SKU库存缓存ID
	 *
	 * @param id SkuId
	 * @return {@link String }
	 * @since 2023-08-18 16:00:58
	 */
	default String getStockCacheKey(Long id) {
		return CachePrefix.SKU_STOCK.getPrefix() + id;
	}

	/**
	 * 添加商品sku
	 *
	 * @param goods             商品信息
	 * @param goodsAddCmd 商品操作信息
	 * @since 2023-08-18 16:00:59
	 */
	boolean add(GoodsPO goods, GoodsAddCmd goodsAddCmd);

	/**
	 * 更新商品sku
	 *
	 * @param goods             商品信息
	 * @param goodsAddCmd 商品操作信息
	 * @since 2023-08-18 16:00:59
	 */
	boolean update(GoodsPO goods, GoodsAddCmd goodsAddCmd);

	/**
	 * 更新商品sku
	 *
	 * @param goodsSkuPO sku信息
	 * @since 2023-08-18 16:00:59
	 */
	boolean update(GoodsSkuPO goodsSkuPO);

	/**
	 * 清除sku缓存
	 *
	 * @param skuId skuid
	 * @since 2023-08-18 16:00:59
	 */
	boolean clearCache(Long skuId);

	/**
	 * 从redis缓存中获取商品SKU信息
	 *
	 * @param id SkuId
	 * @return {@link GoodsSkuPO }
	 * @since 2023-08-18 16:00:59
	 */
	GoodsSkuPO getGoodsSkuByIdFromCache(Long skuId);

	/**
	 * 从缓存中获取可参与促销商品
	 *
	 * @param skuId skuid
	 * @return {@link GoodsSkuPO }
	 * @since 2023-08-18 16:00:59
	 */
	GoodsSkuPO getCanPromotionGoodsSkuByIdFromCache(Long skuId);

	/**
	 * 获取商品sku详情
	 *
	 * @param goodsId 商品ID
	 * @param skuId   skuID
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2023-08-18 16:00:59
	 */
	Map<String, Object> getGoodsSkuDetail(Long goodsId, Long skuId);

	/**
	 * 批量从redis中获取商品SKU信息
	 *
	 * @param ids SkuId集合
	 * @return {@link List }<{@link GoodsSkuPO }>
	 * @since 2023-08-18 16:00:59
	 */
	List<GoodsSkuPO> getGoodsSkuByIdFromCache(List<Long> ids);

	/**
	 * 获取goodsId下所有的goodsSku
	 *
	 * @param goodsId 商品id
	 * @return {@link List }<{@link GoodsSkuCO }>
	 * @since 2023-08-18 16:00:59
	 */
	List<GoodsSkuCO> getGoodsListByGoodsId(Long goodsId);

	/**
	 * 获取goodsId下所有的goodsSku
	 *
	 * @param goodsId 商品id
	 * @return {@link List }<{@link GoodsSkuPO }>
	 * @since 2023-08-18 16:00:59
	 */
	List<GoodsSkuPO> getGoodsSkuListByGoodsId(Long goodsId);

	/**
	 * 根据goodsSku组装goodsSkuCO
	 *
	 * @param list 商品id
	 * @return {@link List }<{@link GoodsSkuCO }>
	 * @since 2023-08-18 16:00:59
	 */
	List<GoodsSkuCO> getGoodsSkuVOList(List<GoodsSkuPO> list);

	/**
	 * 根据goodsSku组装goodsSkuCO
	 *
	 * @param goodsSkuPO 商品规格
	 * @return {@link GoodsSkuCO }
	 * @since 2023-08-18 16:00:59
	 */
	GoodsSkuCO getGoodsSkuVO(GoodsSkuPO goodsSkuPO);

	/**
	 * 分页查询商品sku信息
	 *
	 * @param searchParams 查询参数
	 * @return {@link IPage }<{@link GoodsSkuPO }>
	 * @since 2023-08-18 16:00:59
	 */
	IPage<GoodsSkuPO> getGoodsSkuByPage(GoodsPageQry searchParams);


	/**
	 * 分页查询商品sku信息
	 *
	 * @param page         分页参数
	 * @param queryWrapper 查询参数
	 * @return {@link IPage }<{@link GoodsSkuSearchQry }>
	 * @since 2023-08-18 16:00:59
	 */
	IPage<GoodsSkuSearchQry> getGoodsSkuDTOByPage(Page<GoodsSkuSearchQry> page, Wrapper<GoodsSkuSearchQry> queryWrapper);

	/**
	 * 列表查询商品sku信息
	 *
	 * @param searchParams 查询参数
	 * @return {@link List }<{@link GoodsSkuPO }>
	 * @since 2023-08-18 16:00:59
	 */
	List<GoodsSkuPO> getGoodsSkuByList(GoodsPageQry searchParams);

	/**
	 * 更新商品sku状态
	 *
	 * @param goods 商品信息(Id,MarketEnable/AuthFlag)
	 * @since 2023-08-18 16:00:59
	 */
	boolean updateGoodsSkuStatus(GoodsPO goods);

	/**
	 * 更新商品sku状态根据店铺id
	 *
	 * @param storeId      店铺id
	 * @param marketEnable 市场启用状态
	 * @param authFlag     审核状态
	 * @since 2023-08-18 16:00:59
	 */
	boolean updateGoodsSkuStatusByStoreId(Long storeId, String marketEnable, String authFlag);

	/**
	 * 更新SKU库存
	 *
	 * @param goodsSkuStockUpdateCmds sku库存修改实体
	 * @since 2023-08-18 16:00:59
	 */
	boolean updateStocks(List<GoodsSkuStockUpdateCmd> goodsSkuStockUpdateCmds);

	/**
	 * 更新SKU库存
	 *
	 * @param skuId    SKUId
	 * @param quantity 设置的库存数量
	 * @since 2023-08-18 16:00:59
	 */
	boolean updateStock(Long skuId, Integer quantity);

	/**
	 * 获取商品sku库存
	 *
	 * @param skuId 商品skuId
	 * @return {@link Integer }
	 * @since 2023-08-18 16:00:59
	 */
	Integer getStock(Long skuId);

	/**
	 * 修改商品库存字段
	 *
	 * @param goodsSkusPOS
	 */
	boolean updateGoodsStuck(List<GoodsSkuPO> goodsSkusPOS);

	/**
	 * 更新SKU评价数量
	 *
	 * @param skuId SKUId
	 */
	boolean updateGoodsSkuCommentNum(Long skuId);

	/**
	 * 根据商品id获取全部skuId的集合
	 *
	 * @param goodsId goodsId
	 * @return 全部skuId的集合
	 */
	List<String> getSkuIdsByGoodsId(Long goodsId);

	/**
	 * 删除并且新增sku，即覆盖之前信息
	 *
	 * @param goodsSkusPOS 商品sku集合
	 */
	boolean deleteAndInsertGoodsSkus(List<GoodsSkuPO> goodsSkusPOS);

	/**
	 * 统计sku总数
	 *
	 * @param storeId 店铺id
	 * @return sku总数
	 */
	Long countSkuNum(Long storeId);

	/**
	 * 批量渲染商品sku
	 *
	 * @param goodsSkuPOList SKU基础数据列表
	 * @param goodsAddCmd 商品操作信息
	 */
	void renderGoodsSkuList(List<GoodsSkuPO> goodsSkuPOList, GoodsAddCmd goodsAddCmd);
}
