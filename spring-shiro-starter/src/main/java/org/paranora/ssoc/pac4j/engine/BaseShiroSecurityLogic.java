package org.paranora.ssoc.pac4j.engine;

import org.pac4j.core.context.WebContext;

/**
 * The type Base shiro security logic.
 *
 * @param <R> the type parameter
 * @param <C> the type parameter
 */
public class BaseShiroSecurityLogic<R, C extends WebContext> extends BasicSecurityLogic<R,C> {

    /**
     * Instantiates a new Base shiro security logic.
     */
    public BaseShiroSecurityLogic(){
        setProfileManagerFactory(io.buji.pac4j.profile.ShiroProfileManager::new);
    }
}
