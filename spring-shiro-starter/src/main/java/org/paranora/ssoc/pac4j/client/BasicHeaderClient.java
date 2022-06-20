package org.paranora.ssoc.pac4j.client;

import org.paranora.ssoc.pac4j.extractor.BasicHeaderExtractor;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.profile.creator.ProfileCreator;
import org.pac4j.http.client.direct.HeaderClient;

/**
 * The type Basic header client.
 */
public class BasicHeaderClient extends HeaderClient {


    /**
     * Instantiates a new Basic header client.
     *
     * @param headerName         the header name
     * @param tokenAuthenticator the token authenticator
     */
    public BasicHeaderClient(final String headerName, final Authenticator tokenAuthenticator) {
        super(headerName,tokenAuthenticator);
    }

    /**
     * Instantiates a new Basic header client.
     *
     * @param headerName         the header name
     * @param prefixHeader       the prefix header
     * @param tokenAuthenticator the token authenticator
     */
    public BasicHeaderClient(final String headerName, final String prefixHeader,
                        final Authenticator tokenAuthenticator) {
        super(headerName,prefixHeader,tokenAuthenticator);
        defaultCredentialsExtractor(new BasicHeaderExtractor(headerName,""));
    }

    /**
     * Instantiates a new Basic header client.
     *
     * @param headerName         the header name
     * @param tokenAuthenticator the token authenticator
     * @param profileCreator     the profile creator
     */
    public BasicHeaderClient(final String headerName, final Authenticator tokenAuthenticator,
                        final ProfileCreator profileCreator) {
        super(headerName,tokenAuthenticator,profileCreator);
        defaultCredentialsExtractor(new BasicHeaderExtractor(headerName,""));

    }

    /**
     * Instantiates a new Basic header client.
     *
     * @param headerName         the header name
     * @param prefixHeader       the prefix header
     * @param tokenAuthenticator the token authenticator
     * @param profileCreator     the profile creator
     */
    public BasicHeaderClient(final String headerName, final String prefixHeader,
                        final Authenticator tokenAuthenticator, final ProfileCreator profileCreator) {
        super(headerName,prefixHeader,tokenAuthenticator,profileCreator);
        defaultCredentialsExtractor(new BasicHeaderExtractor(headerName,prefixHeader));

    }

}
