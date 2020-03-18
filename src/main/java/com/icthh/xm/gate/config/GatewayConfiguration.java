package com.icthh.xm.gate.config;

import com.icthh.xm.commons.security.XmAuthenticationContextHolder;
import com.icthh.xm.gate.gateway.accesscontrol.AccessControlFilter;
import com.icthh.xm.gate.gateway.ratelimiting.RateLimitingFilter;
import com.icthh.xm.gate.gateway.responserewriting.SwaggerBasePathRewritingFilter;
import com.icthh.xm.gate.service.TenantPropertiesService;
import io.github.jhipster.config.JHipsterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Configuration
    public static class SwaggerBasePathRewritingConfiguration {

        @Bean
        public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter() {
            return new SwaggerBasePathRewritingFilter();
        }
    }

    @Configuration
    public static class AccessControlFilterConfiguration {

        @Bean
        public AccessControlFilter accessControlFilter(RouteLocator routeLocator, JHipsterProperties jHipsterProperties) {
            return new AccessControlFilter(routeLocator, jHipsterProperties);
        }
    }

    /**
     * Configures the Zuul filter that limits the number of API calls per user.
     * <p>
     * This uses Bucke4J to limit the API calls, see {@link RateLimitingFilter}.
     */
    @Configuration
    @ConditionalOnProperty("jhipster.gateway.rate-limiting.enabled")
    @RequiredArgsConstructor
    public static class RateLimitingConfiguration {

        private final JHipsterProperties jHipsterProperties;

        private final TenantPropertiesService tenantPropertiesService;

        @Bean
        public RateLimitingFilter rateLimitingFilter(XmAuthenticationContextHolder authenticationContextHolder) {
            return new RateLimitingFilter(jHipsterProperties, authenticationContextHolder, tenantPropertiesService);
        }
    }
}
