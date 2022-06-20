package org.paranora.ssoc.configuration;

import org.pac4j.core.engine.SecurityLogic;
import org.paranora.ssoc.pac4j.client.BasicCasRestFormClient;
import org.paranora.ssoc.pac4j.client.BasicHeaderClient;
import org.paranora.ssoc.pac4j.engine.BasicRestStatelessSecurityLogic;
import org.paranora.ssoc.pac4j.filter.BasicSecurityFilter;
import org.paranora.ssoc.pac4j.profile.BasicJwtGenerator;
import org.paranora.ssoc.shiro.filter.BasicAnonymousFilter;
import org.paranora.ssoc.shiro.filter.BasicRestStatelessLoginFilter;
import org.paranora.ssoc.shiro.filter.BasicRestStatelessRefreshFilter;
import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.paranora.ssoc.shiro.realm.BasicRestStatelessLoginRealm;
import org.paranora.ssoc.shiro.realm.BasicRestStatelessRefreshRealm;
import org.apache.shiro.realm.Realm;
import org.pac4j.core.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;


/**
 * The type Default rest stateless shiro configurer.
 *
 * @param <T> the type parameter
 */
public abstract class DefaultRestStatelessShiroConfigurer<T extends RestStatelessShiroConfig> extends DefaultShiroConfigurer<T> {

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
        super.configure(config);
    }

    @Override
    protected Filter defaultSecurityFilter(T config) {
        String clientNames= generateClientNames(config);
        BasicSecurityFilter securityFilter = new BasicSecurityFilter();
        securityFilter.setSecurityLogic(defaultSecurityLogic());
        securityFilter.setClients(clientNames);
        securityFilter.setConfig(config.getCasConfig());
        return securityFilter;
    }

    @Override
    protected SecurityLogic defaultSecurityLogic() {
        return new BasicRestStatelessSecurityLogic();
    }

    @Override
    protected Map<String, Filter> generateDefaultShiroFilters(T config) {
        Map<String, Filter> filters = new HashMap<String, Filter>();
        Filter securityFilter = defaultSecurityFilter(config);
        filters.put("default", securityFilter);
        BasicAnonymousFilter anonymousFilter = new BasicAnonymousFilter();
        filters.put("none", anonymousFilter);
        return filters;
    }

    @Override
    protected Map<String, Filter> generateCustomShiroFilters(T config) {
        Map<String, Filter> filters = new HashMap<String, Filter>();
        BasicRestStatelessLoginFilter restLoginFilter = new BasicRestStatelessLoginFilter();
        filters.put("restJwtLogin", restLoginFilter);

        BasicRestStatelessRefreshFilter restRefreshFilter=new BasicRestStatelessRefreshFilter();
        filters.put("restJwtRefresh", restRefreshFilter);
        return filters;
    }

    @Override
    protected Map<String, Client> generateDefaultClient(T config) {
        Map<String, Client> clients = new HashMap<>();
//        BasicCasRestFormClient casRestFormClient = config.getCasRestFormClient();
//        clients.put("casRestFormClient", casRestFormClient);

        BasicHeaderClient headerClient = generateHeaderClient(config);
        clients.put("headerClient", headerClient);
        return clients;
    }

    @Override
    protected Map<String, Realm> generateDefaultRealms(T config) {
        return super.generateDefaultRealms(config);
    }

    @Override
    protected Map<String,Realm> generateCustomRealms(T config) {
        Map<String,Realm> realms = new HashMap<>();

        BasicCasRestFormClient casRestFormClient = config.getCasRestFormClient();
        BasicJwtGenerator jwtGenerator = config.getJwtGenerator();
        BasicRestStatelessLoginRealm restLoginRealm = new BasicRestStatelessLoginRealm();
        restLoginRealm.setCasRestFormClient(casRestFormClient);
        restLoginRealm.setJwtGenerator(jwtGenerator);
        realms.put("restLoginRealm",restLoginRealm);

        BasicRestStatelessRefreshRealm restRefreshRealm=new BasicRestStatelessRefreshRealm();
        restRefreshRealm.setJwtGenerator(jwtGenerator);
        restRefreshRealm.setJwtAuthenticator(config.getJwtAuthenticator());
        realms.put("restRefreshRealm",restRefreshRealm);
        return realms;
    }
}
