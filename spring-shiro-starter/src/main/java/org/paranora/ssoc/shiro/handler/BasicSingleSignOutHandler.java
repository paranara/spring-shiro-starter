package org.paranora.ssoc.shiro.handler;

import io.buji.pac4j.context.ShiroSessionStore;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.LogoutLogic;
import org.pac4j.core.http.adapter.JEEHttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

/**
 * The type Basic single sign out handler.
 */
public class BasicSingleSignOutHandler {

    private final static int DECOMPRESSION_FACTOR = 10;

    /** Logger instance */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Mapping of token IDs and session IDs to HTTP sessions */
    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();

    /** The name of the artifact parameter.  This is used to capture the session identifier. */
    private String artifactParameterName = Protocol.CAS2.getArtifactParameterName();

    /** Parameter name that stores logout request for SLO */
    private String logoutParameterName = ConfigurationKeys.LOGOUT_PARAMETER_NAME.getDefaultValue();

    /** Parameter name that stores the state of the CAS server webflow for the callback */
    private String relayStateParameterName = ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getDefaultValue();

    /** The prefix url of the CAS server */
    private String casServerUrlPrefix = "";

    /** The logout callback path configured at the CAS server, if there is one */
    private String logoutCallbackPath;

    private boolean artifactParameterOverPost = false;

    private boolean eagerlyCreateSessions = true;

    private List<String> safeParameters;

    private final BasicSingleSignOutHandler.LogoutStrategy logoutStrategy = isServlet30() ? new BasicSingleSignOutHandler.Servlet30LogoutStrategy() : new BasicSingleSignOutHandler.Servlet25LogoutStrategy();

