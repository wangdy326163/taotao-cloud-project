package com.taotao.cloud.order.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.api.dto.aftersale.AfterSaleDTO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleApplyVO;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleSearchParams;
import com.taotao.cloud.order.api.vo.aftersale.AfterSaleVOVO123;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleLog;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleLogService;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleReasonService;
import com.taotao.cloud.order.biz.service.aftersale.AfterSaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,售后管理接口
 */
@Validated
@RestController
@Tag(name = "买家端-售后API", description = "买家端-售后API")
@RequestMapping("/order/buyer/afterSale")
public class AfterSaleController {

	/**
	 * 售后
	 */
	@Autowired
	private AfterSaleService afterSaleService;
	/**
	 * 售后原因
	 */
	@Autowired
	private AfterSaleReasonService afterSaleReasonService;
	/**
	 * 售后日志
	 */
	@Autowired
	private AfterSaleLogService afterSaleLogService;

	@Operation(summary = "查看售后服务详情", description = "查看售后服务详情", method = CommonConstant.GET)
	@RequestLogger(description = "查看售后服务详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{sn}")
	public Result<AfterSaleVOVO123> get(
		@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
		AfterSaleVOVO123 afterSale = OperationalJudgment.judgment(afterSaleService.getAfterSale(sn));
		return Result.success(afterSale);
	}

	@Operation(summary = "分页获取售后服务", description = "分页获取售后服务", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取售后服务")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<AfterSaleVOVO123>> getByPage(AfterSaleSearchParams searchParams) {
		return Result.success(afterSaleService.getAfterSalePages(searchParams));
	}

	@Operation(summary = "获取申请售后页面信息", description = "获取申请售后页面信息", method = CommonConstant.GET)
	@RequestLogger(description = "获取申请售后页面信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/applyAfterSaleInfo/{sn}")
	public Result<AfterSaleApplyVO> applyAfterSaleInfo(@PathVariable String sn) {
		return Result.success(afterSaleService.getAfterSaleVO(sn));
	}

	@Operation(summary = "申请售后", description = "申请售后", method = CommonConstant.POST)
	@RequestLogger(description = "申请售后")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/save/{orderItemSn}")
	public Result<AfterSale> save(AfterSaleDTO afterSaleDTO) {
		return Result.success(afterSaleService.saveAfterSale(afterSaleDTO));

	}

	@Operation(summary = "买家 退回 物流信息", description = "买家 退回 物流信息", method = CommonConstant.POST)
	@RequestLogger(description = "买家 退回 物流信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/delivery/{afterSaleSn}")
	public Result<AfterSale> delivery(
		@NotNull(message = "售后编号不能为空") @PathVariable("afterSaleSn") String afterSaleSn,
		@NotNull(message = "发货单号不能为空") @RequestParam String logisticsNo,
		@NotNull(message = "请选择物流公司") @RequestParam String logisticsId,
		@NotNull(message = "请选择发货时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date mDeliverTime) {
		return Result.success(
			afterSaleService.buyerDelivery(afterSaleSn, logisticsNo, logisticsId, mDeliverTime));
	}

	@Operation(summary = "售后，取消售后", description = "售后，取消售后", method = CommonConstant.POST)
	@RequestLogger(description = "售后，取消售后")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/cancel/{afterSaleSn}")
	public Result<AfterSale> cancel(
		@NotNull(message = "参数非法") @PathVariable("afterSaleSn") String afterSaleSn) {
		return Result.success(afterSaleService.cancel(afterSaleSn));
	}

	@Operation(summary = "获取商家售后收件地址", description = "获取商家售后收件地址", method = CommonConstant.GET)
	@RequestLogger(description = "获取商家售后收件地址")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getStoreAfterSaleAddress/{sn}")
	public Result<StoreAfterSaleAddressDTO> getStoreAfterSaleAddress(
		@NotNull(message = "售后单号") @PathVariable("sn") String sn) {
		return Result.success(afterSaleService.getStoreAfterSaleAddressDTO(sn));
	}

	@Operation(summary = "获取售后原因", description = "获取售后原因", method = CommonConstant.GET)
	@RequestLogger(description = "获取售后原因")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/afterSaleReason/{serviceType}")
	public Result<List<AfterSaleReason>> getAfterSaleReason(
		@PathVariable String serviceType) {
		return Result.success(afterSaleReasonService.afterSaleReasonList(serviceType));
	}

	@Operation(summary = "获取售后日志", description = "获取售后日志", method = CommonConstant.GET)
	@RequestLogger(description = "获取售后日志")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/get/getAfterSaleLog/{sn}")
	public Result<List<AfterSaleLog>> getAfterSaleLog(@PathVariable String sn) {
		return Result.success(afterSaleLogService.getAfterSaleLog(sn));
	}

}
