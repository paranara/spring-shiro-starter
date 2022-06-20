package org.paranora.ssoc.configuration;

import org.pac4j.core.engine.SecurityLogic;
import org.paranora.ssoc.cas.validation.BasicCas30ServiceTicketValidator;
import org.paranora.ssoc.pac4j.authenticator.BasicJwtAuthenticator;
import org.paranora.ssoc.pac4j.cas.config.BasicCasConfiguration;
import org.paranora.ssoc.pac4j.client.BasicCasRestFormClient;
import org.paranora.ssoc.pac4j.client.BasicHeaderClient;
import org.paranora.ssoc.pac4j.profile.BasicJwtGenerator;
import org.paranora.ssoc.pac4j.realm.BasicCasRealm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;
import java.util.*;

/**
 * The type Default shiro configurer.
 *
 * @param <T> the type parameter
 */
public abstract class DefaultShiroConfigurer<T extends ShiroConfig> implements ShiroConfigurer<T> {

    @Override
    public void configure(T config) {

        BasicCasConfiguration casConfiguration = generateCasConfiguration(config);
        config.setCasConfiguration(casConfiguration);

        BasicJwtGenerator jwtGenerator = generateJwtGenerator(config);
        config.setJwtGenerator(jwtGenerator);

        BasicJwtAuthenticator jwtAuthenticator = generateJwtAuthenticator(config);
        config.setJwtAuthenticator(jwtAuthenticator);

        BasicCasRestFormClient casRestFormClient = generateCasRestFormClient(config);
        config.setCasRestFormClient(casRestFormClient);
//
//        BasicHeaderClient headerClient = generateHeaderClient(config);
//        config.setHeaderClient(headerClient);

        config.setClients(generateClients(config));

        config.setClientsObj(generateClientsObj(config));

        config.setClients(generateClients(config));

        config.setCasConfig(generateConfig(config));

        config.setRealms(generateRealms(config));

        config.setShiroFilters(generateShiroFilters(config));

        ShiroFilterChainDefinition shiroFilterChainDefinition = generateShiroFilterChainDefinition(config);
        config.setShiroFilterChainDefinition(shiroFilterChainDefinition);
    }

    /**
     * Default security filter filter.
     *
     * @param config the config
     * @return the filter
     */
    protected abstract Filter defaultSecurityFilter(T config);

    /**
     * Default security logic security logic.
     *
     * @return the security logic
     */
    protected abstract SecurityLogic defaultSecurityLogic();

    /**
     * Generate client names string.
     *
     * @param config the config
     * @return the string
     */
    protected String generateClientNames(T config) {
        List<String> names = new ArrayList<>();
        for (Client it : config.getClients().values()) {
            names.add(it.getName());
        }
        return StringUtils.join(names, ",");
    }

    /**
     * Generate config config.
     *
     * @param config the config
     * @return the config
     */
    protected Config generateConfig(T config) {
        Config casConf = new Config();
        casConf.setClients(config.getClientsObj());
        return casConf;
    }

    /**
     * Generate jwt generator basic jwt generator.
     *
     * @param config the config
     * @return the basic jwt generator
     */
    protected BasicJwtGenerator generateJwtGenerator(T config) {
        ShiroConfigurationProperties.JwtConfiguration jwt = config.getConfig().getJwt();
        if (ObjectUtils.isEmpty(jwt)) return null;
        BasicJwtGenerator generator = new BasicJwtGenerator(new SecretSignatureConfiguration(jwt.getSalt()), new SecretEncryptionConfiguration(jwt.getSalt()));
        generator.setExpiresTime(jwt.getExpiresTime());
        return generator;
    }

    /**
     * Generate jwt authenticator basic jwt authenticator.
     *
     * @param config the config
     * @return the basic jwt authenticator
     */
    protected BasicJwtAuthenticator generateJwtAuthenticator(T config) {
        ShiroConfigurationProperties.JwtConfiguration jwt = config.getConfig().getJwt();
        if (ObjectUtils.isEmpty(jwt)) return null;
        BasicJwtAuthenticator authenticator = new BasicJwtAuthenticator();
        authenticator.addSignatureConfiguration(new SecretSignatureConfiguration(jwt.getSalt()));
        authenticator.addEncryptionConfiguration(new SecretEncryptionConfiguration(jwt.getSalt()));
        return authenticator;
    }

    /**
     * Generate cas configuration basic cas configuration.
     *
     * @param config the config
     * @return the basic cas configuration
     */
    protected BasicCasConfiguration generateCasConfiguration(T config) {
        ShiroConfigurationProperties.CasConfiguration cas = config.getConfig().getCas();
        if (ObjectUtils.isEmpty(cas)) return null;
        BasicCasConfiguration casConfig = new BasicCasConfiguration();
        casConfig.setProtocol(CasProtocol.CAS30);
        casConfig.setPrefixUrl(cas.getServer().getServiceUrl());
        casConfig.setLoginUrl(cas.getServer().getLoginUrl());
        casConfig.setDefaultTicketValidator(new BasicCas30ServiceTicketValidator(cas.getServer().getServiceUrl(), config.getRestOperations()));
        casConfig.setAcceptAnyProxy(true);
        return casConfig;
    }

