package org.paranora.ssoc.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;
import org.pac4j.core.context.WebContext;

/**
 * The type Basic auth token.
 */
public abstract class BasicAuthToken implements AuthenticationToken {

    /**
     * The Token.
     */
    protected String token;

    /**
     * The Context.
     */
    protected WebContext context;

    /**
     * Gets context.
     *
     * @return the context
     */
    public WebContext getContext() {
        return context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(WebContext context) {
        this.context = context;
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
}
