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
package com.taotao.cloud.member.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.vo.MemberAddressVO;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import com.taotao.cloud.member.biz.service.MemberAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 买家端-会员地址API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:54:53
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-会员地址API", description = "买家端-会员地址API")
@RequestMapping("/member/buyer/member/address")
public class MemberAddressController {

	private final MemberAddressService memberAddressService;

	@Operation(summary = "分页获取当前会员收件地址列表", description = "分页获取当前会员收件地址列表")
	@RequestLogger("分页获取当前会员收件地址列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<PageModel<MemberAddressVO>> page(@Validated PageParam page) {
		return Result.success(
			memberAddressService.getAddressByMember(page, SecurityUtil.getUserId()));
	}

	@Operation(summary = "根据ID获取会员收件地址", description = "根据ID获取会员收件地址")
	@RequestLogger("根据ID获取会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{id}")
	public Result<MemberAddressVO> getShippingAddress(
		@Parameter(description = "会员地址ID", required = true) @NotNull(message = "id不能为空")
		@PathVariable(value = "id") Long id) {
		MemberAddress memberAddress = memberAddressService.getMemberAddress(id);
		return Result.success(BeanUtil.copyProperties(memberAddress, MemberAddressVO.class));
	}

	@Operation(summary = "获取当前会员默认收件地址", description = "获取当前会员默认收件地址")
	@RequestLogger("获取当前会员默认收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/current/default")
	public Result<MemberAddressVO> getDefaultShippingAddress() {
		MemberAddress memberAddress = memberAddressService.getDefaultMemberAddress();
		return Result.success(BeanUtil.copyProperties(memberAddress, MemberAddressVO.class));
	}

	@Operation(summary = "新增会员收件地址", description = "新增会员收件地址")
	@RequestLogger("新增会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> addShippingAddress(@Valid MemberAddress shippingAddress) {
		//添加会员地址
		shippingAddress.setMemberId(Objects.requireNonNull(SecurityUtil.getUserId()));
		if (shippingAddress.getDefaulted() == null) {
			shippingAddress.setDefaulted(false);
		}
		return Result.success(memberAddressService.saveMemberAddress(shippingAddress));
	}

	@Operation(summary = "修改会员收件地址", description = "修改会员收件地址")
	@RequestLogger("修改会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> editShippingAddress(@Valid MemberAddress shippingAddress) {
		return Result.success(memberAddressService.updateMemberAddress(shippingAddress));
	}

	@Operation(summary = "删除会员收件地址", description = "删除会员收件地址")
	@RequestLogger("删除会员收件地址")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delShippingAddressById(
		@Parameter(description = "会员地址ID", required = true) @NotNull(message = "id不能为空")
		@PathVariable(value = "id") Long id) {
		return Result.success(memberAddressService.removeMemberAddress(id));
	}

}
