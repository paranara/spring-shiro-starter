package org.paranora.ssoc.configuration;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * The type Shiro configuration.
 *
 * @param <T> the type parameter
 */
public abstract class ShiroConfiguration<T extends ShiroConfig> {

    /**
     * The Config.
     */
    protected T config;

    /**
     * Create config t.
     *
     * @return the t
     */
    protected abstract T createConfig();

    /**
     * Instantiates a new Shiro configuration.
     */
    public ShiroConfiguration(){
        config=createConfig();
    }

    @Autowired
    private List<ShiroConfigurer> configurers = Collections.emptyList();

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        for (ShiroConfigurer configurer : configurers) {
            try {
                configurer.configure(config);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot configure shiro conf.", e);
            }
        }
    }

    /**
     * Generate security manager default web security manager.
     *
     * @return the default web security manager
     */
    public DefaultWebSecurityManager generateSecurityManager(){
        return config.generateSecurityManager();
    }

    /**
     * Generate shiro filter factory bean shiro filter factory bean.
     *
     * @param securityManager the security manager
     * @return the shiro filter factory bean
     */
    public ShiroFilterFactoryBean generateShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        return config.generateShiroFilterFactoryBean(securityManager);
    }

    /**
     * Generate advisor auto proxy creator default advisor auto proxy creator.
     *
     * @return the default advisor auto proxy creator
     */
    public DefaultAdvisorAutoProxyCreator generateAdvisorAutoProxyCreator() {
        return config.generateAdvisorAutoProxyCreator();
    }

    /**
     * Generate authorization attribute source advisor authorization attribute source advisor.
     *
     * @param securityManager the security manager
     * @return the authorization attribute source advisor
     */
    public AuthorizationAttributeSourceAdvisor generateAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        return config.generateAuthorizationAttributeSourceAdvisor(securityManager);
    }
}
