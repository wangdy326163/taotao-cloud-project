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

package com.taotao.cloud.auth.biz.authentication.login.extension.sms;

import com.taotao.cloud.auth.biz.authentication.token.JwtTokenGenerator;
import com.taotao.cloud.auth.biz.authentication.login.extension.AbstractExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.ExtensionLoginFilterSecurityConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationFailureHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.JsonExtensionLoginAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.authentication.login.extension.sms.service.SmsCheckCodeService;
import com.taotao.cloud.auth.biz.authentication.login.extension.sms.service.SmsUserDetailsService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class SmsExtensionLoginFilterConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractExtensionLoginFilterConfigurer<
                H,
                SmsExtensionLoginFilterConfigurer<H>,
                SmsAuthenticationFilter,
                ExtensionLoginFilterSecurityConfigurer<H>> {

    private SmsUserDetailsService smsUserDetailsService;

    private SmsCheckCodeService smsCheckCodeService;

    private JwtTokenGenerator jwtTokenGenerator;

    public SmsExtensionLoginFilterConfigurer(ExtensionLoginFilterSecurityConfigurer<H> securityConfigurer) {
        super(securityConfigurer, new SmsAuthenticationFilter(), "/login/phone");
    }

    public SmsExtensionLoginFilterConfigurer<H> smsUserDetailsService(SmsUserDetailsService smsUserDetailsService) {
        this.smsUserDetailsService = smsUserDetailsService;
        return this;
    }

    public SmsExtensionLoginFilterConfigurer<H> smsCheckCodeService(SmsCheckCodeService smsCheckCodeService) {
        this.smsCheckCodeService = smsCheckCodeService;
        return this;
    }

    public SmsExtensionLoginFilterConfigurer<H> jwtTokenGenerator(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    protected AuthenticationProvider authenticationProvider(H http) {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);

        SmsUserDetailsService smsUserDetailsService = this.smsUserDetailsService != null
                ? this.smsUserDetailsService
                : getBeanOrNull(applicationContext, SmsUserDetailsService.class);
        Assert.notNull(smsUserDetailsService, "phoneUserDetailsService is required");

        SmsCheckCodeService smsCheckCodeService = this.smsCheckCodeService != null
                ? this.smsCheckCodeService
                : getBeanOrNull(applicationContext, SmsCheckCodeService.class);
        Assert.notNull(smsCheckCodeService, "phoneService is required");

        return new SmsAuthenticationProvider(smsUserDetailsService, smsCheckCodeService);
    }

    @Override
    protected AuthenticationSuccessHandler defaultSuccessHandler(H http) {
        if (this.jwtTokenGenerator == null) {
            ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
            jwtTokenGenerator = getBeanOrNull(applicationContext, JwtTokenGenerator.class);
        }
        Assert.notNull(jwtTokenGenerator, "jwtTokenGenerator is required");
        return new JsonExtensionLoginAuthenticationSuccessHandler(jwtTokenGenerator);
    }

    @Override
    protected AuthenticationFailureHandler defaultFailureHandler(H http) {
        return new JsonExtensionLoginAuthenticationFailureHandler();
    }
}
