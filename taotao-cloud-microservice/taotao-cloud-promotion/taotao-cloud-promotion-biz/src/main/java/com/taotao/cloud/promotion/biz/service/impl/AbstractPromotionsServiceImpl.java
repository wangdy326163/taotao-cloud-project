package com.taotao.cloud.promotion.biz.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.query.BasePromotionsSearchParams;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import com.taotao.cloud.promotion.biz.entity.BasePromotions;
import com.taotao.cloud.promotion.biz.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.AbstractPromotionsService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 摘要促销活动服务实现类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:46
 */
public class AbstractPromotionsServiceImpl<M extends BaseMapper<T>, T extends BasePromotions<T, Long>> extends ServiceImpl<M, T> implements
	AbstractPromotionsService<T> {

	/**
	 * 推广产品服务
	 * 促销商品
	 */
	@Autowired
    private PromotionGoodsService promotionGoodsService;

	/**
	 * rocketmq自定义属性
	 * rocketMq配置
	 */
	@Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

	/**
	 * 火箭mqtemplate
	 * rocketMq
	 */
	@Autowired
    private RocketMQTemplate rocketMQTemplate;

	/**
	 * 通用促销保存
	 * 调用顺序:
	 * 1. initPromotion 初始化促销信息
	 * 2. checkPromotions 检查促销参数
	 * 3. save 保存促销信息
	 * 4. updatePromotionGoods 更新促销商品信息
	 * 5。 updateEsGoodsIndex 更新商品索引促销信息
	 */
	@Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean savePromotions(T promotions) {
        this.initPromotion(promotions);
        this.checkPromotions(promotions);
        boolean save = this.save(promotions);
        this.updatePromotionsGoods(promotions);
        this.updateEsGoodsIndex(promotions);
        return save;
    }

	/**
	 * 通用促销更新
	 * 调用顺序:
	 * 1. checkStatus 检查促销状态
	 * 2. checkPromotions 检查促销参数
	 * 3. saveOrUpdate 保存促销信息
	 * 4. updatePromotionGoods 更新促销商品信息
	 * 5. updateEsGoodsIndex 更新商品索引促销信息
	 */
	@Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updatePromotions(T promotions) {
        this.checkStatus(promotions);
        this.checkPromotions(promotions);
        boolean save = this.saveOrUpdate(promotions);
        this.updatePromotionsGoods(promotions);
        this.updateEsGoodsIndex(promotions);
        return save;
    }

	/**
	 * 更新促销状态
	 * 如果要更新促销状态为关闭，startTime和endTime置为空即可
	 */
	@Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean updateStatus(List<String> ids, Long startTime, Long endTime) {
        List<T> promotionsList = this.list(new QueryWrapper<T>().in("id", ids));
        for (T t : promotionsList) {
            if (startTime != null && endTime != null) {
                t.setStartTime(new Date(startTime));
                t.setEndTime(new Date(endTime));
            } else {
                t.setStartTime(null);
                t.setEndTime(null);
            }
            this.checkStatus(t);
            this.updatePromotionsGoods(t);
            this.updateEsGoodsIndex(t);
        }
        if (startTime != null && endTime != null) {
            return this.update(new UpdateWrapper<T>().in("id", ids).set("start_time", new Date(startTime)).set("end_time", new Date(endTime)));
        } else {
            return this.update(new UpdateWrapper<T>().in("id", ids).set("start_time", null).set("end_time", null));
        }
    }

	@Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removePromotions(List<String> ids) {
        for (String id : ids) {
            T promotions = this.getById(id);
            this.checkStatus(promotions);
            promotions.setDeleteFlag(true);
            this.updatePromotionsGoods(promotions);
            this.updateEsGoodsIndex(promotions);
        }
        return this.removeByIds(ids);
    }

	@Override
    public <S extends BasePromotionsSearchParams> IPage<T> pageFindAll(S searchParams, PageVO page) {
        page.setNotConvert(false);
        return this.page(PageUtil.initPage(page), searchParams.queryWrapper());
    }

	@Override
    public <S extends BasePromotionsSearchParams> List<T> listFindAll(S searchParams) {
        return this.list(searchParams.queryWrapper());
    }

	@Override
    public void initPromotion(T promotions) {
        if (CharSequenceUtil.isEmpty(promotions.getScopeType())) {
            promotions.setScopeType(PromotionsScopeTypeEnum.PORTION_GOODS.name());
        }
    }

	@Override
    public void checkPromotions(T promotions) {
        PromotionTools.checkPromotionTime(promotions.getStartTime(), promotions.getEndTime());
    }

	@Override
    public void checkStatus(T promotions) {
        T byId = this.getById(promotions.getId());
        if (byId == null) {
            throw new BusinessException(ResultEnum.PROMOTION_ACTIVITY_ERROR);
        }
    }

	@Override
    @Transactional(rollbackFor = {Exception.class})
    public void updatePromotionsGoods(T promotions) {
        if (promotions.getStartTime() == null && promotions.getEndTime() == null) {
            this.promotionGoodsService.deletePromotionGoods(Collections.singletonList(promotions.getId()));
            return;
        }
        if (PromotionsScopeTypeEnum.ALL.name().equals(promotions.getScopeType())) {
            PromotionGoods promotionGoods = new PromotionGoods();
            promotionGoods.setScopeType(promotions.getScopeType());
            promotionGoods.setPromotionId(promotions.getId());
            promotionGoods.setStoreId(promotions.getStoreId());
            promotionGoods.setStoreName(promotions.getStoreName());
            promotionGoods.setStartTime(promotions.getStartTime());
            promotionGoods.setEndTime(promotions.getEndTime());
            promotionGoods.setPromotionType(this.getPromotionType().name());
            promotionGoods.setTitle(promotions.getPromotionName());
            this.promotionGoodsService.deletePromotionGoods(Collections.singletonList(promotions.getId()));
            this.promotionGoodsService.save(promotionGoods);
        }
    }

	@Override
    public void updateEsGoodsIndex(T promotions) {
        if (promotions.getStartTime() == null && promotions.getEndTime() == null) {
            //删除商品促销消息
            String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.DELETE_GOODS_INDEX_PROMOTIONS.name();
            //发送mq消息
            rocketMQTemplate.asyncSend(destination, promotions.getId(), RocketmqSendCallbackBuilder.commonCallback());
        } else {

            String esPromotionKey = this.getPromotionType().name() + "-" + promotions.getId();
            Map<String, Object> map = new HashMap<>();
            // es促销key
            map.put("esPromotionKey", esPromotionKey);
            // 促销类型全路径名
            map.put("promotionsType", promotions.getClass().getName());
            // 促销实体
            map.put("promotions", promotions);
            //更新商品促销消息
            String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX_PROMOTIONS.name();
            //发送mq消息
            rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(map), RocketmqSendCallbackBuilder.commonCallback());
        }
    }

	@Override
    public PromotionTypeEnum getPromotionType() {
        return null;
    }
}
