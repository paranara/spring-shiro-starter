package org.paranora.ssoc.shiro.filter;

import org.paranora.ssoc.util.ContextHolder;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import java.io.IOException;

/**
 * The type Basic context thread local filter.
 */
public class BasicContextThreadLocalFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            Subject subject = SecurityUtils.getSubject();
            PrincipalCollection pcs = subject.getPrincipals();
            if(null != pcs){
                Pac4jPrincipal p = pcs.oneByType(Pac4jPrincipal.class);
                ContextHolder.setPac4jPrincipal(p);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ContextHolder.clear();
        }

    }

    @Override
    public void destroy() {

    }

}
