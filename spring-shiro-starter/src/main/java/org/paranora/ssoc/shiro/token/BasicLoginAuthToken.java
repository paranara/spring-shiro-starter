package org.paranora.ssoc.shiro.token;

import org.pac4j.core.credentials.Credentials;

/**
 * The type Basic login auth token.
 */
public abstract class BasicLoginAuthToken extends BasicAuthToken {

    /**
     * The User name.
     */
    protected String userName;

    /**
     * The Password.
     */
    protected String password;

    /**
     * The Credentials.
     */
    protected Credentials credentials;

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
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets credentials.
     *
     * @param credentials the credentials
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
