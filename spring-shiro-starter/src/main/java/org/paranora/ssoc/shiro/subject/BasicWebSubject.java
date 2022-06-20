package org.paranora.ssoc.shiro.subject;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The type Basic web subject.
 */
public class BasicWebSubject extends WebDelegatingSubject {

    /**
     * Instantiates a new Basic web subject.
     *
     * @param principals      the principals
     * @param authenticated   the authenticated
     * @param host            the host
     * @param session         the session
     * @param request         the request
     * @param response        the response
     * @param securityManager the security manager
     */
    public BasicWebSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, ServletRequest request, ServletResponse response, SecurityManager securityManager) {
        this(principals, authenticated, host, session, true, request, response, securityManager);
    }

    /**
     * Instantiates a new Basic web subject.
     *
     * @param principals      the principals
     * @param authenticated   the authenticated
     * @param host            the host
     * @param session         the session
     * @param sessionEnabled  the session enabled
     * @param request         the request
     * @param response        the response
     * @param securityManager the security manager
     */
    public BasicWebSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, boolean sessionEnabled, ServletRequest request, ServletResponse response, SecurityManager securityManager) {
        super(principals, authenticated, host, session, sessionEnabled, request, response, securityManager);
    }
}
