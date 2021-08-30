package com.taotao.cloud.canal.annotation;

import com.taotao.cloud.canal.configuration.CanalClientConfiguration;
import com.taotao.cloud.canal.properties.CanalProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 Canal 客户端
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 14:08
 * @Modified_By 阿导 2018/5/28 14:08
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CanalClientConfiguration.class})
public @interface EnableCanalClient {
}
