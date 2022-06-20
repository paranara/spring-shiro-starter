package org.paranora.ssoc.shiro.token;

/**
 * The type Jwt token.
 */
public class JwtToken {

    /**
     * The Token.
     */
    protected String token;
    private String userName;
    private long expiresTime;
    private String issuer;

    /**
     * Gets issuer.
     *
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets issuer.
     *
     * @param issuer the issuer
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets expires time.
     *
     * @return the expires time
     */
    public long getExpiresTime() {
        return expiresTime;
    }

    /**
     * Sets expires time.
     *
     * @param expiresTime the expires time
     */
    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }
}
