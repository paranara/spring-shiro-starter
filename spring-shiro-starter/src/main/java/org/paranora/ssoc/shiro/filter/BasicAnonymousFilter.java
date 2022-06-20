package org.paranora.ssoc.shiro.filter;

import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.filter.authc.AnonymousFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The type Basic anonymous filter.
 */
public class BasicAnonymousFilter extends AnonymousFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        return true;
    }

    /**
     * Set pattern matcher.
     *
     * @param pathMatcher the path matcher
     */
    public void setPatternMatcher(PatternMatcher pathMatcher){
        this.pathMatcher=pathMatcher;
    }
}
