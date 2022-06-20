package org.paranora.ssoc.shiro.token;

import org.pac4j.cas.profile.CasProfile;

/**
 * The type Basic rest stateless refresh auth token.
 */
public class BasicRestStatelessRefreshAuthToken extends BasicRefreshAuthToken {

    /**
     * The Cas profile.
     */
    protected CasProfile casProfile;

    /**
     * Instantiates a new Basic rest stateless refresh auth token.
     */
    public BasicRestStatelessRefreshAuthToken(){

    }

    /**
     * Gets cas profile.
     *
     * @return the cas profile
     */
    public CasProfile getCasProfile() {
        return casProfile;
    }

    /**
     * Sets cas profile.
     *
     * @param casProfile the cas profile
     */
    public void setCasProfile(CasProfile casProfile) {
        this.casProfile = casProfile;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return secret;
    }
}
