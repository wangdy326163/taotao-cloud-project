package com.taotao.cloud.promotion.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 促销skuVO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionSkuVO {

    /**
     * 促销类型
     * @see PromotionTypeEnum
     */
    private String promotionType;

    /**
     * 促销活动
     */
    private String activityId;

}
