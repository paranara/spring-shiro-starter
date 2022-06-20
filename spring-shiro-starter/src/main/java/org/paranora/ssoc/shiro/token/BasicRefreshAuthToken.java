package org.paranora.ssoc.shiro.token;

/**
 * The type Basic refresh auth token.
 */
public abstract class BasicRefreshAuthToken extends BasicAuthToken {

    /**
     * The Secret.
     */
    protected String secret;
    /**
     * The Timestamp.
     */
    protected String timestamp;

    /**
     * Gets secret.
     *
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets secret.
     *
     * @param secret the secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
