package org.paranora.ssoc.pac4j.client;

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.redirect.RedirectionActionBuilder;

/**
 * The type Basic cas client.
 */
public class BasicCasClient extends CasClient {

    /**
     * Instantiates a new Basic cas client.
     */
    public BasicCasClient() {
        super();
    }

    /**
     * Instantiates a new Basic cas client.
     *
     * @param configuration the configuration
     */
    public BasicCasClient(CasConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void defaultRedirectionActionBuilder(RedirectionActionBuilder redirectActionBuilder) {
        setRedirectionActionBuilder(new BaseCasRedirectionActionBuilder(getConfiguration(),this));
    }
}
