package org.paranora.ssoc.token;

import java.util.Map;
import java.util.Set;

public interface TokenRequest {
    String getClientId();
    String getGrantType();
    Set<String> getScope();
    Map<String, String> getRequestParameters();
}
