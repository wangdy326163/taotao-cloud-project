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

package com.taotao.cloud.goods.application.command.store.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺分类CO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:52:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreGoodsLabelInfoCO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "店铺商品分类ID")
    private Long id;

    @Schema(description = "店铺商品分类名称")
    private String labelName;

    @Schema(description = "层级, 从0开始")
    private Integer level;

    @Schema(description = "店铺商品分类排序")
    private Integer sortOrder;

    @Schema(description = "父id, 根节点为0")
    private Long parentId;

    @Schema(description = "店铺ID")
    private Long storeId;
}
