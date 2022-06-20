package org.paranora.ssoc.pac4j.profile;

import org.paranora.ssoc.shiro.token.JwtToken;
import org.pac4j.cas.profile.CasProfile;

/**
 * The type Basic rest profile.
 */
public class BasicRestProfile extends CasProfile {

    private JwtToken jwtToken;

    /**
     * Gets jwt token.
     *
     * @return the jwt token
     */
    public JwtToken getJwtToken() {
        return jwtToken;
    }

    /**
     * Sets jwt token.
     *
     * @param jwtToken the jwt token
     */
    public void setJwtToken(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }
}
