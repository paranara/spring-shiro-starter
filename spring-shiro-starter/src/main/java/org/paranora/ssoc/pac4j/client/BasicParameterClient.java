package org.paranora.ssoc.pac4j.client;

import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.profile.creator.ProfileCreator;
import org.pac4j.http.client.direct.ParameterClient;

/**
 * The type Basic parameter client.
 */
public class BasicParameterClient extends ParameterClient {

    /**
     * Instantiates a new Basic parameter client.
     *
     * @param parameterName      the parameter name
     * @param tokenAuthenticator the token authenticator
     */
    public BasicParameterClient(final String parameterName, final Authenticator tokenAuthenticator) {
        super(parameterName, tokenAuthenticator);
    }

    /**
     * Instantiates a new Basic parameter client.
     *
     * @param parameterName      the parameter name
     * @param tokenAuthenticator the token authenticator
     * @param profileCreator     the profile creator
     */
    public BasicParameterClient(final String parameterName,
                                final Authenticator tokenAuthenticator,
                                final ProfileCreator profileCreator) {
        super(parameterName, tokenAuthenticator, profileCreator);
    }

}