package org.paranora.ssoc.cas.validation;

import org.jasig.cas.client.ssl.HttpURLConnectionFactory;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The interface Url based ticket validator.
 */
public interface UrlBasedTicketValidator {

    /**
     * The constant logger.
     */
    Logger logger = LoggerFactory.getLogger(UrlBasedTicketValidator.class);

    /**
     * Gets url connection factory.
     *
     * @return the url connection factory
     */
    HttpURLConnectionFactory getUrlConnectionFactory();

    /**
     * Construct verification url string.
     *
     * @param ticket     the ticket
     * @param serviceUrl the service url
     * @return the string
     */
    String constructVerificationUrl(final String ticket, final String serviceUrl);

    /**
     * Retrieve response string.
     *
     * @param validationUrl the validation url
     * @param ticket        the ticket
     * @return the string
     */
    String retrieveResponse(URL validationUrl, String ticket);

    /**
     * Parse response assertion.
     *
     * @param response the response
     * @return the assertion
     * @throws TicketValidationException the ticket validation exception
     */
    Assertion parseResponse(final String response) throws TicketValidationException;

    /**
     * Verify assertion.
     *
     * @param ticket  the ticket
     * @param service the service
     * @return the assertion
     * @throws TicketValidationException the ticket validation exception
     */
    default Assertion verify(final String ticket, final String service) throws TicketValidationException {
        final String validationUrl = constructVerificationUrl(ticket, service);
        logger.debug("Constructing validation url: {}", validationUrl);

        try {
            logger.debug("Retrieving response from server.");
            final String serverResponse = retrieveResponse(new URL(validationUrl), ticket);

            if (serverResponse == null) {
                throw new TicketValidationException("The CAS server returned no response.");
            }

            logger.debug("Server response: {}", serverResponse);

            return parseResponse(serverResponse);
        } catch (final MalformedURLException e) {
            throw new TicketValidationException(e);
        }
    }
}
