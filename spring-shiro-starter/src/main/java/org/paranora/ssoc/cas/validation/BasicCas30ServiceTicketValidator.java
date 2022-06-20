package org.paranora.ssoc.cas.validation;

import org.jasig.cas.client.ssl.HttpURLConnectionFactory;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.web.client.RestOperations;

/**
 * The type Basic cas 30 service ticket validator.
 */
public class BasicCas30ServiceTicketValidator extends Cas30ServiceTicketValidator implements CasProtocolUrlBasedTicketValidator {

    /**
     * The Rest operations.
     */
    protected RestOperations restOperations;

    /**
     * Instantiates a new Basic cas 30 service ticket validator.
     *
     * @param casServerUrlPrefix the cas server url prefix
     */
    public BasicCas30ServiceTicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    /**
     * Instantiates a new Basic cas 30 service ticket validator.
     *
     * @param casServerUrlPrefix the cas server url prefix
     * @param restOperations     the rest operations
     */
    public BasicCas30ServiceTicketValidator(String casServerUrlPrefix, RestOperations restOperations) {
        this(casServerUrlPrefix);
        this.restOperations=restOperations;
    }

    @Override
    public String constructVerificationUrl(String ticket, String serviceUrl) {
        return constructValidationUrl(ticket,serviceUrl);
    }

    @Override
    public Assertion parseResponse(String response) throws TicketValidationException {
        return parseResponseFromServer(response);
    }

    @Override
    public RestOperations getRestOperations() {
        return restOperations;
    }

    @Override
    public HttpURLConnectionFactory getUrlConnectionFactory() {
        return getURLConnectionFactory();
    }
}
