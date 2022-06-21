package org.paranora.ssoc.shiro.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.paranora.ssoc.shiro.token.BasicRestStatelessLoginAuthToken;
import org.paranora.ssoc.shiro.vo.RestfulResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.pac4j.core.credentials.UsernamePasswordCredentials;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * The type Basic rest stateless login filter.
 */
public class BasicRestStatelessLoginFilter extends BasicAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        UsernamePasswordCredentials credentials=new UsernamePasswordCredentials(username,password);
        BasicRestStatelessLoginAuthToken token = new BasicRestStatelessLoginAuthToken();
        token.setUserName(username);
        token.setPassword(password);
        token.setCredentials(credentials);
        return token;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
        writeResponseContent(response,RestfulResponse.fail("500","login error!"));
        return false;
    }

}
