package org.paranora.ssoc.pac4j.client;

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.redirect.CasRedirectionActionBuilder;

/**
 * The type Base cas redirection action builder.
 */
public class BaseCasRedirectionActionBuilder extends CasRedirectionActionBuilder {

    /**
     * Instantiates a new Base cas redirection action builder.
     *
     * @param configuration the configuration
     * @param client        the client
     */
    public BaseCasRedirectionActionBuilder(CasConfiguration configuration, CasClient client) {
        super(configuration, client);
    }
}
