package org.paranora.ssoc.shiro.session;

import io.buji.pac4j.context.ShiroSessionStore;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


/**
 * 该实现和io.buji.pac4j.context.ShiroSessionStore一致，但是修改了getTrackableSession和buildFromTrackableSession方法
 */
public class BasicSessionStore implements SessionStore<JEEContext> {

    private final static Logger logger = LoggerFactory.getLogger(ShiroSessionStore.class);

    public final static BasicSessionStore INSTANCE = new BasicSessionStore();

    /**
     * Get the Shiro session (do not create it if it does not exist).
     *
     * @param createSession create a session if requested
     * @return the Shiro session
     */
    protected Session getSession(final boolean createSession) {
        return SecurityUtils.getSubject().getSession(createSession);
    }

    @Override
    public String getOrCreateSessionId(final JEEContext context) {
        final Session session = getSession(false);
        if (session != null) {
            return session.getId().toString();
        }
        return null;
    }

    @Override
    public Optional<Object> get(final JEEContext context, final String key) {
        final Session session = getSession(false);
        if (session != null) {
            return Optional.of(session.getAttribute(key));
        }
        return null;
    }

    @Override
    public void set(final JEEContext context, final String key, final Object value) {
        final Session session = getSession(true);
        if (session != null) {
            try {
                session.setAttribute(key, value);
            } catch (final UnavailableSecurityManagerException e) {
                logger.warn("Should happen just once at startup in some specific case of Shiro Spring configuration", e);
            }
        }
    }

    @Override
    public boolean destroySession(final JEEContext context) {
        getSession(true).stop();
        return true;
    }

    @Override
    public Optional<Object> getTrackableSession(JEEContext context) {
        return Optional.of(getSession(true));
    }

    @Override
    public Optional<SessionStore<JEEContext>> buildFromTrackableSession(JEEContext context, Object trackableSession) {
        if (trackableSession != null) {
            return Optional.of(new BasicShiroSessionStore((Session) trackableSession));
        }
        return null;
    }

    @Override
    public boolean renewSession(JEEContext context) {
        return false;
    }

}
