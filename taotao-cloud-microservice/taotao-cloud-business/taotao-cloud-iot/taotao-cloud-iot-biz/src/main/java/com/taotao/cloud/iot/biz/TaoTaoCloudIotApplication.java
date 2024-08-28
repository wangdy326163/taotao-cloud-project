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

package com.taotao.cloud.tenant.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 租户管理中心
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-10 11:04:28
 */
@TaoTaoCloudApplication
public class TaoTaoCloudIotApplication {

    public static void main(String[] args) {
        PropertyUtils.setDefaultProperty("taotao-cloud-iot");

        SpringApplication.run(TaoTaoCloudIotApplication.class, args);
    }
}
