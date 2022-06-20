package org.paranora.ssoc.pac4j.engine;

import org.pac4j.core.client.Client;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;

import java.util.List;

/**
 * The type Basic rest stateless security logic.
 *
 * @param <R> the type parameter
 * @param <C> the type parameter
 */
public class BasicRestStatelessSecurityLogic<R, C extends WebContext> extends  BasicSecurityLogic<R, C> {

    @Override
    protected boolean startAuthentication(final C context, final List<Client<? extends Credentials>> currentClients) {
        return false;
    }
}
