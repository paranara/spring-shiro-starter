package org.paranora.ssoc.configuration;

import org.pac4j.core.engine.SecurityLogic;
import org.paranora.ssoc.pac4j.client.BasicAjaxRequestResolver;
import org.paranora.ssoc.pac4j.client.BasicCasClient;
import org.paranora.ssoc.pac4j.engine.BasicSecurityLogic;
import org.paranora.ssoc.pac4j.filter.BasicSecurityFilter;
import org.paranora.ssoc.shiro.filter.BasicAnonymousFilter;
import org.paranora.ssoc.shiro.filter.BasicCallbackFilter;
import org.paranora.ssoc.shiro.filter.BasicLogoutFilter;
import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.paranora.ssoc.shiro.session.*;
import org.paranora.ssoc.shiro.subject.BasicWebSubjectFactory;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Client;
import org.pac4j.core.context.session.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Default web shiro configurer.
 *
 * @param <T> the type parameter
 */
public abstract class DefaultWebShiroConfigurer<T extends DefaultWebShiroConfig> extends DefaultShiroConfigurer<T> {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * The Shiro config.
     */
    @Autowired
    protected ShiroConfigurationProperties shiroConfig;

    /**
     * The Rest template.
     */
    @Autowired(required = false)
    protected RestTemplate restTemplate;

    @Override
    public void configure(T config) {
        config.setConfig(shiroConfig);
        config.setRestOperations(restTemplate);
        config.setRedisConnectionFactory(redisConnectionFactory);

        super.configure(config);

        SessionStore sessionStore = generateSessionStore(config);
        config.setSessionStore(sessionStore);

        SessionIdGenerator sessionIdGenerator = generateSessionIdGenerator(config);
        config.setSessionIdGenerator(sessionIdGenerator);

        SubjectFactory subjectFactory = generateSubjectFactory(config);
        config.setSubjectFactory(subjectFactory);

        SimpleCookie sessionIdCookie = generateSessionIdCookie(config);
        config.setSessionIdCookie(sessionIdCookie);

        SessionDAO sessionDAO = generateSessionDAO(config);
        config.setSessionDAO(sessionDAO);

        SessionFactory sessionFactory = generateSessionFactory(config);
        config.setSessionFactory(sessionFactory);

        WebSessionManager sessionManager = generateSessionManager(config);
        config.setSessionManager(sessionManager);
    }

    @Override
    protected Filter defaultSecurityFilter(T config) {
        String clientNames= generateClientNames(config);
        BasicSecurityFilter securityFilter = new BasicSecurityFilter();
        securityFilter.setSecurityLogic(defaultSecurityLogic());
//        securityFilter.setClients("cas,rest,jwt");
        securityFilter.setClients(clientNames);
        securityFilter.setConfig(config.getCasConfig());
        return securityFilter;
    }

    @Override
    protected SecurityLogic defaultSecurityLogic() {
        return new BasicSecurityLogic<>();
    }

    /**
     * Generate session store session store.
     *
     * @param config the config
     * @return the session store
     */
    protected SessionStore generateSessionStore(T config) {
        return new BasicSessionStore();
    }

    @Override
    protected Map<String, Client> generateDefaultClient(T config) {
        Map<String, Client> clients = new HashMap<>();
        BasicCasClient casClient = generateCasClient(config);
        clients.put("casClient",casClient);
        return clients;
    }

    @Override
    protected Map<String, Filter> generateDefaultShiroFilters(T config) {
        Map<String, Filter> filters = new HashMap<String, Filter>();
        Filter securityFilter = defaultSecurityFilter(config);
        filters.put("default", securityFilter);

        BasicCallbackFilter callbackFilter = new BasicCallbackFilter();
        callbackFilter.setConfig(config.getCasConfig());
        callbackFilter.setDefaultUrl(shiroConfig.getCas().getClient().getServiceUrl());
        filters.put("callback", callbackFilter);

        BasicLogoutFilter logoutFilter = new BasicLogoutFilter();
        logoutFilter.setConfig(config.getCasConfig());
        logoutFilter.setDestroySession(true);
        logoutFilter.setCentralLogout(true);
        logoutFilter.setLocalLogout(true);
        logoutFilter.setDefaultUrl(shiroConfig.getCas().getClient().getServiceUrl());
        filters.put("logout", logoutFilter);

        BasicAnonymousFilter anonymousFilter = new BasicAnonymousFilter();
        filters.put("none", anonymousFilter);
        return filters;
    }

