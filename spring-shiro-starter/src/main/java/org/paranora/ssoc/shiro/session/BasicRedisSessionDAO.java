package org.paranora.ssoc.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * The type Basic redis session dao.
 */
public class BasicRedisSessionDAO implements BasicSessionDAO {

    private static final String REDIS_SHIRO_SESSION_KEY_PREFIX_DEFAULT = "PARANORA-REDIS-SESSION:";
    private static final int REDIS_SESSION_VAL_TIME_SPAN_DEFAULT = 1800000;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int sessionTimeout = REDIS_SESSION_VAL_TIME_SPAN_DEFAULT;
    private String sessionKeyPrefix = REDIS_SHIRO_SESSION_KEY_PREFIX_DEFAULT;
    private RedisTemplate<String, Session> redisTemplate;

    /**
     * Gets session key prefix.
     *
     * @return the session key prefix
     */
    public String getSessionKeyPrefix() {
        return sessionKeyPrefix;
    }

    /**
     * Sets session key prefix.
     *
     * @param sessionKeyPrefix the session key prefix
     */
    public void setSessionKeyPrefix(String sessionKeyPrefix) {
        this.sessionKeyPrefix = sessionKeyPrefix;
    }

    /**
     * Gets redis template.
     *
     * @return the redis template
     */
    public RedisTemplate<String, Session> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * Sets redis template.
     *
     * @param redisTemplate the redis template
     */
    public void setRedisTemplate(RedisTemplate<String, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Gets session timeout.
     *
     * @return the session timeout
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Sets session timeout.
     *
     * @param sessionTimeout the session timeout
     */
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }


    public void refresh(Serializable sessionId) {
        getRedisTemplate().expire(prepareSessionKey(sessionId), sessionTimeout, TimeUnit.MILLISECONDS);
        logger.debug("refresh session id: {}, timeout: {}", sessionId, sessionTimeout);
    }

    @Override
    public Serializable create(Session session) {
        if (session == null || session.getId() == null)
            throw new NullPointerException("session is empty");
        try {
            getRedisTemplate().opsForValue().set(prepareSessionKey(session.getId()), session, sessionTimeout, TimeUnit.MILLISECONDS);
            logger.debug("create session id: {}, timeout: {}", session.getId(), sessionTimeout);
            return session.getId();
        } catch (Exception e) {
            logger.error("create Session Error", e);
        }
        return null;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        if (sessionId == null) {
            throw new NullPointerException("session id is empty");
        }
        Session session = null;
        try {
            session = getRedisTemplate().boundValueOps(prepareSessionKey(sessionId)).get();
            logger.debug("get session id: {}", sessionId);
        } catch (Exception e) {
            logger.error("get Session Error", e);
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null)
            throw new NullPointerException("session is empty");
        try {
            getRedisTemplate().boundValueOps(prepareSessionKey(session.getId())).set(session, sessionTimeout, TimeUnit.MILLISECONDS);
            logger.debug("update session id: {}, timeout: {}", session.getId(), sessionTimeout);

        } catch (Exception e) {
            logger.error("update Session Error", e);
        }
    }

    @Override
    public void delete(Session session) {
        if (null==session || null==session.getId()) {
            throw new NullPointerException("session id is empty");
        }
        try {
            Serializable sessionId=session.getId();
            getRedisTemplate().delete(prepareSessionKey(sessionId));
            logger.debug("delete session id: {}, timeout: {}", sessionId, sessionTimeout);

        } catch (Exception e) {
            logger.error("delete Session Error", e);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        logger.debug("get all sessions");
        return null;
    }

    /**
     * Prepare session key string.
     *
     * @param sessionId the session id
     * @return the string
     */
    protected String prepareSessionKey(Serializable sessionId) {
        return sessionKeyPrefix + sessionId;
    }
}
