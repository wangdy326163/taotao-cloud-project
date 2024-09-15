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

package com.taotao.cloud.goods.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 店铺商品分类表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:50:58
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = StoreGoodsLabelPO.TABLE_NAME)
@TableName(StoreGoodsLabelPO.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = StoreGoodsLabel.TABLE_NAME, comment = "店铺商品标签表")
public class StoreGoodsLabelPO extends BaseSuperEntity<StoreGoodsLabelPO, Long> {

    public static final String TABLE_NAME = "tt_store_goods_label";

    /** 店铺ID */
    @Column(name = "store_id", columnDefinition = "bigint not null comment '店铺ID'")
    private Long storeId;

    /** 店铺商品分类名称 */
    @Column(name = "label_name", columnDefinition = "varchar(255) not null comment '店铺商品分类名称'")
    private String labelName;

    /** 店铺商品分类排序 */
    @Column(name = "sort_order", columnDefinition = "int not null comment '店铺商品分类排序'")
    private Integer sortOrder;

    /** 父id, 根节点为0 */
    @Column(name = "parent_id", columnDefinition = "bigint not null comment '父id, 根节点为0'")
    private Long parentId;

    /** 层级, 从0开始 */
    @Column(name = "level", columnDefinition = "int not null comment '层级, 从0开始'")
    private Integer level;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StoreGoodsLabelPO that = (StoreGoodsLabelPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
