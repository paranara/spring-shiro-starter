package org.paranora.ssoc.pac4j.matcher;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.matching.matcher.PathMatcher;

/**
 * The type Basic path matcher.
 */
public class BasicPathMatcher extends PathMatcher {

    private org.apache.shiro.util.AntPathMatcher matcher = new org.apache.shiro.util.AntPathMatcher();

    @Override
    public boolean matches(WebContext context) {
        for (String pattern : getExcludedPaths()) {
            if (matcher.match(pattern, context.getPath())) {
                return true;
            }
        }
        return super.matches(context);
    }

}