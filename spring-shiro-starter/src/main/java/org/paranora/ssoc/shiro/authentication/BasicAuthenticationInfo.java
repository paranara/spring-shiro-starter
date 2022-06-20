package org.paranora.ssoc.shiro.authentication;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * The type Basic authentication info.
 */
public class BasicAuthenticationInfo extends SimpleAuthenticationInfo {

    /**
     * Instantiates a new Basic authentication info.
     *
     * @param principal   the principal
     * @param credentials the credentials
     * @param realmName   the realm name
     */
    public BasicAuthenticationInfo(Object principal, Object credentials, String realmName) {
        super(principal, credentials, realmName);
    }

    /**
     * Instantiates a new Basic authentication info.
     *
     * @param principal         the principal
     * @param hashedCredentials the hashed credentials
     * @param credentialsSalt   the credentials salt
     * @param realmName         the realm name
     */
    public BasicAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName) {
        super(principal, hashedCredentials, credentialsSalt, realmName);
    }

    /**
     * Instantiates a new Basic authentication info.
     *
     * @param principals  the principals
     * @param credentials the credentials
     */
    public BasicAuthenticationInfo(PrincipalCollection principals, Object credentials) {
        super(principals, credentials);

    }

    /**
     * Instantiates a new Basic authentication info.
     *
     * @param principals        the principals
     * @param hashedCredentials the hashed credentials
     * @param credentialsSalt   the credentials salt
     */
    public BasicAuthenticationInfo(PrincipalCollection principals, Object hashedCredentials, ByteSource credentialsSalt) {
        super(principals, hashedCredentials, credentialsSalt);

    }
}
