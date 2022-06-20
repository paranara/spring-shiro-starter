package org.paranora.ssoc.shiro.filter;

import com.alibaba.fastjson.JSON;
import org.paranora.ssoc.pac4j.context.BasicWebContext;

import org.paranora.ssoc.shiro.token.BasicRestStatelessRefreshAuthToken;
import org.paranora.ssoc.shiro.vo.RestfulResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Basic rest stateless refresh filter.
 */
public class BasicRestStatelessRefreshFilter extends BasicAuthenticationFilter {

    /**
     * The constant REFRESH_TOKEN_HEADER.
     */
    protected static final String REFRESH_TOKEN_HEADER = "token";
    /**
     * The constant REFRESH_SECRET_HEADER.
     */
    protected static final String REFRESH_SECRET_HEADER = "secret";
    /**
     * The constant REFRESH_TIMESTAMP_HEADER.
     */
    protected static final String REFRESH_TIMESTAMP_HEADER = "timestamp";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return StringUtils.isNotBlank(httpRequest.getHeader(REFRESH_TOKEN_HEADER))
                && StringUtils.isNotBlank(httpRequest.getHeader(REFRESH_SECRET_HEADER))
                && StringUtils.isNotBlank(httpRequest.getHeader(REFRESH_TIMESTAMP_HEADER));
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token=httpRequest.getHeader(REFRESH_TOKEN_HEADER);
        String secret=httpRequest.getHeader(REFRESH_SECRET_HEADER);
        String timestamp=httpRequest.getHeader(REFRESH_TIMESTAMP_HEADER);
        BasicRestStatelessRefreshAuthToken authToken = new BasicRestStatelessRefreshAuthToken();
        authToken.setToken(token);
        authToken.setSecret(secret);
        authToken.setTimestamp(timestamp);
        authToken.setContext(new BasicWebContext((HttpServletRequest) request, (HttpServletResponse) response));
        return authToken;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
        writeResponseContent(response, JSON.toJSONString(RestfulResponse.fail("503", "refresh error!")));
        return false;
    }

}
