package org.paranora.ssoc.annotation;

import org.paranora.ssoc.configuration.JwtShiroConfiguration;
import org.paranora.ssoc.configuration.JwtShiroConfigurer;
import org.paranora.ssoc.configuration.LifecycleBeanPostProcessorConfiguration;
import org.paranora.ssoc.configuration.ShiroAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * The interface Jwt shiro auto configuration.
 */
@Order(0)
@Configuration
@Import({JwtShiroConfigurer.class, JwtShiroConfiguration.class, LifecycleBeanPostProcessorConfiguration.class, ShiroAutoConfiguration.class})
public @interface JwtShiroAutoConfiguration {
}
