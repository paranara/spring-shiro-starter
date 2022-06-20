package org.paranora.ssoc.pac4j.extractor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * The type Secret key properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro.secretkey", ignoreUnknownFields = true)
public class SecretKeyProperties {
    private long signTimeRange=10*60*1000;
    private String defaultKey;
    private Map<String, String> keys;
}
