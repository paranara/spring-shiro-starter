package org.paranora.ssoc.pac4j.client;

import org.paranora.ssoc.cas.validation.UrlBasedTicketValidator;
import org.paranora.ssoc.pac4j.authenticator.BasicCasRestAuthenticator;
import org.paranora.ssoc.pac4j.extractor.BasicCasHeaderExtractor;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.util.Pac4jConstants;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

/**
 * The type Basic cas rest form client.
 */
public class BasicCasRestFormClient extends CasRestFormClient {

    /**
     * The Rest operations.
     */
    protected RestOperations restOperations;

    /**
     * Instantiates a new Basic cas rest form client.
     */
    public BasicCasRestFormClient() {
        defaultCredentialsExtractor(new BasicCasHeaderExtractor(Pac4jConstants.USERNAME, Pac4jConstants.PASSWORD));
    }

    /**
     * Instantiates a new Basic cas rest form client.
     *
     * @param configuration     the configuration
     * @param usernameParameter the username parameter
     * @param passwordParameter the password parameter
     */
    public BasicCasRestFormClient(final CasConfiguration configuration, final String usernameParameter, final String passwordParameter) {
        super(configuration, usernameParameter, passwordParameter);
        defaultAuthenticator(new BasicCasRestAuthenticator(configuration));
        defaultCredentialsExtractor(new BasicCasHeaderExtractor(usernameParameter, passwordParameter));
    }

    /**
     * Instantiates a new Basic cas rest form client.
     *
     * @param configuration     the configuration
     * @param usernameParameter the username parameter
     * @param passwordParameter the password parameter
     * @param restOperations    the rest operations
     */
    public BasicCasRestFormClient(final CasConfiguration configuration, final String usernameParameter, final String passwordParameter, final RestOperations restOperations) {
        super(configuration, usernameParameter, passwordParameter);
        defaulRestOperations(restOperations);
        defaultAuthenticator(new BasicCasRestAuthenticator(configuration, restOperations));
        defaultCredentialsExtractor(new BasicCasHeaderExtractor(usernameParameter, passwordParameter));
    }

    /**
     * Defaul rest operations.
     *
     * @param restOperations the rest operations
     */
    public void defaulRestOperations(final RestOperations restOperations) {
        if (this.restOperations == null) {
            this.restOperations = restOperations;
        }
    }

    /**
     * Request service ticket token credentials.
     *
     * @param profile the profile
     * @param context the context
     * @return the token credentials
     */
    public TokenCredentials requestServiceTicket(final CasRestProfile profile, final WebContext context) {
        return requestServiceTicket(configuration.getPrefixUrl(), profile, context);
    }

    @Override
    public TokenCredentials requestServiceTicket(final String serviceURL, final CasRestProfile profile, final WebContext context) {
        if (null == restOperations) {
            return super.requestServiceTicket(serviceURL, profile, context);
        }
        try {
            final URL endpointURL = new URL(configuration.computeFinalRestUrl(context));
            final URL ticketURL = new URL(endpointURL, endpointURL.getPath() + "/" + profile.getTicketGrantingTicketId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap();
            body.put("service", Arrays.asList(serviceURL));

            HttpEntity<String> entity = new HttpEntity(body, headers);
            ResponseEntity<String> responseEntity = restOperations.postForEntity(ticketURL.toURI(), entity, String.class);

            final int responseCode = responseEntity.getStatusCodeValue();
            if (responseCode == HttpConstants.OK) {
                return new TokenCredentials(responseEntity.getBody());
            }
            throw new TechnicalException("Service ticket request for `" + profile + "` failed. ");
        } catch (final Exception e) {
            throw new TechnicalException(e);
        } finally {
        }
    }

    /**
     * Validate service ticket cas profile.
     *
     * @param ticket  the ticket
     * @param context the context
     * @return the cas profile
     */
    public CasProfile validateServiceTicket(final TokenCredentials ticket, final WebContext context) {
        return validateServiceTicket(getConfiguration().getPrefixUrl(), ticket, context);
    }

    @Override
    public CasProfile validateServiceTicket(final String serviceURL, final TokenCredentials ticket, final WebContext context) {
        try {
            final Assertion assertion = retrieveServiceTicket(serviceURL, ticket, context);
            final CasProfile casProfile = retrieveCasProfile(assertion);
            return casProfile;
        } catch (final TicketValidationException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     * Retrieve ticket validator ticket validator.
     *
     * @param context the context
     * @return the ticket validator
     */
    protected TicketValidator retrieveTicketValidator(final WebContext context) {
        return configuration.retrieveTicketValidator(context);
    }

    /**
     * Retrieve service ticket assertion.
     *
     * @param serviceURL the service url
     * @param ticket     the ticket
     * @param context    the context
     * @return the assertion
     * @throws TicketValidationException the ticket validation exception
     */
    protected Assertion retrieveServiceTicket(final String serviceURL, final TokenCredentials ticket, final WebContext context) throws TicketValidationException {
        try {
            final TicketValidator ticketValidator = retrieveTicketValidator(context);
            if (ticketValidator instanceof UrlBasedTicketValidator) {
                return ((UrlBasedTicketValidator) ticketValidator).verify(ticket.getToken(), serviceURL);
            } else {
                return ticketValidator.validate(ticket.getToken(), serviceURL);
            }
        } catch (final TicketValidationException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     * Retrieve cas profile cas profile.
     *
     * @param assertion the assertion
     * @return the cas profile
     */
    protected CasProfile retrieveCasProfile(Assertion assertion) {
        final AttributePrincipal principal = assertion.getPrincipal();
        final CasProfile casProfile = new CasProfile();
        casProfile.setId(ProfileHelper.sanitizeIdentifier(casProfile, principal.getName()));
        casProfile.addAttributes(principal.getAttributes());
        return casProfile;
    }

    @Override
    public void destroyTicketGrantingTicket(final CasRestProfile profile, final WebContext context) {
        if (null == restOperations) {
            super.destroyTicketGrantingTicket(profile, context);
            return;
        }
        try {
            final URL endpointURL = new URL(configuration.computeFinalRestUrl(context));
            final URL deleteURL = new URL(endpointURL, endpointURL.getPath() + "/" + profile.getTicketGrantingTicketId());

            final int responseCode = restOperations.execute(deleteURL.toURI(), HttpMethod.DELETE, null, new ResponseExtractor<Integer>() {
                @Override
                public Integer extractData(ClientHttpResponse clientHttpResponse) throws IOException {
                    return clientHttpResponse.getRawStatusCode();
                }
            });

            if (responseCode != HttpConstants.OK) {
                throw new TechnicalException("TGT delete request for `" + profile + "` failed. ");
            }
        } catch (URISyntaxException e) {
            throw new TechnicalException(e);
        } catch (final IOException e) {
            throw new TechnicalException(e);
        } finally {
        }
    }

    @Override
    public void setConfiguration(CasConfiguration configuration) {
        super.setConfiguration(configuration);
        defaultAuthenticator(new BasicCasRestAuthenticator(configuration));
    }

    /**
     * Gets rest operations.
     *
     * @return the rest operations
     */
    public RestOperations getRestOperations() {
        return restOperations;
    }

    /**
     * Sets rest operations.
     *
     * @param restOperations the rest operations
     */
    public void setRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

}
