package org.paranora.ssoc.pac4j.client;

import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.paranora.ssoc.pac4j.credentials.SecretKeyCredentials;
import org.paranora.ssoc.pac4j.extractor.SecretKeyProvider;
import org.paranora.ssoc.pac4j.extractor.SimpleCiphertextExtractor;
import org.pac4j.core.client.DirectClient;
import org.pac4j.core.credentials.authenticator.Authenticator;

/**
 * The type Simple ciphertext client.
 */
public class SimpleCiphertextClient extends DirectClient<SecretKeyCredentials> {

    /**
     * The Secret key provider.
     */
    protected SecretKeyProvider secretKeyProvider=null;

    /**
     * The Extractor.
     */
    protected CredentialsExtractor extractor;

    /**
     * Instantiates a new Simple ciphertext client.
     *
     * @param authenticator the authenticator
     */
    public SimpleCiphertextClient(Authenticator<SecretKeyCredentials> authenticator) {
        setAuthenticator(authenticator);
    }

    /**
     * Instantiates a new Simple ciphertext client.
     *
     * @param authenticator the authenticator
     * @param extractor     the extractor
     */
    public SimpleCiphertextClient(Authenticator<SecretKeyCredentials> authenticator,CredentialsExtractor extractor) {
        this(authenticator);
        this.extractor=extractor;
    }

    @Override
    protected void clientInit() {
        if (getCredentialsExtractor() == null) {
            defaultCredentialsExtractor(extractor);
        }
    }

}
