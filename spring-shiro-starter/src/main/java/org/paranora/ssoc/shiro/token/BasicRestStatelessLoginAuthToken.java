package org.paranora.ssoc.shiro.token;

/**
 * The type Basic rest stateless login auth token.
 */
public class BasicRestStatelessLoginAuthToken extends BasicLoginAuthToken {

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    /**
     * Instantiates a new Basic rest stateless login auth token.
     */
    public BasicRestStatelessLoginAuthToken() {
    }

    @Override
    public Object getPrincipal() {
        return userName;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

}