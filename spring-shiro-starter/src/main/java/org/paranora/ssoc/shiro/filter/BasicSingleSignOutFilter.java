package org.paranora.ssoc.shiro.filter;

import org.paranora.ssoc.pac4j.engine.BasicLogoutLogic;
import org.paranora.ssoc.shiro.handler.BasicSingleSignOutHandler;
import io.buji.pac4j.profile.ShiroProfileManager;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.engine.LogoutLogic;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type Basic single sign out filter.
 */
public class BasicSingleSignOutFilter extends AbstractConfigurationFilter {

    private static final BasicSingleSignOutHandler HANDLER = new BasicSingleSignOutHandler();

    private Config config;

    private LogoutLogic<Object, JEEContext> logoutLogic;

    private AtomicBoolean handlerInitialized = new AtomicBoolean(false);

    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        if (!isIgnoreInitConfiguration()) {
            setArtifactParameterName(getString(ConfigurationKeys.ARTIFACT_PARAMETER_NAME));
            setLogoutParameterName(getString(ConfigurationKeys.LOGOUT_PARAMETER_NAME));
            setRelayStateParameterName(getString(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME));
            setCasServerUrlPrefix(getString(ConfigurationKeys.CAS_SERVER_URL_PREFIX));
            setLogoutCallbackPath(getString(ConfigurationKeys.LOGOUT_CALLBACK_PATH));
            HANDLER.setArtifactParameterOverPost(getBoolean(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST));
            HANDLER.setEagerlyCreateSessions(getBoolean(ConfigurationKeys.EAGERLY_CREATE_SESSIONS));
        }
        HANDLER.init();
        handlerInitialized.set(true);

        logoutLogic = new BasicLogoutLogic<>();
        ((BasicLogoutLogic<Object, JEEContext>) logoutLogic).setProfileManagerFactory(ShiroProfileManager::new);
    }

    /**
     * Sets artifact parameter name.
     *
     * @param name the name
     */
    public void setArtifactParameterName(final String name) {
        HANDLER.setArtifactParameterName(name);
    }

    /**
     * Sets logout parameter name.
     *
     * @param name the name
     */
    public void setLogoutParameterName(final String name) {
        HANDLER.setLogoutParameterName(name);
    }

    /**
     * Sets relay state parameter name.
     *
     * @param name the name
     */
    public void setRelayStateParameterName(final String name) {
        HANDLER.setRelayStateParameterName(name);
    }

    /**
     * Sets cas server url prefix.
     *
     * @param casServerUrlPrefix the cas server url prefix
     */
    public void setCasServerUrlPrefix(final String casServerUrlPrefix) {
        HANDLER.setCasServerUrlPrefix(casServerUrlPrefix);
    }

    /**
     * Sets logout callback path.
     *
     * @param logoutCallbackPath the logout callback path
     */
    public void setLogoutCallbackPath(String logoutCallbackPath) {
        HANDLER.setLogoutCallbackPath(logoutCallbackPath);
    }

    /**
     * Sets session mapping storage.
     *
     * @param storage the storage
     */
    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        HANDLER.setSessionMappingStorage(storage);
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * <p>Workaround for now for the fact that Spring Security will fail since it doesn't call {@link #init(javax.servlet.FilterConfig)}.</p>
         * <p>Ultimately we need to allow deployers to actually inject their fully-initialized {@link org.jasig.cas.client.session.SingleSignOutHandler}.</p>
         */
        if (!this.handlerInitialized.getAndSet(true)) {
            HANDLER.init();
        }

        if (HANDLER.process(request, response,config,logoutLogic)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
        // nothing to do
    }

    /**
     * Gets single sign out handler.
     *
     * @return the single sign out handler
     */
    protected static BasicSingleSignOutHandler getSingleSignOutHandler() {
        return HANDLER;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Sets config.
     *
     * @param config the config
     */
    public void setConfig(Config config) {
        this.config = config;
    }
}
