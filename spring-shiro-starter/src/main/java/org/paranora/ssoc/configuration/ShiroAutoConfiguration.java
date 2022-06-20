package org.paranora.ssoc.configuration;

import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * The type Shiro auto configuration.
 */
@Configuration
@EnableConfigurationProperties({ShiroConfigurationProperties.class})
public class ShiroAutoConfiguration {

    /**
     * The Config.
     */
    @Autowired
    protected ShiroConfiguration config;

    /**
     * Generate security manager default web security manager.
     *
     * @return the default web security manager
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("securityManager")
    @ConditionalOnMissingBean({DefaultWebSecurityManager.class})
    public DefaultWebSecurityManager generateSecurityManager() {
        return config.generateSecurityManager();
    }

    /**
     * Generate shiro filter factory bean shiro filter factory bean.
     *
     * @param securityManager the security manager
     * @return the shiro filter factory bean
     */
    @Bean(name = "shiroFilter")
    @ConditionalOnMissingBean({ShiroFilterFactoryBean.class})
    public ShiroFilterFactoryBean generateShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        return config.generateShiroFilterFactoryBean(securityManager);
    }

    /**
     * Advisor auto proxy creator default advisor auto proxy creator.
     *
     * @return the default advisor auto proxy creator
     */
    @Bean
    @ConditionalOnMissingBean({DefaultAdvisorAutoProxyCreator.class})
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        return config.generateAdvisorAutoProxyCreator();
    }

    /**
     * Authorization attribute source advisor authorization attribute source advisor.
     *
     * @param securityManager the security manager
     * @return the authorization attribute source advisor
     */
    @Bean
    @ConditionalOnMissingBean({AuthorizationAttributeSourceAdvisor.class})
    @ConditionalOnBean({DefaultWebSecurityManager.class})
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        return config.generateAuthorizationAttributeSourceAdvisor(securityManager);
    }
}
