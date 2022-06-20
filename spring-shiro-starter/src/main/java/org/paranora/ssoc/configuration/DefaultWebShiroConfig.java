package org.paranora.ssoc.configuration;

import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.pac4j.core.context.session.SessionStore;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.Map;

/**
 * The type Default web shiro config.
 */
public abstract class DefaultWebShiroConfig extends ShiroConfig {

    /**
     * The Session store.
     */
    protected SessionStore sessionStore;
    /**
     * The Session id generator.
     */
    protected SessionIdGenerator sessionIdGenerator;
    /**
     * The Subject factory.
     */
    protected SubjectFactory subjectFactory;
    /**
     * The Session id cookie.
     */
    protected SimpleCookie sessionIdCookie;
    /**
     * The Session dao.
     */
    protected SessionDAO sessionDAO;
    /**
     * The Session factory.
     */
    protected SessionFactory sessionFactory;
    /**
     * The Session manager.
     */
    protected WebSessionManager sessionManager;
    /**
     * The Redis connection factory.
     */
    protected RedisConnectionFactory redisConnectionFactory;

    /**
     * Gets session store.
     *
     * @return the session store
     */
    public SessionStore getSessionStore() {
        return sessionStore;
    }

    /**
     * Sets session store.
     *
     * @param sessionStore the session store
     */
    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    /**
     * Gets session id generator.
     *
     * @return the session id generator
     */
    public SessionIdGenerator getSessionIdGenerator() {
        return sessionIdGenerator;
    }

    /**
     * Sets session id generator.
     *
     * @param sessionIdGenerator the session id generator
     */
    public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {
        this.sessionIdGenerator = sessionIdGenerator;
    }

    /**
     * Gets subject factory.
     *
     * @return the subject factory
     */
    public SubjectFactory getSubjectFactory() {
        return subjectFactory;
    }

    /**
     * Sets subject factory.
     *
     * @param subjectFactory the subject factory
     */
    public void setSubjectFactory(SubjectFactory subjectFactory) {
        this.subjectFactory = subjectFactory;
    }

    /**
     * Gets session id cookie.
     *
     * @return the session id cookie
     */
    public SimpleCookie getSessionIdCookie() {
        return sessionIdCookie;
    }

    /**
     * Sets session id cookie.
     *
     * @param sessionIdCookie the session id cookie
     */
    public void setSessionIdCookie(SimpleCookie sessionIdCookie) {
        this.sessionIdCookie = sessionIdCookie;
    }

    /**
     * Gets session dao.
     *
     * @return the session dao
     */
    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    /**
     * Sets session dao.
     *
     * @param sessionDAO the session dao
     */
    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Sets session factory.
     *
     * @param sessionFactory the session factory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets session manager.
     *
     * @return the session manager
     */
    public WebSessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Sets session manager.
     *
     * @param sessionManager the session manager
     */
    public void setSessionManager(WebSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Gets redis connection factory.
     *
     * @return the redis connection factory
     */
    public RedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }

    /**
     * Sets redis connection factory.
     *
     * @param redisConnectionFactory the redis connection factory
     */
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public DefaultWebSecurityManager generateSecurityManager() {
        SessionManager sessionManager = getSessionManager();
        SubjectFactory subjectFactory = getSubjectFactory();

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        Collection<Realm> realms = getRealms().values();

        securityManager.setRealms(realms);
        securityManager.setSubjectFactory(subjectFactory);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Override
    public ShiroFilterFactoryBean generateShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterChainDefinition shiroFilterChainDefinition = getShiroFilterChainDefinition();
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setLoginUrl(getConfig().getCas().getServer().getLoginUrl());
        filterFactoryBean.setSuccessUrl(getConfig().getCas().getClient().getSuccessUrl());
        filterFactoryBean.setUnauthorizedUrl(getConfig().getCas().getClient().getUnauthorizedUrl());
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        Map<String, Filter> filters = getShiroFilters();
        filterFactoryBean.setFilters(filters);
        return filterFactoryBean;

    }
}
