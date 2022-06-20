package org.paranora.ssoc.shiro.filter;

import org.paranora.ssoc.pac4j.engine.BaseShiroSecurityLogic;
import io.buji.pac4j.context.ShiroSessionStore;
import org.apache.shiro.SecurityUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.http.adapter.JEEHttpActionAdapter;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

/**
 * 不登录也可访问的连接配置该过滤器
 */
public class BasicCasFilter implements Filter {

    private SecurityLogic<Object, JEEContext> securityLogic;

    private Config config;

    private String clients;

    private String authorizers;

    private String matchers;

    private Boolean multiProfile;

    /**
     * Instantiates a new Basic cas filter.
     */
    public BasicCasFilter() {
        securityLogic = new BaseShiroSecurityLogic<>();
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws IOException, ServletException {
        assertNotNull("securityLogic", securityLogic);
        assertNotNull("config", config);

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final SessionStore<JEEContext> sessionStore = config.getSessionStore();
        final JEEContext context = new JEEContext(request, response,
                sessionStore != null ? sessionStore : ShiroSessionStore.INSTANCE);
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            Collection<Cookie> cookies = context.getRequestCookies();
            Optional<Cookie> fid = cookies.stream().filter(cookie -> "fid".equals(cookie.getName())).findFirst();
            if (fid.isPresent() && !StringUtils.isEmpty(fid.get().getValue())) {
                // 在其他项目中已经登录、跳去登录验证；
                securityLogic.perform(context, config, (ctx, profiles, parameters) -> {

                    filterChain.doFilter(request, response);
                    return null;

                }, JEEHttpActionAdapter.INSTANCE, clients, authorizers, matchers, multiProfile);
            }
        }
        // 不登录也能访问的页面
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    /**
     * Gets security logic.
     *
     * @return the security logic
     */
    public SecurityLogic<Object, JEEContext> getSecurityLogic() {
        return securityLogic;
    }

    /**
     * Sets security logic.
     *
     * @param securityLogic the security logic
     */
    public void setSecurityLogic(final SecurityLogic<Object, JEEContext> securityLogic) {
        this.securityLogic = securityLogic;
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
    public void setConfig(final Config config) {
        this.config = config;
    }

    /**
     * Gets clients.
     *
     * @return the clients
     */
    public String getClients() {
        return clients;
    }

    /**
     * Sets clients.
     *
     * @param clients the clients
     */
    public void setClients(final String clients) {
        this.clients = clients;
    }

    /**
     * Gets authorizers.
     *
     * @return the authorizers
     */
    public String getAuthorizers() {
        return authorizers;
    }

    /**
     * Sets authorizers.
     *
     * @param authorizers the authorizers
     */
    public void setAuthorizers(final String authorizers) {
        this.authorizers = authorizers;
    }

    /**
     * Gets matchers.
     *
     * @return the matchers
     */
    public String getMatchers() {
        return matchers;
    }

    /**
     * Sets matchers.
     *
     * @param matchers the matchers
     */
    public void setMatchers(final String matchers) {
        this.matchers = matchers;
    }

    /**
     * Gets multi profile.
     *
     * @return the multi profile
     */
    public Boolean getMultiProfile() {
        return multiProfile;
    }

    /**
     * Sets multi profile.
     *
     * @param multiProfile the multi profile
     */
    public void setMultiProfile(final Boolean multiProfile) {
        this.multiProfile = multiProfile;
    }

}
