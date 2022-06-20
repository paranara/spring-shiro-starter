package org.paranora.ssoc.shiro.session;

import com.google.common.collect.Lists;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * The type Basic cache session dao.
 */
public class BasicCacheSessionDAO extends EnterpriseCacheSessionDAO implements BasicSessionDAO {

    private static final Logger logger = LoggerFactory.getLogger(BasicCacheSessionDAO.class);

    private BasicSessionDAO sessionDAO;

    /**
     * Gets session dao.
     *
     * @return the session dao
     */
    public BasicSessionDAO getSessionDAO() {
        return sessionDAO;
    }

    /**
     * Sets session dao.
     *
     * @param sessionDAO the session dao
     */
    public void setSessionDAO(BasicSessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    /**
     * 重写CachingSessionDAO中readSession方法，如果Session中没有登陆信息就调用doReadSession方法从Redis中重读
     * session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null 代表没有登录，登录后Shiro会放入该值
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session session = getCachedSession(sessionId);
        if (session == null || session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
            session = this.doReadSession(sessionId);
            if (session == null) {
                throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
            } else {
                // 缓存
                cache(session, session.getId());
            }
        }
        return session;
    }

    /**
     * 根据会话ID获取会话
     *
     * @param sessionId 会话ID
     * @return ShiroSession
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        logger.debug("begin doReadSession {} ", sessionId);
        Session session = null;
        try {
            session = sessionDAO.readSession(sessionId);
            if(null!=session){
                sessionDAO.refresh(session.getId());
                logger.info("session : {} is refresh !",sessionId);
            }
        } catch (Exception e) {
            logger.warn("doReadSession session error!", e);
        }
        return session;
    }

    /**
     * 如DefaultSessionManager在创建完session后会调用该方法；
     * 如保存到关系数据库/文件系统/NoSQL数据库；即可以实现会话的持久化；
     * 返回会话ID；主要此处返回的ID.equals(session.getId())；
     */
    @Override
    protected Serializable doCreate(Session session) {
        // 创建一个Id并设置给Session
        Serializable sessionId = this.generateSessionId(session);
        assignSessionId(session, sessionId);
        try {
            sessionDAO.create(session);
            logger.info("sessionId {} name {} created!", sessionId, session.getClass().getName());
        } catch (Exception e) {
            logger.warn("create session error!", e);
        }
        return sessionId;
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    @Override
    protected void doUpdate(Session session) {
        //如果会话过期/停止 没必要再更新了
        try {
            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
                return;
            }
        } catch (Exception e) {
            logger.error("ValidatingSession error");
        }
        try {
            if (session instanceof BasicSession) {
                // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
                BasicSession ss = (BasicSession) session;
                if (!ss.isChanged()) {
                    return;
                }
                ss.setChanged(false);
                ss.setLastAccessTime(new Date());

                sessionDAO.update(session);
                logger.debug("sessionId {} name {} updated!", session.getId(), session.getClass().getName());
            } else {
                logger.debug("sessionId {} name {} update error!", session.getId(), session.getClass().getName());
            }
        } catch (Exception e) {
            logger.warn("update Session error!", e);
        }
    }

    /**
     * 删除会话；当会话过期/会话停止（如用户退出时）会调用
     */
    @Override
    public void doDelete(Session session) {
        logger.debug("begin doDelete {} ", session);
        try {
            sessionDAO.delete(session);
            this.uncache(session.getId());
            logger.debug("shiro session id {} deleted!", session.getId());
        } catch (Exception e) {
            logger.warn("delete Session error!", e);
        }
    }

    /**
     * 删除cache中缓存的Session
     *
     * @param sessionId the session id
     */
    public void uncache(Serializable sessionId) {
        try {
            Session session = super.getCachedSession(sessionId);
            super.uncache(session);
            logger.debug("delete local cache Session id {} error!", sessionId);
        } catch (Exception e) {
        	logger.error("uncache error", e);
        }
    }

    /**
     * 获取当前所有活跃用户，如果用户量多此方法影响性能
     */
    @Override
    public Collection<Session> getActiveSessions() {
        return Lists.newArrayList();
    }

    /**
     * 返回本机Ehcache中Session
     *
     * @return the eh cache active sessions
     */
    public Collection<Session> getEhCacheActiveSessions() {
        return super.getActiveSessions();
    }

    @Override
    public void refresh(Serializable id) {
        sessionDAO.refresh(id);
    }

}
