package org.paranora.ssoc.pac4j.engine;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.engine.DefaultCallbackLogic;

/**
 * The type Base shiro callback logic.
 *
 * @param <R> the type parameter
 * @param <C> the type parameter
 */
public class BaseShiroCallbackLogic<R, C extends WebContext> extends DefaultCallbackLogic<R, C> {
    /**
     * Instantiates a new Base shiro callback logic.
     */
    public BaseShiroCallbackLogic() {
        setProfileManagerFactory(io.buji.pac4j.profile.ShiroProfileManager::new);
    }
}
