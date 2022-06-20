package org.paranora.ssoc.configuration;

import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.paranora.ssoc.pac4j.authenticator.SimpleSecretKeyAuthenticator;
import org.paranora.ssoc.pac4j.client.SimpleCiphertextClient;
import org.paranora.ssoc.pac4j.extractor.PropertiesSecretKeyProvider;
import org.paranora.ssoc.pac4j.extractor.SecretKeyProperties;
import org.paranora.ssoc.pac4j.extractor.SecretKeyProvider;
import org.paranora.ssoc.pac4j.extractor.SimpleCiphertextExtractor;
import org.paranora.ssoc.shiro.property.ShiroConfigurationProperties;
import org.apache.shiro.realm.Realm;
import org.pac4j.core.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Simple ciphertext shiro configurer.
 */
@Order(0)
@Configuration
@EnableConfigurationProperties({ShiroConfigurationProperties.class, SecretKeyProperties.class})
public class SimpleCiphertextShiroConfigurer extends DefaultRestStatelessShiroConfigurer<SimpleCiphertextShiroConfig> {

    @Autowired
    private SecretKeyProperties secretKeyProperties;

    @Override
    protected Map<String, Filter> generateCustomShiroFilters(SimpleCiphertextShiroConfig config) {
        return new HashMap<>();
    }

    /**
     * Secret key provider secret key provider.
     *
     * @return the secret key provider
     */
    protected SecretKeyProvider secretKeyProvider() {
        return new PropertiesSecretKeyProvider(secretKeyProperties);
    }

    /**
     * Credentials extractor credentials extractor.
     *
     * @return the credentials extractor
     */
    protected CredentialsExtractor credentialsExtractor(){
        return new SimpleCiphertextExtractor(secretKeyProvider());
    }

    /**
     * Authenticator authenticator.
     *
     * @return the authenticator
     */
    protected Authenticator authenticator(){
        return new SimpleSecretKeyAuthenticator(secretKeyProperties.getSignTimeRange());
    }

    /**
     * Generate secret key client simple ciphertext client.
     *
     * @param config the config
     * @return the simple ciphertext client
     */
    protected SimpleCiphertextClient generateSecretKeyClient(SimpleCiphertextShiroConfig config) {
        Authenticator authenticator = authenticator();
        SimpleCiphertextClient client = new SimpleCiphertextClient(authenticator, credentialsExtractor());
        client.setName("secretkey");
        return client;
    }

    @Override
    protected Map<String, Client> generateDefaultClient(SimpleCiphertextShiroConfig config) {
        Map<String, Client> clients = new HashMap<>();
        SimpleCiphertextClient client = generateSecretKeyClient(config);
        clients.put("secretkeyClient", client);
        return clients;
    }

    @Override
    protected Map<String, Realm> generateCustomRealms(SimpleCiphertextShiroConfig config) {
        return new HashMap<>();
    }
}
