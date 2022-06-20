package org.paranora.ssoc.shiro.session;

import org.apache.shiro.session.mgt.eis.SessionDAO;

import java.io.Serializable;

/**
 * The interface Basic session dao.
 */
public interface BasicSessionDAO extends SessionDAO {
    /**
     * Refresh.
     *
     * @param id the id
     */
    void refresh(Serializable id);
}
