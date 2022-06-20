package org.paranora.ssoc.token;

import java.util.*;

public class TokenRequestAbstract implements TokenRequest{

    private String grantType;
    private String clientId;
    private Set<String> scope = new HashSet<String>();
    private Map<String, String> requestParameters = Collections.unmodifiableMap(new HashMap<String, String>());

    protected TokenRequestAbstract() {
    }

    public TokenRequestAbstract(String clientId, String grantType, Collection<String> scope, Map<String, String> requestParameters) {
        setClientId(clientId);
        setRequestParameters(requestParameters);
        setScope(scope);
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setScope(Collection<String> scope) {
        if (scope != null && scope.size() == 1) {
            String value = scope.iterator().next();
            if (value.contains(" ") || value.contains(",")) {
                scope = parseParameterList(value);
            }
        }
        this.scope = Collections
                .unmodifiableSet(scope == null ? new LinkedHashSet<String>()
                        : new LinkedHashSet<String>(scope));
    }

    public Set<String> getScope() {
        return scope;
    }

    protected Set<String> parseParameterList(String values) {
        Set<String> result = new TreeSet<String>();
        if (values != null && values.trim().length() > 0) {
            // the spec says the scope is separated by spaces
            String[] tokens = values.split("[\\s+]");
            result.addAll(Arrays.asList(tokens));
        }
        return result;
    }


    public void setRequestParameters(Map<String, String> requestParameters) {
        if (requestParameters != null) {
            this.requestParameters = Collections.unmodifiableMap(new HashMap<String, String>(requestParameters));
        }
    }

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }
}
