package org.paranora.ssoc.shiro.filter;

import io.buji.pac4j.context.ShiroSessionStore;
import io.buji.pac4j.filter.LogoutFilter;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.http.adapter.JEEHttpActionAdapter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * The type Basic logout filter.
 */
public class BasicLogoutFilter extends LogoutFilter {

    private Boolean destroySession;

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        assertNotNull("logoutLogic", getLogoutLogic());
        assertNotNull("config", getConfig());

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final SessionStore<JEEContext> sessionStore = getConfig().getSessionStore();
        final JEEContext context = new JEEContext(request, response, sessionStore != null ? sessionStore : ShiroSessionStore.INSTANCE);

        getLogoutLogic().perform(context, getConfig(), JEEHttpActionAdapter.INSTANCE, getDefaultUrl(), getLogoutUrlPattern(), getLocalLogout(), getDestroySession(), getCentralLogout());
    }

    /**
     * Gets destroy session.
     *
     * @return the destroy session
     */
    public Boolean getDestroySession() {
        return destroySession;
    }

    /**
     * Sets destroy session.
     *
     * @param destroySession the destroy session
     */
    public void setDestroySession(Boolean destroySession) {
        this.destroySession = destroySession;
    }
}
