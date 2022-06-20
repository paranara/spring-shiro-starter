package org.paranora.ssoc.pac4j.cas.config;

import org.jasig.cas.client.validation.TicketValidator;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.context.WebContext;

/**
 * The type Basic cas configuration.
 */
public class BasicCasConfiguration extends CasConfiguration {

    @Override
    public TicketValidator retrieveTicketValidator(WebContext context) {
        TicketValidator ticketValidator = super.retrieveTicketValidator(context);
        setDefaultTicketValidator(ticketValidator);
        return ticketValidator;
    }
}