    /**
     * Sets session mapping storage.
     *
     * @param storage the storage
     */
    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        this.sessionMappingStorage = storage;
    }

    /**
     * Sets artifact parameter over post.
     *
     * @param artifactParameterOverPost the artifact parameter over post
     */
    public void setArtifactParameterOverPost(final boolean artifactParameterOverPost) {
        this.artifactParameterOverPost = artifactParameterOverPost;
    }

    /**
     * Gets session mapping storage.
     *
     * @return the session mapping storage
     */
    public SessionMappingStorage getSessionMappingStorage() {
        return this.sessionMappingStorage;
    }

    /**
     * Sets artifact parameter name.
     *
     * @param name Name of the authentication token parameter.
     */
    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    /**
     * Sets logout parameter name.
     *
     * @param name Name of parameter containing CAS logout request message for SLO.
     */
    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }

    /**
     * Sets cas server url prefix.
     *
     * @param casServerUrlPrefix The prefix url of the CAS server.
     */
    public void setCasServerUrlPrefix(final String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    /**
     * Sets logout callback path.
     *
     * @param logoutCallbackPath The logout callback path configured at the CAS server.
     */
    public void setLogoutCallbackPath(String logoutCallbackPath) {
        this.logoutCallbackPath = logoutCallbackPath;
    }

    /**
     * Sets relay state parameter name.
     *
     * @param name Name of parameter containing the state of the CAS server webflow.
     */
    public void setRelayStateParameterName(final String name) {
        this.relayStateParameterName = name;
    }

    /**
     * Sets eagerly create sessions.
     *
     * @param eagerlyCreateSessions the eagerly create sessions
     */
    public void setEagerlyCreateSessions(final boolean eagerlyCreateSessions) {
        this.eagerlyCreateSessions = eagerlyCreateSessions;
    }

    /**
     * Initializes the component for use.
     */
    public synchronized void init() {
        if (this.safeParameters == null) {
            CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
            CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
            CommonUtils.assertNotNull(this.sessionMappingStorage, "sessionMappingStorage cannot be null.");
            CommonUtils.assertNotNull(this.relayStateParameterName, "relayStateParameterName cannot be null.");
            CommonUtils.assertNotNull(this.casServerUrlPrefix, "casServerUrlPrefix cannot be null.");

            if (CommonUtils.isBlank(this.casServerUrlPrefix)) {
                logger.warn("Front Channel single sign out redirects are disabled when the 'casServerUrlPrefix' value is not set.");
            }

            if (this.artifactParameterOverPost) {
                this.safeParameters = Arrays.asList(this.logoutParameterName, this.artifactParameterName);
            } else {
                this.safeParameters = Collections.singletonList(this.logoutParameterName);
            }
        }
    }

    /**
     * Determines whether the given request contains an authentication token.
     *
     * @param request HTTP reqest.
     *
     * @return True if request contains authentication token, false otherwise.
     */
    private boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName,
                this.safeParameters));
    }

    /**
     * Determines whether the given request is a CAS  logout request.
     *
     * @param request HTTP request.
     *
     * @return True if request is logout request, false otherwise.
     */
    private boolean isLogoutRequest(final HttpServletRequest request) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            return !isMultipartRequest(request)
                    && pathEligibleForLogout(request)
                    && CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName,
                    this.safeParameters));
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName, this.safeParameters));
        }
        return false;
    }

    private boolean pathEligibleForLogout(HttpServletRequest request) {
        return logoutCallbackPath == null || logoutCallbackPath.equals(getPath(request));
    }

    private String getPath(HttpServletRequest request) {
        return request.getServletPath() + CommonUtils.nullToEmpty(request.getPathInfo());
    }

    /**
     * Process a request regarding the SLO process: record the session or destroy it.
     *
     * @param request     the incoming HTTP request.
     * @param response    the HTTP response.
     * @param config      the config
     * @param logoutLogic the logout logic
     * @return if the request should continue to be processed.
     */
    public boolean process(final HttpServletRequest request, final HttpServletResponse response,final Config config,final LogoutLogic<Object, JEEContext> logoutLogic) {
        if (isTokenRequest(request)) {
            logger.trace("Received a token request");
            recordSession(request);
            return true;
        }

        if (isLogoutRequest(request)) {
            logger.trace("Received a logout request");
            destroySession(request,response,config,logoutLogic);
            return false;
        }
        logger.trace("Ignoring URI for logout: {}", request.getRequestURI());
        return true;
    }

    /**
     * Associates a token request with the current HTTP session by recording the mapping
     * in the the configured {@link SessionMappingStorage} container.
     *
     * @param request HTTP request containing an authentication token.
     */
    private void recordSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(this.eagerlyCreateSessions);

        if (session == null) {
            logger.debug("No session currently exists (and none created).  Cannot record session information for single sign out.");
            return;
        }

        final String token = CommonUtils.safeGetParameter(request, this.artifactParameterName, this.safeParameters);
        logger.debug("Recording session for token {}", token);

        try {
            this.sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid. Nothing we can do!
        }
        sessionMappingStorage.addSessionById(token, session);
    }

    /**
     * Uncompress a logout message (base64 + deflate).
     *
     * @param originalMessage the original logout message.
     * @return the uncompressed logout message.
     */
    private String uncompressLogoutMessage(final String originalMessage) {
        final byte[] binaryMessage = DatatypeConverter.parseBase64Binary(originalMessage);

        Inflater decompresser = null;
        try {
            // decompress the bytes
            decompresser = new Inflater();
            decompresser.setInput(binaryMessage);
            final byte[] result = new byte[binaryMessage.length * DECOMPRESSION_FACTOR];

            final int resultLength = decompresser.inflate(result);

            // decode the bytes into a String
            return new String(result, 0, resultLength, "UTF-8");
        } catch (final Exception e) {
            logger.error("Unable to decompress logout message", e);
            throw new RuntimeException(e);
        } finally {
            if (decompresser != null) {
                decompresser.end();
            }
        }
    }

    /**
     * Destroys the current HTTP session for the given CAS logout request.
     *
     * @param request     HTTP request containing a CAS logout message.
     * @param response    the response
     * @param config      the config
     * @param logoutLogic the logout logic
     */
    protected void destroySession(final HttpServletRequest request,final HttpServletResponse response,final Config config,final LogoutLogic<Object, JEEContext> logoutLogic) {
        String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName, this.safeParameters);
        if (CommonUtils.isBlank(logoutMessage)) {
            logger.error("Could not locate logout message of the request from {}", this.logoutParameterName);
            return;
        }

        if (!logoutMessage.contains("SessionIndex")) {
            logoutMessage = uncompressLogoutMessage(logoutMessage);
        }

        logger.trace("Logout request:\n{}", logoutMessage);
        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
        if (CommonUtils.isNotBlank(token)) {
            final HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);

            if (session != null) {
                final String sessionID = session.getId();
                logger.debug("Invalidating session [{}] for token [{}]", sessionID, token);

                try {
                    session.invalidate();
                } catch (final IllegalStateException e) {
                    logger.debug("Error invalidating session.", e);
                }
                this.logoutStrategy.logout(request);
            }
        }

        final SessionStore<JEEContext> sessionStore = config.getSessionStore();
        final JEEContext context = new JEEContext(request, response, sessionStore != null ? sessionStore : ShiroSessionStore.INSTANCE);
        logoutLogic.perform(context,config, JEEHttpActionAdapter.INSTANCE,null,null,true,true,true);
    }

    private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }

    private static boolean isServlet30() {
        try {
            return HttpServletRequest.class.getMethod("logout") != null;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }


    /**
     * Abstracts the ways we can force logout with the Servlet spec.
     */
    private interface LogoutStrategy {

        /**
         * Logout.
         *
         * @param request the request
         */
        void logout(HttpServletRequest request);
    }

    private class Servlet25LogoutStrategy implements BasicSingleSignOutHandler.LogoutStrategy {

        public void logout(final HttpServletRequest request) {
            // nothing additional to do here
        }
    }

    private class Servlet30LogoutStrategy implements BasicSingleSignOutHandler.LogoutStrategy {

        public void logout(final HttpServletRequest request) {
            try {
                request.logout();
            } catch (final ServletException e) {
                logger.debug("Error performing request.logout.");
            }
        }
    }
}
