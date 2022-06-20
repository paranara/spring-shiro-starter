package org.paranora.ssoc.pac4j.credentials;

import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.util.CommonHelper;

/**
 * The type Secret key credentials.
 */
public class SecretKeyCredentials extends TokenCredentials {

    private long timestamp;
    private String sign;
    private String secretKey;
    private String id;

    /**
     * Instantiates a new Secret key credentials.
     *
     * @param token the token
     */
    public SecretKeyCredentials(String token) {
        super(token);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets secret key.
     *
     * @return the secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets secret key.
     *
     * @param secretKey the secret key
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets sign.
     *
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * Sets sign.
     *
     * @param sign the sign
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SecretKeyCredentials that = (SecretKeyCredentials) o;
        String tokenThis=sign+timestamp+getToken();
        String tokenThat=that.getSign()+that.getTimestamp()+that.getToken();
        return !(getToken() != null ? !tokenThis.equals(tokenThat) : tokenThat != null);
    }

    @Override
    public int hashCode() {
        String tokenThis=sign+timestamp+getToken();
        return getToken() != null ? tokenThis.hashCode() : 0;
    }

    @Override
    public String toString() {
        String tokenThis=sign+timestamp+getToken();
        return CommonHelper.toNiceString(this.getClass(), "token", tokenThis);
    }
}
