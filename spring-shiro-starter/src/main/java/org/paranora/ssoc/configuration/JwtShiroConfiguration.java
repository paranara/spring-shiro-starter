package org.paranora.ssoc.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * The type Jwt shiro configuration.
 */
@Order(0)
@Configuration
public class JwtShiroConfiguration extends RestStatelessShiroConfiguration<JwtShiroConfig>{

    @Override
    protected JwtShiroConfig createConfig() {
        return new JwtShiroConfig();
    }
}
