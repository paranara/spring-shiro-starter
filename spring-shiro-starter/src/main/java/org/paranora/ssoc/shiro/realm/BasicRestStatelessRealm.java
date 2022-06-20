package org.paranora.ssoc.shiro.realm;

import org.paranora.ssoc.pac4j.profile.BasicRestProfile;
import org.paranora.ssoc.pac4j.profile.BasicJwtGenerator;
import org.paranora.ssoc.shiro.token.BasicAuthToken;
import org.paranora.ssoc.shiro.token.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.core.credentials.UsernamePasswordCredentials;


/**
 * The type Basic rest stateless realm.
 *
 * @param <T> the type parameter
 * @param <P> the type parameter
 */
public abstract class BasicRestStatelessRealm<T extends BasicAuthToken,P extends BasicRestProfile> extends BasicAuthorizingRealm {

    /**
     * The Jwt generator.
     */
    protected BasicJwtGenerator jwtGenerator;

    /**
     * Gets jwt generator.
     *
     * @return the jwt generator
     */
    public BasicJwtGenerator getJwtGenerator() {
        return jwtGenerator;
    }

    /**
     * Sets jwt generator.
     *
     * @param jwtGenerator the jwt generator
     */
    public void setJwtGenerator(BasicJwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    /**
     * Generate jwt token jwt token.
     *
     * @param userName   the user name
     * @param casProfile the cas profile
     * @return the jwt token
     */
    protected JwtToken generateJwtToken(String userName, CasProfile casProfile){
        String jwt = jwtGenerator.generate(casProfile);
        JwtToken jwtToken=new JwtToken();
        jwtToken.setToken(jwt);
        jwtToken.setUserName(userName);
        jwtToken.setExpiresTime(jwtGenerator.getExpirationTime().getTime());
        return jwtToken;
    }

    /**
     * Generate profile p.
     *
     * @param casProfile the cas profile
     * @param jwtToken   the jwt token
     * @return the p
     */
    protected P generateProfile(CasProfile casProfile, JwtToken jwtToken){
        P profile =createProfile();
        profile=prepareProfile(profile,casProfile,jwtToken);
        return profile;
    }

    /**
     * Create profile p.
     *
     * @return the p
     */
    protected abstract P createProfile();

    /**
     * Prepare profile p.
     *
     * @param profile    the profile
     * @param casProfile the cas profile
     * @param jwtToken   the jwt token
     * @return the p
     */
    protected P prepareProfile(P profile,CasProfile casProfile, JwtToken jwtToken){
        profile.setId(casProfile.getId());
        profile.addAttributes(casProfile.getAttributes());
        profile.setJwtToken(jwtToken);
        return profile;
    }

    /**
     * Retrieve cas profile cas profile.
     *
     * @param authToken   the auth token
     * @param credentials the credentials
     * @return the cas profile
     */
    protected abstract CasProfile retrieveCasProfile(T authToken, UsernamePasswordCredentials credentials);




}
