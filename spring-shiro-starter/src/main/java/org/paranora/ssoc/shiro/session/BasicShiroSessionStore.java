package org.paranora.ssoc.shiro.session;

import io.buji.pac4j.context.ShiroSessionStore;
import org.apache.shiro.session.Session;

/**
 * The type Basic shiro session store.
 */
public class BasicShiroSessionStore extends ShiroSessionStore {

    /**存储的TrackableSession，往后要操作时用这个session操作*/
    private Session session;

    /**
     * Instantiates a new Basic shiro session store.
     *
     * @param session the session
     */
    public BasicShiroSessionStore(Session session) {
        this.session = session;
    }

    @Override
    protected Session getSession(final boolean createSession) {
        return session;
    }
}
