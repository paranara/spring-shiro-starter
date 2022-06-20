package org.paranora.ssoc.pac4j.authenticator;

import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.credentials.authenticator.CasRestAuthenticator;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.Pac4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Arrays;


/**
 * The type Basic cas rest authenticator.
 */
public class BasicCasRestAuthenticator extends CasRestAuthenticator {

    private final static Logger logger = LoggerFactory.getLogger(BasicCasRestAuthenticator.class);

    /**
     * The Rest operations.
     */
    protected RestOperations restOperations;

    /**
     * Instantiates a new Basic cas rest authenticator.
     *
     * @param configuration the configuration
     */
    public BasicCasRestAuthenticator(final CasConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    /**
     * Instantiates a new Basic cas rest authenticator.
     *
     * @param configuration  the configuration
     * @param restOperations the rest operations
     */
    public BasicCasRestAuthenticator(final CasConfiguration configuration, final RestOperations restOperations) {
        super(configuration);
        this.configuration = configuration;
        this.restOperations = restOperations;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials, WebContext context) {
        if (null == restOperations) {
            super.validate(credentials, context);
            return;
        }
        if (credentials == null || credentials.getPassword() == null || credentials.getUsername() == null) {
            throw new TechnicalException("Credentials are required");
        }
        final String ticketGrantingTicketId = retrieveTicketGrantingTicket(credentials.getUsername(), credentials.getPassword(), context);
        if (CommonHelper.isNotBlank(ticketGrantingTicketId)) {
            credentials.setUserProfile(new CasRestProfile(ticketGrantingTicketId, credentials.getUsername()));
        }
    }

    /**
     * Retrieve ticket granting ticket string.
     *
     * @param username the username
     * @param password the password
     * @param context  the context
     * @return the string
     */
    public String retrieveTicketGrantingTicket(final String username, final String password, final WebContext context) {
        try {
            String url = this.configuration.computeFinalRestUrl(context);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap();
            body.put(Pac4jConstants.USERNAME, Arrays.asList(username));
            body.put(Pac4jConstants.PASSWORD, Arrays.asList(password));

            HttpEntity<String> entity = new HttpEntity(body, headers);
            final ResponseEntity<String> responseEntity = restOperations.postForEntity(url, entity, String.class);
            final URI locationHeader = responseEntity.getHeaders().getLocation();
            final int responseCode = responseEntity.getStatusCodeValue();

            if (null != locationHeader && null != locationHeader && responseCode == HttpConstants.CREATED) {
                return locationHeader.getPath().substring(locationHeader.getPath().lastIndexOf("/") + 1);
            }
            logger.debug("Ticket granting ticket request failed: " + locationHeader + " " + responseCode);
            return null;
        } catch (Exception e) {
            throw new TechnicalException(e);
        } finally {
        }
    }
}
