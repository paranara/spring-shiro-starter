package org.paranora.ssoc.shiro.subject;

import io.buji.pac4j.subject.Pac4jSubjectFactory;
import io.buji.pac4j.token.Pac4jToken;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.subject.WebSubjectContext;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The type Basic web subject factory.
 */
public class BasicWebSubjectFactory extends Pac4jSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {

        boolean authenticated = context.isAuthenticated();

        if (authenticated) {

            AuthenticationToken token = context.getAuthenticationToken();

            if (token != null && token instanceof Pac4jToken) {
                final Pac4jToken clientToken = (Pac4jToken) token;
                if (clientToken.isRememberMe()) {
                    context.setAuthenticated(false);
                }
            }
        }

        return doCreateSubject(context);
    }

    /**
     * Do create subject subject.
     *
     * @param context the context
     * @return the subject
     */
    protected Subject doCreateSubject(SubjectContext context){
        if (!(context instanceof WebSubjectContext)) {
            return super.createSubject(context);
        } else {
            WebSubjectContext wsc = (WebSubjectContext)context;
            SecurityManager securityManager = wsc.resolveSecurityManager();
            Session session = wsc.resolveSession();
            boolean sessionEnabled = wsc.isSessionCreationEnabled();
            PrincipalCollection principals = wsc.resolvePrincipals();
            boolean authenticated = wsc.resolveAuthenticated();
            String host = wsc.resolveHost();
            ServletRequest request = wsc.resolveServletRequest();
            ServletResponse response = wsc.resolveServletResponse();

            Subject subject=new BasicWebSubject(principals, authenticated, host, session, sessionEnabled, request, response, securityManager);

            return subject;
        }
    }
}
