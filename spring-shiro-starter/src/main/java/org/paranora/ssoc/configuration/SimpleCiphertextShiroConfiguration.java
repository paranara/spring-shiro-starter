package org.paranora.ssoc.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * The type Simple ciphertext shiro configuration.
 */
@Order(0)
@Configuration
public class SimpleCiphertextShiroConfiguration extends RestStatelessShiroConfiguration<SimpleCiphertextShiroConfig>{

    @Override
    protected SimpleCiphertextShiroConfig createConfig() {
        return new SimpleCiphertextShiroConfig();
    }
}
