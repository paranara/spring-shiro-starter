package org.paranora.ssoc.pac4j.engine;

import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.DefaultLogoutLogic;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.exception.http.NoContentAction;
import org.pac4j.core.exception.http.RedirectionAction;
import org.pac4j.core.exception.http.RedirectionActionHelper;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.Pac4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.pac4j.core.util.CommonHelper.assertNotBlank;
import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * The type Basic logout logic.
 *
 * @param <R> the type parameter
 * @param <C> the type parameter
 */
public class BasicLogoutLogic<R, C extends WebContext> extends DefaultLogoutLogic<R, C> {

    /**
     * The Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(BasicLogoutLogic.class);

    @Override
    public R perform(final C context, final Config config, final HttpActionAdapter<R, C> httpActionAdapter,
                     final String defaultUrl, final String inputLogoutUrlPattern, final Boolean inputLocalLogout,
                     final Boolean inputDestroySession, final Boolean inputCentralLogout) {

        logger.debug("=== LOGOUT ===");

        HttpAction action;
        try {

            // default values
            final String logoutUrlPattern;
            if (inputLogoutUrlPattern == null) {
                logoutUrlPattern = Pac4jConstants.DEFAULT_LOGOUT_URL_PATTERN_VALUE;
            } else {
                logoutUrlPattern = inputLogoutUrlPattern;
            }
            final boolean localLogout = inputLocalLogout == null || inputLocalLogout;
            final boolean destroySession = inputDestroySession != null && inputDestroySession;
            final boolean centralLogout = inputCentralLogout != null && inputCentralLogout;

            // checks
            assertNotNull("context", context);
            assertNotNull("config", config);
            assertNotNull("httpActionAdapter", httpActionAdapter);
            assertNotBlank(Pac4jConstants.LOGOUT_URL_PATTERN, logoutUrlPattern);
            final Clients configClients = config.getClients();
            assertNotNull("configClients", configClients);

            // logic
            final ProfileManager<UserProfile> manager = getProfileManager(context);
            manager.setConfig(config);
            final List<UserProfile> profiles = manager.getAll(true);

            // compute redirection URL
            final Optional<String> url = context.getRequestParameter(Pac4jConstants.URL);
            String redirectUrl = defaultUrl;
            if (url.isPresent() && Pattern.matches(logoutUrlPattern, url.get())) {
                redirectUrl = url.get();
            }
            logger.debug("redirectUrl: {}", redirectUrl);
            if (redirectUrl != null) {
                action = RedirectionActionHelper.buildRedirectUrlAction(context, redirectUrl);
            } else {
                action = NoContentAction.INSTANCE;
            }

            // local logout if requested or multiple profiles
            if (localLogout || profiles.size() > 1) {
                logger.debug("Performing application logout");
                manager.logout();
                if (destroySession) {
                    final SessionStore sessionStore = context.getSessionStore();
                    if (sessionStore != null) {
                        final boolean removed = sessionStore.destroySession(context);
                        if (!removed) {
                            logger.error("Unable to destroy the web session. The session store may not support this feature");
                        }
                    } else {
                        logger.error("No session store available for this web context");
                    }
                }
            }

            // central logout
            if (centralLogout) {
                logger.debug("Performing central logout");
                for (final UserProfile profile : profiles) {
                    logger.debug("Profile: {}", profile);
                    final String clientName = profile.getClientName();
                    if (clientName != null) {
                        final Optional<Client> client = configClients.findClient(clientName);
                        if (client.isPresent()) {
                            final String targetUrl;
                            if (redirectUrl != null && (redirectUrl.startsWith(HttpConstants.SCHEME_HTTP) ||
                                    redirectUrl.startsWith(HttpConstants.SCHEME_HTTPS))) {
                                targetUrl = redirectUrl;
                            } else {
                                targetUrl = null;
                            }
                            final Optional<RedirectionAction> logoutAction = client.get().getLogoutAction(context, profile, targetUrl);
                            logger.debug("Logout action: {}", logoutAction);
                            if (logoutAction.isPresent()) {
                                action = logoutAction.get();
                                break;
                            }
                        }
                    }
                }
            }

        } catch (final RuntimeException e) {
            return handleException(e, httpActionAdapter, context);
        }

        return httpActionAdapter.adapt(action, context);
    }
}
