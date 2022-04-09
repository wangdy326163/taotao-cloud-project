/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.sys.api.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 角色查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:23:58
 */
@Data
@Builder
@Schema( description = "角色查询对象")
public class RoleQueryVO implements Serializable {

	@Serial
	static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;
	/**
	 * 角色名称
	 */
	@Schema(description = "角色名称")
	private String name;
	/**
	 * 角色code
	 */
	@Schema(description = "角色code")
	private String code;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
