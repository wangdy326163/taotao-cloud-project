package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品品牌表
 */
@Entity
@Table(name = Brand.TABLE_NAME)
@TableName(Brand.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Brand.TABLE_NAME, comment = "商品品牌表")
public class Brand extends BaseSuperEntity<Brand, Long> {

	public static final String TABLE_NAME = "tt_brand";
	/**
	 * 排序
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(64) not null comment '品牌名称'")
	private String name;

	/**
	 * 排序
	 */
	@Column(name = "logo", nullable = false, columnDefinition = "varchar(64) not null comment '品牌图标'")
	private String logo;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
