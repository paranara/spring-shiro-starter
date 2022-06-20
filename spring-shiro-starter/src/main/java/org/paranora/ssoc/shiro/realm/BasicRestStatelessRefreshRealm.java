package org.paranora.ssoc.shiro.realm;

import org.paranora.ssoc.pac4j.authenticator.BasicJwtAuthenticator;
import org.paranora.ssoc.pac4j.profile.BasicRestProfile;
import org.paranora.ssoc.pac4j.profile.BasicRestRefreshProfile;
import org.paranora.ssoc.shiro.token.BasicRestStatelessRefreshAuthToken;
import org.paranora.ssoc.shiro.token.JwtToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Basic rest stateless refresh realm.
 */
public class BasicRestStatelessRefreshRealm extends BasicRestStatelessRealm<BasicRestStatelessRefreshAuthToken, BasicRestRefreshProfile> {

    /**
     * The Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(BasicRestStatelessRefreshRealm.class);

    /**
     * The Jwt authenticator.
     */
    protected BasicJwtAuthenticator jwtAuthenticator;

    /**
     * Gets jwt authenticator.
     *
     * @return the jwt authenticator
     */
    public BasicJwtAuthenticator getJwtAuthenticator() {
        return jwtAuthenticator;
    }

    /**
     * Sets jwt authenticator.
     *
     * @param jwtAuthenticator the jwt authenticator
     */
    public void setJwtAuthenticator(BasicJwtAuthenticator jwtAuthenticator) {
        this.jwtAuthenticator = jwtAuthenticator;
    }

    /**
     * Instantiates a new Basic rest stateless refresh realm.
     */
    public BasicRestStatelessRefreshRealm() {
        setAuthenticationTokenClass(BasicRestStatelessRefreshAuthToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            BasicRestStatelessRefreshAuthToken authToken = (BasicRestStatelessRefreshAuthToken) token;
            CasProfile casProfile = validate(authToken);
            JwtToken jwtToken = generateJwtToken(casProfile.getUsername(), casProfile);
            BasicRestProfile profile = generateProfile(casProfile, jwtToken);
            return new SimpleAuthenticationInfo(profile, authToken.getCredentials(), getName());
        } catch (AuthenticationException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AuthenticationException("refresh error !");
        }
    }

    /**
     * Validate cas profile.
     *
     * @param authToken the auth token
     * @return the cas profile
     */
    protected CasProfile validate(BasicRestStatelessRefreshAuthToken authToken) {
        TokenCredentials tokenCredentials = new TokenCredentials(authToken.getToken());
        WebContext context = authToken.getContext();
        jwtAuthenticator.validate(tokenCredentials, context);
        CasProfile casProfile = (CasProfile) tokenCredentials.getUserProfile();
        String securityCode = DigestUtils.md5Hex(authToken.getToken() + casProfile.getId() + authToken.getTimestamp());
        if (!securityCode.equalsIgnoreCase(authToken.getSecret())) {
            throw new AuthenticationException("refresh failed, error reqeust !");
        }
        authToken.setCasProfile(casProfile);
        casProfile = retrieveCasProfile(authToken, null);
        return casProfile;
    }

    @Override
    protected BasicRestRefreshProfile createProfile() {
        return new BasicRestRefreshProfile();
    }

    @Override
    protected CasProfile retrieveCasProfile(BasicRestStatelessRefreshAuthToken authToken, UsernamePasswordCredentials credentials) {
        CasProfile profile = authToken.getCasProfile();
        return profile;
    }

}
