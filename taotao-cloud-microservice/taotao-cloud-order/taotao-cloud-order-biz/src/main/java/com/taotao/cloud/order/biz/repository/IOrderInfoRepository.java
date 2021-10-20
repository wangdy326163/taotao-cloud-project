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
package com.taotao.cloud.order.biz.repository;

import com.taotao.cloud.order.biz.domain.order_info.OrderDO;
import com.taotao.cloud.order.biz.entity.OrderInfo;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/22 12:46
 */
@Repository
public interface IOrderInfoRepository extends JpaRepository<OrderInfo, Long> {

	@Query(value = """
			select u from OrderInfo u where u.id = ?#{[0]}
		""")
	List<OrderInfo> findOrderInfoById(Long id);

	OrderInfo findByCode(String code);

	//@Query(value = """
    //        select new com.taotao.cloud.order.api.OrderDO(
	//			u.memberId,
	//			u.code,
	//			u.mainStatus,
	//			u.childStatus,
	//			u.amount,
	//			u.receiverName
	//		)
	//		from OrderInfo u
	//		where u.code = :code
	//	""")
	//List<OrderDO> findOrderInfoByBo(@Param("code") String code);
}
