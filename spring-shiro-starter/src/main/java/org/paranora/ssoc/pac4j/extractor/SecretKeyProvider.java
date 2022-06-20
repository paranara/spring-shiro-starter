package org.paranora.ssoc.pac4j.extractor;


/**
 * The interface Secret key provider.
 */
public interface SecretKeyProvider {
    /**
     * Gets secret key.
     *
     * @param token the token
     * @return the secret key
     */
    String getSecretKey(String token);
}
