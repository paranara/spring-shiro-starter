package org.paranora.ssoc.token;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ClientDetail extends Serializable {

    String getClientId();

    Set<String> getResourceIds();

    boolean isSecretRequired();

    String getClientSecret();

    boolean isScoped();

    Set<String> getScope();

    Set<String> getAuthorizedGrantTypes();

    Set<String> getRegisteredRedirectUri();

    Collection<GrantedAuthority> getAuthorities();

    Integer getAccessTokenValiditySeconds();

    Integer getRefreshTokenValiditySeconds();


    boolean isAutoApprove(String scope);

    Map<String, Object> getAdditionalInformation();

}
