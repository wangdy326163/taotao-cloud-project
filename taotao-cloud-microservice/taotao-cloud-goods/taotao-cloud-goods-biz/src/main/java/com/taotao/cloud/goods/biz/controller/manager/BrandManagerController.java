package com.taotao.cloud.goods.biz.controller.manager;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.goods.api.dto.BrandDTO;
import com.taotao.cloud.goods.api.dto.BrandPageDTO;
import com.taotao.cloud.goods.api.vo.BrandVO;
import com.taotao.cloud.goods.biz.entity.Brand;
import com.taotao.cloud.goods.biz.mapstruct.BrandMapStruct;
import com.taotao.cloud.goods.biz.service.BrandService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,品牌接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-品牌管理API", description = "平台管理端-品牌管理API")
@RequestMapping("/goods/manager/brand")
public class BrandManagerController {

	/**
	 * 品牌
	 */
	private final BrandService brandService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger(description = "通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<BrandVO> getById(@NotBlank(message = "id不能为空") @PathVariable String id) {
		Brand brand = brandService.getById(id);
		return Result.success(BrandMapStruct.INSTANCE.brandToBrandVO(brand));
	}

	@Operation(summary = "获取所有可用品牌", description = "获取所有可用品牌", method = CommonConstant.GET)
	@RequestLogger(description = "获取所有可用品牌")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/all/available")
	public Result<List<BrandVO>> getAllAvailable() {
		List<Brand> list = brandService.getAllAvailable();
		return Result.success(BrandMapStruct.INSTANCE.brandsToBrandVOs(list));
	}

	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageModel<BrandVO>> page(@Validated BrandPageDTO page) {
		IPage<Brand> brandPage = brandService.getBrandsByPage(page);
		return Result.success(PageModel.convertMybatisPage(brandPage, BrandVO.class));
	}

	@Operation(summary = "新增品牌", description = "新增品牌", method = CommonConstant.POST)
	@RequestLogger(description = "新增品牌")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Validated @RequestBody BrandDTO brand) {
		return Result.success(brandService.addBrand(brand));
	}

	@Operation(summary = "更新品牌", description = "更新品牌", method = CommonConstant.PUT)
	@RequestLogger(description = "更新品牌")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@PathVariable String id, @Validated BrandDTO brand) {
		brand.setId(id);
		return Result.success(brandService.updateBrand(brand));
	}

	@Operation(summary = "后台禁用品牌", description = "后台禁用品牌", method = CommonConstant.PUT)
	@RequestLogger(description = "后台禁用品牌")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/disable/{brandId}")
	public Result<Object> disable(@PathVariable String brandId,
		@RequestParam Boolean disable) {
			return Result.success(brandService.brandDisable(brandId, disable));
	}

	@Operation(summary = "批量删除", description = "批量删除", method = CommonConstant.DELETE)
	@RequestLogger(description = "批量删除")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{ids}")
	public Result<Object> delAllByIds(@PathVariable List<String> ids) {
		brandService.deleteBrands(ids);
		return Result.success(ResultEnum.SUCCESS);
	}

}
