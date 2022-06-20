package org.paranora.ssoc.configuration;

import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * The type Cas shiro configurer.
 */
@Order(0)
@Configuration
@EnableConfigurationProperties({ShiroConfigurationProperties.class})
public class CasShiroConfigurer extends DefaultWebShiroConfigurer<CasShiroConfig> {
}
