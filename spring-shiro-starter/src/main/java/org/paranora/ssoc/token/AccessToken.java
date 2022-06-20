package org.paranora.ssoc.token;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface AccessToken {

    public static String BEARER_TYPE = "Bearer";

    public static String OAUTH2_TYPE = "OAuth2";

    public static String ACCESS_TOKEN = "access_token";

    public static String TOKEN_TYPE = "token_type";

    public static String EXPIRES_IN = "expires_in";

    public static String REFRESH_TOKEN = "refresh_token";

    public static String SCOPE = "scope";

    Map<String, Object> getAdditionalInformation();

    Set<String> getScope();

    RefreshToken getRefreshToken();

    String getTokenType();

    boolean isExpired();

    Date getExpiration();

    int getExpiresIn();

    String getValue();

}
