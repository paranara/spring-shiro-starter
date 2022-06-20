package org.paranora.ssoc.configuration;

import org.paranora.ssoc.pac4j.authenticator.BasicJwtAuthenticator;
import org.paranora.ssoc.pac4j.cas.config.BasicCasConfiguration;
import org.paranora.ssoc.pac4j.client.BasicCasRestFormClient;
import org.paranora.ssoc.pac4j.profile.BasicJwtGenerator;
import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.web.client.RestOperations;

import javax.servlet.Filter;
import java.util.*;

/**
 * The type Shiro config.
 */
public abstract class ShiroConfig {

    /**
     * The Rest operations.
     */
    protected RestOperations restOperations;
    /**
     * The Config.
     */
    protected ShiroConfigurationProperties config;

    /**
     * The Jwt authenticator.
     */
    protected BasicJwtAuthenticator jwtAuthenticator;
    /**
     * The Jwt generator.
     */
    protected BasicJwtGenerator jwtGenerator;
    /**
     * The Cas configuration.
     */
    protected BasicCasConfiguration casConfiguration;
    /**
     * The Cas rest form client.
     */
    protected BasicCasRestFormClient casRestFormClient;
//    protected BasicParameterClient parameterClient;
//    protected BasicHeaderClient headerClient;

//    protected List<Client> defaultClients;
//    protected List<Client> customClients;
//
//    protected List<Realm> defaultRealms;
//    protected List<Realm> customRealms;

    /**
     * The Clients obj.
     */
    protected Clients clientsObj;
    /**
     * The Cas config.
     */
    protected Config casConfig;
    /**
     * The Clients.
     */
    protected Map<String,Client> clients;
    /**
     * The Realms.
     */
    protected Map<String, Realm> realms;
    /**
     * The Shiro filters.
     */
    protected Map<String, Filter> shiroFilters;

//    protected Map<String, Filter> defaultShiroFilters;
//    protected Map<String, Filter> customShiroFilters;

    /**
     * The Shiro filter chain definition.
     */
    protected ShiroFilterChainDefinition ShiroFilterChainDefinition;


    /**
     * Gets clients obj.
     *
     * @return the clients obj
     */
    public Clients getClientsObj() {
        return clientsObj;
    }

    /**
     * Sets clients obj.
     *
     * @param clientsObj the clients obj
     */
    public void setClientsObj(Clients clientsObj) {
        this.clientsObj = clientsObj;
    }

    /**
     * Gets clients.
     *
     * @return the clients
     */
    public Map<String, Client> getClients() {
        return clients;
    }

    /**
     * Sets clients.
     *
     * @param clients the clients
     */
    public void setClients(Map<String, Client> clients) {
        this.clients = clients;
    }

    /**
     * Gets jwt authenticator.
     *
     * @return the jwt authenticator
     */
    public BasicJwtAuthenticator getJwtAuthenticator() {
        return jwtAuthenticator;
    }

    /**
     * Sets jwt authenticator.
     *
     * @param jwtAuthenticator the jwt authenticator
     */
    public void setJwtAuthenticator(BasicJwtAuthenticator jwtAuthenticator) {
        this.jwtAuthenticator = jwtAuthenticator;
    }

    /**
     * Gets realms.
     *
     * @return the realms
     */
    public Map<String, Realm> getRealms() {
        return realms;
    }

    /**
     * Sets realms.
     *
     * @param realms the realms
     */
    public void setRealms(Map<String, Realm> realms) {
        this.realms = realms;
    }

    /**
     * Gets shiro filters.
     *
     * @return the shiro filters
     */
    public Map<String, Filter> getShiroFilters() {
        return shiroFilters;
    }

    /**
     * Sets shiro filters.
     *
     * @param shiroFilters the shiro filters
     */
    public void setShiroFilters(Map<String, Filter> shiroFilters) {
        this.shiroFilters = shiroFilters;
    }

    /**
     * Gets cas configuration.
     *
     * @return the cas configuration
     */
    public BasicCasConfiguration getCasConfiguration() {
        return casConfiguration;
    }

    /**
     * Sets cas configuration.
     *
     * @param casConfiguration the cas configuration
     */
    public void setCasConfiguration(BasicCasConfiguration casConfiguration) {
        this.casConfiguration = casConfiguration;
    }

    /**
     * Gets jwt generator.
     *
     * @return the jwt generator
     */
    public BasicJwtGenerator getJwtGenerator() {
        return jwtGenerator;
    }

    /**
     * Sets jwt generator.
     *
     * @param jwtGenerator the jwt generator
     */
    public void setJwtGenerator(BasicJwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    /**
     * Gets cas rest form client.
     *
     * @return the cas rest form client
     */
    public BasicCasRestFormClient getCasRestFormClient() {
        return casRestFormClient;
    }

    /**
     * Sets cas rest form client.
     *
     * @param casRestFormClient the cas rest form client
     */
    public void setCasRestFormClient(BasicCasRestFormClient casRestFormClient) {
        this.casRestFormClient = casRestFormClient;
    }

    /**
     * Gets rest operations.
     *
     * @return the rest operations
     */
    public RestOperations getRestOperations() {
        return restOperations;
    }

    /**
     * Sets rest operations.
     *
     * @param restOperations the rest operations
     */
    public void setRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    /**
     * Gets cas config.
     *
     * @return the cas config
     */
    public Config getCasConfig() {
        return casConfig;
    }

    /**
     * Sets cas config.
     *
     * @param casConfig the cas config
     */
    public void setCasConfig(Config casConfig) {
        this.casConfig = casConfig;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public ShiroConfigurationProperties getConfig() {
        return config;
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    public void setConfig(ShiroConfigurationProperties config) {
        this.config = config;
    }

    /**
     * Gets shiro filter chain definition.
     *
     * @return the shiro filter chain definition
     */
    public org.apache.shiro.spring.web.config.ShiroFilterChainDefinition getShiroFilterChainDefinition() {
        return ShiroFilterChainDefinition;
    }

    /**
     * Sets shiro filter chain definition.
     *
     * @param shiroFilterChainDefinition the shiro filter chain definition
     */
    public void setShiroFilterChainDefinition(org.apache.shiro.spring.web.config.ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterChainDefinition = shiroFilterChainDefinition;
    }

    /**
     * Generate security manager default web security manager.
     *
     * @return the default web security manager
     */
    public abstract DefaultWebSecurityManager generateSecurityManager();

    /**
     * Generate shiro filter factory bean shiro filter factory bean.
     *
     * @param securityManager the security manager
     * @return the shiro filter factory bean
     */
    public ShiroFilterFactoryBean generateShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterChainDefinition shiroFilterChainDefinition = getShiroFilterChainDefinition();
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        Map<String, Filter> filters =getShiroFilters();
        filterFactoryBean.setFilters(filters);
        return filterFactoryBean;
    }

    /**
     * Generate advisor auto proxy creator default advisor auto proxy creator.
     *
     * @return the default advisor auto proxy creator
     */
    public DefaultAdvisorAutoProxyCreator generateAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * Generate authorization attribute source advisor authorization attribute source advisor.
     *
     * @param securityManager the security manager
     * @return the authorization attribute source advisor
     */
    public AuthorizationAttributeSourceAdvisor generateAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