    /**
     * Generate cas rest form client basic cas rest form client.
     *
     * @param config the config
     * @return the basic cas rest form client
     */
    protected BasicCasRestFormClient generateCasRestFormClient(T config) {
        BasicCasConfiguration casConfig = config.getCasConfiguration();
        BasicCasRestFormClient client = new BasicCasRestFormClient(casConfig, Pac4jConstants.USERNAME, Pac4jConstants.PASSWORD, config.getRestOperations());
        client.setName("rest");
        return client;
    }

//    @Bean
//    public BasicParameterClient parameterClient(BasicJwtAuthenticator jwtAuthenticator) {
//        BasicParameterClient parameterClient = new BasicParameterClient("token", jwtAuthenticator);
//        parameterClient.setSupportGetRequest(true);
//        parameterClient.setName("jwt");
//        return parameterClient;
//    }

    /**
     * Generate header client basic header client.
     *
     * @param config the config
     * @return the basic header client
     */
    protected BasicHeaderClient generateHeaderClient(T config) {
        BasicJwtAuthenticator jwtAuthenticator = config.getJwtAuthenticator();
        BasicHeaderClient client = new BasicHeaderClient("token", jwtAuthenticator);
        client.setName("jwt");
        return client;
    }

    /**
     * Generate clients obj clients.
     *
     * @param config the config
     * @return the clients
     */
    protected Clients generateClientsObj(T config) {
        List<Client> clientList = new ArrayList<>();
        Map<String, Client> defaultClient = config.getClients();
        putMapToList(defaultClient, clientList);
        Clients clients = new Clients();
        clients.setClients(clientList);
        return clients;
    }

    /**
     * Put map to list.
     *
     * @param <T>  the type parameter
     * @param map  the map
     * @param list the list
     */
    protected <T> void putMapToList(Map<String, T> map, List<T> list) {
        if (null != map && map.size() > 0) {
            if (null != list) {
                list.addAll(map.values());
            }
        }
    }

    /**
     * Generate clients map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Client> generateClients(T config) {
        Map<String, Client> clientList = new HashMap<>();
        Map<String, Client> defaultClient = generateDefaultClient(config);
        Map<String, Client> customClient = generateCustomClient(config);
        putMapToMap(defaultClient, clientList);
        putMapToMap(customClient, clientList);
        return clientList;
    }

    /**
     * Generate default client map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Client> generateDefaultClient(T config) {
        return null;
    }

    /**
     * Generate custom client map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Client> generateCustomClient(T config) {
        return null;
    }

    /**
     * Generate cas realm basic cas realm.
     *
     * @return the basic cas realm
     */
    protected BasicCasRealm generateCasRealm() {
        BasicCasRealm realm = new BasicCasRealm();
        return realm;
    }

    /**
     * Put map to map.
     *
     * @param <T>  the type parameter
     * @param maps the maps
     * @param mapt the mapt
     */
    protected <T> void putMapToMap(Map<String, T> maps, Map<String, T> mapt) {
        if (null != maps && maps.size() > 0) {
            if (null != mapt) {
                mapt.putAll(maps);
            }
        }
    }

    /**
     * Generate realms map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Realm> generateRealms(T config) {
        Map<String, Realm> realms = new HashMap<>();
        Map<String, Realm> defaultRealms = generateDefaultRealms(config);
        Map<String, Realm> customRealms = generateCustomRealms(config);
        putMapToMap(defaultRealms, realms);
        putMapToMap(customRealms, realms);
        return realms;
    }

    /**
     * Generate default realms map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Realm> generateDefaultRealms(T config) {
        BasicCasRealm casRealm = generateCasRealm();
        Map<String, Realm> realms = new HashMap<>();
        realms.put("casRealm", casRealm);
        return realms;
    }

    /**
     * Generate custom realms map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Realm> generateCustomRealms(T config) {
        return null;
    }

    /**
     * Generate default shiro filters map.
     *
     * @param config the config
     * @return the map
     */
    protected abstract Map<String, Filter> generateDefaultShiroFilters(T config);

    /**
     * Generate custom shiro filters map.
     *
     * @param config the config
     * @return the map
     */
    protected abstract Map<String, Filter> generateCustomShiroFilters(T config);

    /**
     * Generate shiro filters map.
     *
     * @param config the config
     * @return the map
     */
    protected Map<String, Filter> generateShiroFilters(T config) {
        Map<String, Filter> filter = new HashMap<>();
        filter.putAll(generateDefaultShiroFilters(config));
        filter.putAll(generateCustomShiroFilters(config));
        return filter;
    }

    /**
     * Generate shiro filter chain definition shiro filter chain definition.
     *
     * @param config the config
     * @return the shiro filter chain definition
     */
    protected ShiroFilterChainDefinition generateShiroFilterChainDefinition(T config) {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinitions(config.getConfig().getFilter());
        return definition;
    }
}
