package org.paranora.ssoc.annotation;

import org.paranora.ssoc.configuration.CasShiroConfiguration;
import org.paranora.ssoc.configuration.CasShiroConfigurer;
import org.paranora.ssoc.configuration.LifecycleBeanPostProcessorConfiguration;
import org.paranora.ssoc.configuration.ShiroAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * The interface Cas shiro auto configuration.
 */
@Order(0)
@Configuration
@Import({CasShiroConfigurer.class, CasShiroConfiguration.class, LifecycleBeanPostProcessorConfiguration.class, ShiroAutoConfiguration.class})
public @interface CasShiroAutoConfiguration {
}
