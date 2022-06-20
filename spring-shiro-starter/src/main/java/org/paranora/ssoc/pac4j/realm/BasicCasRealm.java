package org.paranora.ssoc.pac4j.realm;

import org.paranora.ssoc.shiro.authentication.BasicAuthenticationInfo;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The type Basic cas realm.
 */
public class BasicCasRealm extends Pac4jRealm {

    private Logger logger = LoggerFactory.getLogger(BasicCasRealm.class);

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final Pac4jToken pac4jToken = (Pac4jToken) authenticationToken;
        final List<CommonProfile> profiles = pac4jToken.getProfiles();
        final CommonProfile profile = profiles.get(0);
        System.out.println("单点登录返回的信息" + profile.toString());

        final Pac4jPrincipal principal = new Pac4jPrincipal(profiles, getPrincipalNameAttribute());
        final PrincipalCollection principalCollection = new SimplePrincipalCollection(principal, getName());
        AuthenticationInfo authenticationInfo = createAuthenticationInfo(principalCollection, profiles);
        return authenticationInfo;
    }

    /**
     * Create authentication info authentication info.
     *
     * @param <T>        the type parameter
     * @param principals the principals
     * @param profiles   the profiles
     * @return the authentication info
     */
    protected <T extends UserProfile> AuthenticationInfo  createAuthenticationInfo(final  PrincipalCollection principals, List<T> profiles){
        BasicAuthenticationInfo authenticationInfo = new BasicAuthenticationInfo(principals, profiles.hashCode());
        return authenticationInfo;
    }
}