package org.paranora.ssoc.configuration;

/**
 * The interface Shiro configurer.
 *
 * @param <T> the type parameter
 */
public interface ShiroConfigurer<T extends ShiroConfig> {

    /**
     * Configure.
     *
     * @param config the config
     */
    void configure(T config);
}
