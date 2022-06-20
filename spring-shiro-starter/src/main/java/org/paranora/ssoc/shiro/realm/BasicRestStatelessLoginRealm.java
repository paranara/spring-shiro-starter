package org.paranora.ssoc.shiro.realm;

import org.paranora.ssoc.pac4j.client.BasicCasRestFormClient;
import org.paranora.ssoc.pac4j.context.BasicWebContext;
import org.paranora.ssoc.pac4j.profile.BasicRestLoginProfile;
import org.paranora.ssoc.pac4j.profile.BasicRestProfile;
import org.paranora.ssoc.shiro.token.BasicRestStatelessLoginAuthToken;
import org.paranora.ssoc.shiro.token.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.util.Pac4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The type Basic rest stateless login realm.
 */
public class BasicRestStatelessLoginRealm extends BasicRestStatelessRealm<BasicRestStatelessLoginAuthToken,BasicRestLoginProfile> {

    /**
     * The Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(BasicRestStatelessLoginRealm.class);

    /**
     * The Cas rest form client.
     */
    protected BasicCasRestFormClient casRestFormClient;

    /**
     * Gets cas rest form client.
     *
     * @return the cas rest form client
     */
    public BasicCasRestFormClient getCasRestFormClient() {
        return casRestFormClient;
    }

    /**
     * Sets cas rest form client.
     *
     * @param casRestFormClient the cas rest form client
     */
    public void setCasRestFormClient(BasicCasRestFormClient casRestFormClient) {
        this.casRestFormClient = casRestFormClient;
    }

    /**
     * Instantiates a new Basic rest stateless login realm.
     */
    public BasicRestStatelessLoginRealm() {
        setAuthenticationTokenClass(BasicRestStatelessLoginAuthToken.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            BasicRestStatelessLoginAuthToken authToken = (BasicRestStatelessLoginAuthToken) token;
            UsernamePasswordCredentials credentials=(UsernamePasswordCredentials) authToken.getCredentials();
            CasProfile casProfile=retrieveCasProfile(authToken,credentials);
            JwtToken jwtToken=generateJwtToken(authToken.getUserName(),casProfile);
            BasicRestProfile profile =generateProfile(casProfile,jwtToken);
            return new SimpleAuthenticationInfo(profile, credentials, getName());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new AuthenticationException("login error,please check username and password !");
        }
    }

    @Override
    protected BasicRestLoginProfile createProfile() {
        return new BasicRestLoginProfile();
    }

    protected CasProfile retrieveCasProfile(BasicRestStatelessLoginAuthToken authToken, UsernamePasswordCredentials credentials){
        final BasicWebContext context = (BasicWebContext) authToken.getContext();
        casRestFormClient.getAuthenticator().validate(credentials,context);
        final CasRestProfile casRestProfile = (CasRestProfile) credentials.getUserProfile();
        final TokenCredentials casCredentials = casRestFormClient.requestServiceTicket(casRestProfile, context);
        final CasProfile casProfile = casRestFormClient.validateServiceTicket(casCredentials, context);
        casProfile.addAttribute(Pac4jConstants.USERNAME,authToken.getUserName());
        casProfile.addAttribute("iss","paranora");
        return casProfile;
    }

}
