package com.taotao.cloud.goods.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.GoodsOperationDTO;
import com.taotao.cloud.goods.api.dto.GoodsSearchParams;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.vo.GoodsBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.GoodsGallery;
import com.taotao.cloud.goods.biz.mapper.GoodsMapper;
import com.taotao.cloud.goods.biz.service.CategoryService;
import com.taotao.cloud.goods.biz.service.GoodsGalleryService;
import com.taotao.cloud.goods.biz.service.GoodsService;
import com.taotao.cloud.goods.biz.service.GoodsSkuService;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品业务层实现
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

	/**
	 * 分类
	 */
	@Autowired
	private CategoryService categoryService;
	///**
	// * 设置
	// */
	//@Autowired
	//private IFeignSettingService settingService;
	/**
	 * 商品相册
	 */
	@Autowired
	private GoodsGalleryService goodsGalleryService;
	/**
	 * 商品规格
	 */
	@Autowired
	private GoodsSkuService goodsSkuService;
	/**
	 * 店铺详情
	 */
	//@Autowired
	//private StoreService storeService;
	/**
	 * 会员评价
	 */
	//@Autowired
	//private MemberEvaluationService memberEvaluationService;
	///**
	// * rocketMq
	// */
	//@Autowired
	//private RocketMQTemplate rocketMQTemplate;
	/**
	 * rocketMq配置
	 */
	//@Autowired
	//private RocketmqCustomProperties rocketmqCustomProperties;
	//@Autowired
	//private FreightTemplateService freightTemplateService;

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public List<Goods> getByBrandIds(List<String> brandIds) {
		LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.in(Goods::getBrandId, brandIds);
		return list(lambdaQueryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean underStoreGoods(String storeId) {
		//获取商品ID列表
		List<String> list = this.baseMapper.getGoodsIdByStoreId(storeId);
		//下架店铺下的商品
		updateGoodsMarketAble(list, GoodsStatusEnum.DOWN, "店铺关闭");
		return true;
	}

	/**
	 * 更新商品参数
	 *
	 * @param goodsId 商品id
	 * @param params  商品参数
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateGoodsParams(String goodsId, String params) {
		LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(Goods::getId, goodsId);
		updateWrapper.set(Goods::getParams, params);
		this.update(updateWrapper);

		return true;
	}

	@Override
	public final Long getGoodsCountByCategory(String categoryId) {
		QueryWrapper<Goods> queryWrapper = Wrappers.query();
		queryWrapper.like("category_path", categoryId);
		queryWrapper.eq("delete_flag", false);
		return this.count(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addGoods(GoodsOperationDTO goodsOperationDTO) {
		//Goods goods = new Goods(goodsOperationDTO);
		////检查商品
		//this.checkGoods(goods);
		////向goods加入图片
		//this.setGoodsGalleryParam(goodsOperationDTO.getGoodsGalleryList().get(0), goods);
		////添加商品参数
		//if (goodsOperationDTO.getGoodsParamsDTOList() != null
		//	&& !goodsOperationDTO.getGoodsParamsDTOList().isEmpty()) {
		//	//给商品参数填充值
		//	goods.setParams(JSONUtil.toJsonStr(goodsOperationDTO.getGoodsParamsDTOList()));
		//}
		////添加商品
		//this.save(goods);
		////添加商品sku信息
		//this.goodsSkuService.add(goodsOperationDTO.getSkuList(), goods);
		////添加相册
		//if (goodsOperationDTO.getGoodsGalleryList() != null
		//	&& !goodsOperationDTO.getGoodsGalleryList().isEmpty()) {
		//	this.goodsGalleryService.add(goodsOperationDTO.getGoodsGalleryList(), goods.getId());
		//}
		return true;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean editGoods(GoodsOperationDTO goodsOperationDTO, String goodsId) {
		//Goods goods = new Goods(goodsOperationDTO);
		//goods.setId(goodsId);
		////检查商品信息
		//this.checkGoods(goods);
		////向goods加入图片
		//this.setGoodsGalleryParam(goodsOperationDTO.getGoodsGalleryList().get(0), goods);
		////添加商品参数
		//if (goodsOperationDTO.getGoodsParamsDTOList() != null
		//	&& !goodsOperationDTO.getGoodsParamsDTOList().isEmpty()) {
		//	goods.setParams(JSONUtil.toJsonStr(goodsOperationDTO.getGoodsParamsDTOList()));
		//}
		////修改商品
		//this.updateById(goods);
		////修改商品sku信息
		//this.goodsSkuService.update(goodsOperationDTO.getSkuList(), goods,
		//	goodsOperationDTO.getRegeneratorSkuFlag());
		////添加相册
		//if (goodsOperationDTO.getGoodsGalleryList() != null
		//	&& !goodsOperationDTO.getGoodsGalleryList().isEmpty()) {
		//	this.goodsGalleryService.add(goodsOperationDTO.getGoodsGalleryList(), goods.getId());
		//}
		//if (GoodsAuthEnum.TOBEAUDITED.name().equals(goods.getAuthFlag())) {
		//	this.deleteEsGoods(Collections.singletonList(goodsId));
		//}
		//cache.remove(CachePrefix.GOODS.getPrefix() + goodsId);
		return true;
	}

	@Override
	public GoodsVO getGoodsVO(String goodsId) {
		////缓存获取，如果没有则读取缓存
		//GoodsVO goodsVO = cache.get(CachePrefix.GOODS.getPrefix() + goodsId);
		//if (goodsVO != null) {
		//	return goodsVO;
		//}
		//
		////查询商品信息
		//Goods goods = this.getById(goodsId);
		//if (goods == null) {
		//	log.error("商品ID为" + goodsId + "的商品不存在");
		//	throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
		//}
		//goodsVO = new GoodsVO();
		////赋值
		//BeanUtils.copyProperties(goods, goodsVO);
		////商品id
		//goodsVO.setId(goods.getId());
		////商品相册赋值
		//List<String> images = new ArrayList<>();
		//List<GoodsGallery> galleryList = goodsGalleryService.goodsGalleryList(goodsId);
		//for (GoodsGallery goodsGallery : galleryList) {
		//	images.add(goodsGallery.getOriginal());
		//}
		//goodsVO.setGoodsGalleryList(images);
		////商品sku赋值
		//List<GoodsSkuVO> goodsListByGoodsId = goodsSkuService.getGoodsListByGoodsId(goodsId);
		//if (goodsListByGoodsId != null && !goodsListByGoodsId.isEmpty()) {
		//	goodsVO.setSkuList(goodsListByGoodsId);
		//}
		////商品分类名称赋值
		//List<String> categoryName = new ArrayList<>();
		//String categoryPath = goods.getCategoryPath();
		//String[] strArray = categoryPath.split(",");
		//List<Category> categories = categoryService.listByIds(Arrays.asList(strArray));
		//for (Category category : categories) {
		//	categoryName.add(category.getName());
		//}
		//goodsVO.setCategoryName(categoryName);
		//
		////参数非空则填写参数
		//if (CharSequenceUtil.isNotEmpty(goods.getParams())) {
		//	goodsVO.setGoodsParamsDTOList(JSONUtil.toList(goods.getParams(), GoodsParamsDTO.class));
		//}
		//cache.put(CachePrefix.GOODS.getPrefix() + goodsId, goodsVO);
		//return goodsVO;
		return null;
	}

	@Override
	public PageModel<GoodsBaseVO> queryByParams(GoodsSearchParams goodsSearchParams) {
		IPage<Goods> goodsPage = this.page(goodsSearchParams.buildMpPage(),
			goodsSearchParams.queryWrapper());
		return PageModel.convertMybatisPage(goodsPage, GoodsBaseVO.class);
	}

	/**
	 * 商品查询
	 *
	 * @param goodsSearchParams 查询参数
	 * @return 商品信息
	 */
	@Override
	public List<Goods> queryListByParams(GoodsSearchParams goodsSearchParams) {
		return this.list(goodsSearchParams.queryWrapper());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean auditGoods(List<String> goodsIds, GoodsAuthEnum goodsAuthEnum) {
		boolean result = false;
		for (String goodsId : goodsIds) {
			//Goods goods = this.checkExist(goodsId);
			//goods.setAuthFlag(goodsAuthEnum.name());
			//result = this.updateById(goods);
			//goodsSkuService.updateGoodsSkuStatus(goods);
			////删除之前的缓存
			//cache.remove(CachePrefix.GOODS.getPrefix() + goodsId);
			////商品审核消息
			//String destination =
			//	rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.GOODS_AUDIT.name();
			////发送mq消息
			//rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(goods),
			//	RocketmqSendCallbackBuilder.commonCallback());
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateGoodsMarketAble(List<String> goodsIds, GoodsStatusEnum goodsStatusEnum,
		String underReason) {
		boolean result;

		//如果商品为空，直接返回
		if (goodsIds == null || goodsIds.isEmpty()) {
			return true;
		}

		LambdaUpdateWrapper<Goods> updateWrapper = this.getUpdateWrapperByStoreAuthority();
		updateWrapper.set(Goods::getMarketEnable, goodsStatusEnum.name());
		updateWrapper.set(Goods::getUnderMessage, underReason);
		updateWrapper.in(Goods::getId, goodsIds);
		result = this.update(updateWrapper);

		//修改规格商品
		LambdaQueryWrapper<Goods> queryWrapper = this.getQueryWrapperByStoreAuthority();
		queryWrapper.in(Goods::getId, goodsIds);
		List<Goods> goodsList = this.list(queryWrapper);
		for (Goods goods : goodsList) {
			goodsSkuService.updateGoodsSkuStatus(goods);
		}

		if (GoodsStatusEnum.DOWN.equals(goodsStatusEnum)) {
			this.deleteEsGoods(goodsIds);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean managerUpdateGoodsMarketAble(List<String> goodsIds,
		GoodsStatusEnum goodsStatusEnum, String underReason) {
		boolean result;

		//如果商品为空，直接返回
		if (goodsIds == null || goodsIds.isEmpty()) {
			return true;
		}

		//检测管理员权限
		this.checkManagerAuthority();

		LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.set(Goods::getMarketEnable, goodsStatusEnum.name());
		updateWrapper.set(Goods::getUnderMessage, underReason);
		updateWrapper.in(Goods::getId, goodsIds);
		result = this.update(updateWrapper);

		//修改规格商品
		LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(Goods::getId, goodsIds);
		List<Goods> goodsList = this.list(queryWrapper);
		for (Goods goods : goodsList) {
			goodsSkuService.updateGoodsSkuStatus(goods);
		}
		if (GoodsStatusEnum.DOWN.equals(goodsStatusEnum)) {
			this.deleteEsGoods(goodsIds);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteGoods(List<String> goodsIds) {
		LambdaUpdateWrapper<Goods> updateWrapper = this.getUpdateWrapperByStoreAuthority();
		updateWrapper.set(Goods::getMarketEnable, GoodsStatusEnum.DOWN.name());
		updateWrapper.set(Goods::getDelFlag, true);
		updateWrapper.in(Goods::getId, goodsIds);
		this.update(updateWrapper);

		//修改规格商品
		LambdaQueryWrapper<Goods> queryWrapper = this.getQueryWrapperByStoreAuthority();
		queryWrapper.in(Goods::getId, goodsIds);
		List<Goods> goodsList = this.list(queryWrapper);
		for (Goods goods : goodsList) {
			//修改SKU状态
			goodsSkuService.updateGoodsSkuStatus(goods);
		}

		this.deleteEsGoods(goodsIds);

		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean freight(List<String> goodsIds, String templateId) {
		SecurityUser authUser = this.checkStoreAuthority();

		//FreightTemplate freightTemplate = freightTemplateService.getById(templateId);
		//if (freightTemplate == null) {
		//	throw new BusinessException(ResultEnum.FREIGHT_TEMPLATE_NOT_EXIST);
		//}
		//if (authUser != null && !freightTemplate.getStoreId().equals(authUser.getStoreId())) {
		//	throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		//}
		LambdaUpdateWrapper<Goods> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
		//lambdaUpdateWrapper.set(Goods::getTemplateId, templateId);
		//lambdaUpdateWrapper.in(Goods::getId, goodsIds);
		return this.update(lambdaUpdateWrapper);
	}

	@Override
	public Boolean updateStock(String goodsId, Integer quantity) {
		LambdaUpdateWrapper<Goods> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
		lambdaUpdateWrapper.set(Goods::getQuantity, quantity);
		lambdaUpdateWrapper.eq(Goods::getId, goodsId);
		this.update(lambdaUpdateWrapper);
		return true;
	}

	@Override
	public Boolean updateGoodsCommentNum(String goodsId) {

		//获取商品信息
		Goods goods = this.getById(goodsId);
		////修改商品评价数量
		//goods.setCommentNum(goods.getCommentNum() + 1);
		//
		////修改商品好评率
		//LambdaQueryWrapper<MemberEvaluation> goodEvaluationQueryWrapper = new LambdaQueryWrapper<>();
		//goodEvaluationQueryWrapper.eq(MemberEvaluation::getId, goodsId);
		//goodEvaluationQueryWrapper.eq(MemberEvaluation::getGrade, EvaluationGradeEnum.GOOD.name());
		////好评数量
		//long highPraiseNum = memberEvaluationService.count(goodEvaluationQueryWrapper);
		////好评率
		//double grade = NumberUtil.mul(
		//	NumberUtil.div(highPraiseNum, goods.getCommentNum().doubleValue(), 2), 100);
		//goods.setGrade(grade);
		this.updateById(goods);
		return true;
	}

	/**
	 * 更新商品的购买数量
	 *
	 * @param goodsId  商品ID
	 * @param buyCount 购买数量
	 */
	@Override
	public Boolean updateGoodsBuyCount(String goodsId, int buyCount) {
		this.update(new LambdaUpdateWrapper<Goods>()
			.eq(Goods::getId, goodsId)
			.set(Goods::getBuyCount, buyCount));
		return true;
	}

	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean updateStoreDetail(Store store) {
	//	UpdateWrapper updateWrapper = new UpdateWrapper<>()
	//		.eq("store_id", store.getId())
	//		.set("store_name", store.getStoreName())
	//		.set("self_operated", store.getSelfOperated());
	//	this.update(updateWrapper);
	//	goodsSkuService.update(updateWrapper);
	//	return true;
	//}

	@Override
	public Long countStoreGoodsNum(String storeId) {
		return this.count(
			new LambdaQueryWrapper<Goods>()
				.eq(Goods::getStoreId, storeId)
				//.eq(Goods::getAuthFlag, GoodsAuthEnum.PASS.name())
				.eq(Goods::getMarketEnable, GoodsStatusEnum.UPPER.name()));
	}


	/**
	 * 发送删除es索引的信息
	 *
	 * @param goodsIds 商品id
	 */
	private void deleteEsGoods(List<String> goodsIds) {
		////商品删除消息
		//String destination =
		//	rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.GOODS_DELETE.name();
		////发送mq消息
		//rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(goodsIds),
		//	RocketmqSendCallbackBuilder.commonCallback());
	}

	/**
	 * 添加商品默认图片
	 *
	 * @param origin 图片
	 * @param goods  商品
	 */
	private void setGoodsGalleryParam(String origin, Goods goods) {
		GoodsGallery goodsGallery = goodsGalleryService.getGoodsGallery(origin);
		goods.setOriginal(goodsGallery.getOriginal());
		goods.setSmall(goodsGallery.getSmall());
		goods.setThumbnail(goodsGallery.getThumbnail());
	}

	/**
	 * 检查商品信息 如果商品是虚拟商品则无需配置配送模板 如果商品是实物商品需要配置配送模板 判断商品是否存在 判断商品是否需要审核 判断当前用户是否为店铺
	 *
	 * @param goods 商品
	 */
	private void checkGoods(Goods goods) {
		////判断商品类型
		//switch (goods.getGoodsType()) {
		//	case "PHYSICAL_GOODS":
		//		if ("0".equals(goods.getTemplateId())) {
		//			throw new BusinessException(ResultEnum.PHYSICAL_GOODS_NEED_TEMP);
		//		}
		//		break;
		//	case "VIRTUAL_GOODS":
		//		if (!"0".equals(goods.getTemplateId())) {
		//			throw new BusinessException(ResultEnum.VIRTUAL_GOODS_NOT_NEED_TEMP);
		//		}
		//		break;
		//	default:
		//		throw new BusinessException(ResultEnum.GOODS_TYPE_ERROR);
		//}
		////检查商品是否存在--修改商品时使用
		//if (goods.getId() != null) {
		//	this.checkExist(goods.getId());
		//} else {
		//	//评论次数
		//	goods.setCommentNum(0);
		//	//购买次数
		//	goods.setBuyCount(0);
		//	//购买次数
		//	goods.setQuantity(0);
		//	//商品评分
		//	goods.setGrade(100.0);
		//}
		//
		////获取商品系统配置决定是否审核
		//Setting setting = settingService.get(SettingEnum.GOODS_SETTING.name());
		//GoodsSetting goodsSetting = JSONUtil.toBean(setting.getSettingValue(), GoodsSetting.class);
		////是否需要审核
		//goods.setAuthFlag(
		//	Boolean.TRUE.equals(goodsSetting.getGoodsCheck()) ? GoodsAuthEnum.TOBEAUDITED.name()
		//		: GoodsAuthEnum.PASS.name());
		////判断当前用户是否为店铺
		//if (Objects.requireNonNull(UserContext.getCurrentUser()).getRole()
		//	.equals(UserEnums.STORE)) {
		//	StoreVO storeDetail = this.storeService.getStoreDetail();
		//	if (storeDetail.getSelfOperated() != null) {
		//		goods.setSelfOperated(storeDetail.getSelfOperated());
		//	}
		//	goods.setStoreId(storeDetail.getId());
		//	goods.setStoreName(storeDetail.getStoreName());
		//	goods.setSelfOperated(storeDetail.getSelfOperated());
		//} else {
		//	throw new BusinessException(ResultEnum.STORE_NOT_LOGIN_ERROR);
		//}
	}

	/**
	 * 判断商品是否存在
	 *
	 * @param goodsId 商品id
	 * @return 商品信息
	 */
	private Goods checkExist(String goodsId) {
		Goods goods = getById(goodsId);
		if (goods == null) {
			log.error("商品ID为" + goodsId + "的商品不存在");
			throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
		}
		return goods;
	}


	/**
	 * 获取UpdateWrapper（检查用户越权）
	 *
	 * @return updateWrapper
	 */
	private LambdaUpdateWrapper<Goods> getUpdateWrapperByStoreAuthority() {
		LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
		SecurityUser authUser = this.checkStoreAuthority();
		if (authUser != null) {
			updateWrapper.eq(Goods::getStoreId, authUser.getStoreId());
		}
		return updateWrapper;
	}


	/**
	 * 检查当前登录的店铺
	 *
	 * @return 当前登录的店铺
	 */
	private SecurityUser checkStoreAuthority() {
		SecurityUser currentUser = SecurityUtil.getUser();
		//如果当前会员不为空，且为店铺角色
		//if (currentUser != null && (currentUser.getRole().equals(UserEnums.STORE) && currentUser.getStoreId() != null)) {
		if (currentUser != null) {
			return currentUser;
		}
		return null;
	}

	/**
	 * 检查当前登录的店铺
	 *
	 * @return 当前登录的店铺
	 */
	private SecurityUser checkManagerAuthority() {
		SecurityUser currentUser = SecurityUtil.getUser();
		//如果当前会员不为空，且为店铺角色
		//if (currentUser != null && (currentUser.getRoles().contains(UserEnums.MANAGER))) {
		if (currentUser != null) {
			return currentUser;
		} else {
			throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		}
	}

	/**
	 * 获取QueryWrapper（检查用户越权）
	 *
	 * @return queryWrapper
	 */
	private LambdaQueryWrapper<Goods> getQueryWrapperByStoreAuthority() {
		LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
		//AuthUser authUser = this.checkStoreAuthority();
		//if (authUser != null) {
		//	queryWrapper.eq(Goods::getStoreId, authUser.getStoreId());
		//}
		return queryWrapper;
	}

}
