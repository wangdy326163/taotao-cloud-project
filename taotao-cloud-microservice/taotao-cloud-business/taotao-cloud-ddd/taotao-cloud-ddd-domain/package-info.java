package com.taotao.cloud.ddd.domain;

/**
 * |--- domain                      -- 领域层
 * |      |--- model                -- 领域对象
 * |      |       |--- aggregate    -- 聚合
 * |      |       |--- entities     -- 实休
 * |      |       |--- vo           -- 值对象
 * |      |--- service              -- 域服务
 * |      |--- factory              -- 工厂，针对一些复杂的Object可以通过工厂来构建
 * |      |--- port                 -- 端口，即接口
 * |      |--- event                -- 领域事件
 * |      |--- exception            -- 异常封装
 * |      |--- ability              -- 领域能力
 * |      |--- extension            -- 扩展点
 * |      |       |--- impl        -- 扩展点实现
 */
/**
 * Enterprise Business Rules: 这一层就是最为核心的业务逻辑层，
 * 这一层不包含任何和技术相关的内容，只包含业务逻辑。
 */
/**
 * DomainService命名统一以Service为后缀。
 * Entity实体类的命名不用后缀。 值对象类的定义统一以VO结尾。
 * DomainService逻辑中可以调用Repository和Port中定义的接口。
 * DomainService可以操作多个聚合，实体和值对象。
 * Entity实体类可以有构造函数，builder，getters。 不要直接放开所有属性的setters，防止业务代码随意修改实体的属性。
 * 编写业务逻辑需要遵守原则：优先将业务逻辑放在Entity和VO中，然后才是放在聚合中，最后才放在DomainService中。
 * 依赖反转原则：Domain层依赖的外部接口都要定义在Domain模块的port包中。Domain层只面向接口编程，不依赖接口实现类。
 */

/**
 * 4.领域对象 VS 数据对象
 * 1.数据对象使用基本类型保持纯净：PlayerEntity
 * 2.领域对象需要体现业务含义：PlayerQueryResultDomain
 *
 * <p>领域对象 VS 业务对象
 * 1.数据对象使用基本类型保持纯净：PlayerEntity
 * 2.业务对象同样会体现业务 最大不同是领域对象采用充血模型聚合业务: PlayerCreateBO
 *
 * <p>领域层 VS 应用层
 * 第一个区别：领域层关注纵向，应用层关注横向。领域层纵向做隔离，本领域业务行为要在本领域内处理完。
 * 应用层横向做编排，聚合和编排领域服务。
 * 第二个区别：应用层可以更加灵活组合不同领域业务，
 * 并且可以增加流控、监控、日志、权限，分布式锁，相较于领域层功能更为丰富。
 */
