package org.paranora.ssoc.pac4j.extractor;

import org.springframework.util.ObjectUtils;

/**
 * The type Properties secret key provider.
 */
public class PropertiesSecretKeyProvider implements SecretKeyProvider {

    /**
     * The Properties.
     */
    protected SecretKeyProperties properties;

    /**
     * Instantiates a new Properties secret key provider.
     *
     * @param properties the properties
     */
    public PropertiesSecretKeyProvider(SecretKeyProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getSecretKey(String token) {
        if (ObjectUtils.isEmpty(this.properties)) return null;
        String key = properties.getDefaultKey();
        if (!ObjectUtils.isEmpty(token)
                && !ObjectUtils.isEmpty(properties.getKeys())) {
            String k = properties.getKeys().get(token);
            if (!ObjectUtils.isEmpty(k)) {
                key = k;
            }
        }

        return key;
    }
}
