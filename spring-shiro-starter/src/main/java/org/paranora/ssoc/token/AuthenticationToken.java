package org.paranora.ssoc.token;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class AuthenticationToken implements Authentication {

    private TokenRequest tokenRequest;

    private Collection<GrantedAuthority> authorities;
    private boolean authenticated = false;
    private Object details;

    public AuthenticationToken(TokenRequest tokenRequest) {
        setTokenRequest(tokenRequest);
    }

    public TokenRequest getTokenRequest() {
        return tokenRequest;
    }

    public void setTokenRequest(TokenRequest tokenRequest) {
        this.tokenRequest = tokenRequest;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority a : authorities) {
            if (a == null) {
                throw new IllegalArgumentException("Authorities collection cannot contain any null elements");
            }
        }
        ArrayList<GrantedAuthority> temp = new ArrayList<>(authorities.size());
        temp.addAll(authorities);
        this.authorities = Collections.unmodifiableList(temp);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        if (this.getPrincipal() instanceof Principal) {
            return ((Principal) this.getPrincipal()).getName();
        }
        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }
}
