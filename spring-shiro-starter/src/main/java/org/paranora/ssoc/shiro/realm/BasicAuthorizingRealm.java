package org.paranora.ssoc.shiro.realm;

import org.paranora.ssoc.shiro.authorization.BasicAuthorizationInfo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Basic authorizing realm.
 */
public abstract class BasicAuthorizingRealm extends AuthorizingRealm {

    /**
     * Instantiates a new Basic authorizing realm.
     */
    public BasicAuthorizingRealm() {
        setAuthenticationTokenClass(null);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return null != getAuthenticationTokenClass() && super.supports(token);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return createAuthorizationInfo(principals);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    /**
     * Prepare permissions list.
     *
     * @param principals the principals
     * @return the list
     */
    protected List<String> preparePermissions(final PrincipalCollection principals) {
        List<String> permissions = new ArrayList<>();
        ;
//        permissions.add("user:info");
        return permissions;
    }

    /**
     * Prepare roles list.
     *
     * @param principals the principals
     * @return the list
     */
    protected List<String> prepareRoles(final PrincipalCollection principals) {
        List<String> roles = new ArrayList<>();
//        permissions.add("user:info");
        return roles;
    }

    /**
     * Create authorization info authorization info.
     *
     * @param principals the principals
     * @return the authorization info
     */
    protected AuthorizationInfo createAuthorizationInfo(final PrincipalCollection principals) {
        List<String> permissions = preparePermissions(principals);
        List<String> roles = prepareRoles(principals);

        BasicAuthorizationInfo authorizationInfo = new BasicAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissions);
        authorizationInfo.addRoles(roles);
        return authorizationInfo;
    }
}
