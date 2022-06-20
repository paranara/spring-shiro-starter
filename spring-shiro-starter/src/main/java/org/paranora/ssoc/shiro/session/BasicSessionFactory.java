package org.paranora.ssoc.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * The type Basic session factory.
 */
public class BasicSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext initData) {

        if (initData != null) {
            String host = initData.getHost();
            if (host != null) {
                return new BasicSession(host);
            }
        }

        return new BasicSession();
    }
}