    @Override
    protected Map<String, Filter> generateCustomShiroFilters(T config) {
        return new HashMap<>();
    }

    /**
     * Generate cas client basic cas client.
     *
     * @param config the config
     * @return the basic cas client
     */
    protected BasicCasClient generateCasClient(T config) {
        CasConfiguration casConfiguration = config.getCasConfiguration();
        BasicCasClient casClient = new BasicCasClient();
        casClient.setConfiguration(casConfiguration);
        casClient.setCallbackUrl(config.getConfig().getCas().getClient().getCallbackUrl());
        casClient.setAjaxRequestResolver(new BasicAjaxRequestResolver());
        casClient.setName("cas");
        return casClient;
    }

    /**
     * Generate session id generator session id generator.
     *
     * @param config the config
     * @return the session id generator
     */
    protected SessionIdGenerator generateSessionIdGenerator(T config) {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * Generate subject factory subject factory.
     *
     * @param config the config
     * @return the subject factory
     */
    protected SubjectFactory generateSubjectFactory(T config) {
        return new BasicWebSubjectFactory();
    }

    /**
     * Generate session id cookie simple cookie.
     *
     * @param config the config
     * @return the simple cookie
     */
    protected SimpleCookie generateSessionIdCookie(T config) {
        SimpleCookie cookie = new SimpleCookie(config.getConfig().getSessionIdCookie().getName());
        cookie.setMaxAge(config.getConfig().getSessionIdCookie().getMaxAge());
        cookie.setPath(config.getConfig().getSessionIdCookie().getPath());
        cookie.setHttpOnly(config.getConfig().getSessionIdCookie().isHttpOnly());
        return cookie;
    }

    /**
     * Generate session redis template redis template.
     *
     * @param config the config
     * @return the redis template
     */
    protected RedisTemplate<String, Session> generateSessionRedisTemplate(T config) {
        final RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        final JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();

        RedisTemplate<String, Session> template = new RedisTemplate<>();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jdkSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jdkSerializer);
        template.setConnectionFactory(config.getRedisConnectionFactory());
        return template;
    }

    /**
     * Generate session dao session dao.
     *
     * @param config the config
     * @return the session dao
     */
    protected SessionDAO generateSessionDAO(T config) {
        SessionIdGenerator sessionIdGenerator = config.getSessionIdGenerator();
//        MemorySessionDAO sessionDAO = new MemorySessionDAO();
//        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        EhCacheManager ehCacheManager = generateEhCacheManager(config);
        BasicCacheSessionDAO sessionDAO = new BasicCacheSessionDAO();
        sessionDAO.setCacheManager(ehCacheManager);
        sessionDAO.setActiveSessionsCacheName("cacheTest");

        RedisTemplate<String, Session> sessionRedisTemplate = generateSessionRedisTemplate(config);
        BasicRedisSessionDAO basicRedisSessionDAO = new BasicRedisSessionDAO();
        basicRedisSessionDAO.setRedisTemplate(sessionRedisTemplate);
        basicRedisSessionDAO.setSessionTimeout((int) config.getConfig().getSession().getTimeOut());
        basicRedisSessionDAO.setSessionKeyPrefix(config.getConfig().getSession().getCacheKeyPrefix());

        sessionDAO.setSessionDAO(basicRedisSessionDAO);
        return sessionDAO;
    }

    /**
     * Generate session factory basic session factory.
     *
     * @param config the config
     * @return the basic session factory
     */
    protected BasicSessionFactory generateSessionFactory(T config) {
        BasicSessionFactory sessionFactory = new BasicSessionFactory();
        return sessionFactory;
    }

    /**
     * Generate session manager basic session manager.
     *
     * @param config the config
     * @return the basic session manager
     */
    protected BasicSessionManager generateSessionManager(T config) {
        SimpleCookie sessionIdCookie = config.getSessionIdCookie();
        SessionDAO sessionDAO = config.getSessionDAO();
        SessionFactory sessionFactory = config.getSessionFactory();

        BasicSessionManager sessionManager = new BasicSessionManager();
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setGlobalSessionTimeout(shiroConfig.getSession().getTimeOut());
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionFactory(sessionFactory);
        return sessionManager;
    }

    /**
     * Generate eh cache manager eh cache manager.
     *
     * @param config the config
     * @return the eh cache manager
     */
    protected EhCacheManager generateEhCacheManager(T config) {
        String configFIle = config.getConfig().getEhCacheConfigFile();
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile(configFIle);
        return ehCacheManager;
    }

}
