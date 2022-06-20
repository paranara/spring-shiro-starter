package org.paranora.ssoc.shiro.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Shiro configuration properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro", ignoreUnknownFields = true)
public class ShiroConfigurationProperties {

    private ShiroConfigurationProperties.SessionIdCookieConfiguration sessionIdCookie;

    private ShiroConfigurationProperties.CasConfiguration cas;

    private ShiroConfigurationProperties.JwtConfiguration jwt;

    private ShiroConfigurationProperties.SessionConfiguration session;

    private String ehCacheConfigFile;

    private Map<String, String> filter=new LinkedHashMap();

    /**
     * The type Session configuration.
     */
    @Data
    public static class SessionConfiguration {
        private long timeOut;
        private String cacheKeyPrefix;
    }

    /**
     * The type Cas configuration.
     */
    @Data
    public static class CasConfiguration implements Serializable {

        private ShiroConfigurationProperties.CasServerConfiguration server;

        private ShiroConfigurationProperties.CasClientConfiguration client;
    }

    /**
     * The type Cas server configuration.
     */
    @Data
    public static class CasServerConfiguration implements Serializable {
        private String serviceUrl;
        private String loginUrl;
        private String logoutUrl;
    }

    /**
     * The type Cas client configuration.
     */
    @Data
    public static class CasClientConfiguration implements Serializable {
        private String serviceUrl;
        private String successUrl;
        private String unauthorizedUrl;
        private String callbackUrl;
    }

    /**
     * The type Jwt configuration.
     */
    @Data
    public static class JwtConfiguration implements Serializable {
        private String salt;
        private int expiresTime= 60*30;

    }

    /**
     * The type Session id cookie configuration.
     */
    @Data
    public static class SessionIdCookieConfiguration implements Serializable {
        private String name;
        private String value;
        private String comment;
        private String domain;
        private String path;
        private int maxAge;
        private int version;
        private boolean secure;
        private boolean httpOnly;
    }
}
