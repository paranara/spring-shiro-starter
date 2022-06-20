package org.paranora.ssoc.token;


public interface TokenServices {
    AccessToken createToken(Authentication authentication);

    AccessToken refreshToken(AccessToken refreshToken);

    AccessToken getToken(Authentication authentication);

}
