package org.paranora.ssoc.shiro.authorization;

import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.Collection;
import java.util.Set;

/**
 * The type Basic authorization info.
 */
public class BasicAuthorizationInfo extends SimpleAuthorizationInfo {

    /**
     * Instantiates a new Basic authorization info.
     */
    public BasicAuthorizationInfo() {
    }

    /**
     * Instantiates a new Basic authorization info.
     *
     * @param roles the roles
     */
    public BasicAuthorizationInfo(Set<String> roles) {
        super(roles);
    }


    @Override
    public void addStringPermissions(Collection<String> permissions) {
        if (null == permissions || permissions.size() < 1) return;
        super.addStringPermissions(permissions);
    }

    @Override
    public void addRoles(Collection<String> roles) {
        if (null == roles || roles.size() < 1) return;
        super.addRoles(roles);
    }
}
