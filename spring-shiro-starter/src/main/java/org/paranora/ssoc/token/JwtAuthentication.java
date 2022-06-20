package org.paranora.ssoc.token;

public class JwtAuthentication extends AuthenticationToken {

    public JwtAuthentication(TokenRequest tokenRequest) {
        super(tokenRequest);
    }
}
