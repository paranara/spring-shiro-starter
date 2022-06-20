package org.paranora.ssoc.annotation;

import org.paranora.ssoc.configuration.LifecycleBeanPostProcessorConfiguration;
import org.paranora.ssoc.configuration.ShiroAutoConfiguration;
import org.paranora.ssoc.configuration.SimpleCiphertextShiroConfiguration;
import org.paranora.ssoc.configuration.SimpleCiphertextShiroConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * The interface Simple ciphertext shiro auto configuration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Import({SimpleCiphertextShiroConfigurer.class, SimpleCiphertextShiroConfiguration.class, LifecycleBeanPostProcessorConfiguration.class, ShiroAutoConfiguration.class})
public @interface SimpleCiphertextShiroAutoConfiguration {
}
