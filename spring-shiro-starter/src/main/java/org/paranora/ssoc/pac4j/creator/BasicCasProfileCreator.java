package org.paranora.ssoc.pac4j.creator;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.profile.creator.ProfileCreator;
import org.pac4j.core.util.HttpUtils;
import org.pac4j.core.util.Pac4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * The type Basic cas profile creator.
 *
 * @param <C> the type parameter
 * @param <P> the type parameter
 */
public class BasicCasProfileCreator<C extends Credentials, P extends CommonProfile> implements ProfileCreator<C> {

    /**
     * The Configuration.
     */
    protected CasConfiguration configuration;

    /**
     * The Service url.
     */
    protected String serviceUrl;

    private final static Logger logger = LoggerFactory.getLogger(BasicCasProfileCreator.class);

    @Override
    public Optional<UserProfile> create(C credentials, WebContext context) {
        CasRestProfile casRestProfile = (CasRestProfile) credentials.getUserProfile();
        TokenCredentials tokenCredentials = requestServiceTicket(serviceUrl, casRestProfile, context);
        CasProfile casProfile = validateServiceTicket(serviceUrl, tokenCredentials, context);
        return Optional.of(casProfile);
    }


    /**
     * Request ticket granting ticket string.
     *
     * @param username the username
     * @param password the password
     * @param context  the context
     * @return the string
     */
    protected String requestTicketGrantingTicket(final String username, final String password, final WebContext context) {
        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.openPostConnection(new URL(this.configuration.computeFinalRestUrl(context)));
            final String payload = HttpUtils.encodeQueryParam(Pac4jConstants.USERNAME, username)
                    + "&" + HttpUtils.encodeQueryParam(Pac4jConstants.PASSWORD, password);

            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            out.write(payload);
            out.close();

            final String locationHeader = connection.getHeaderField("location");
            final int responseCode = connection.getResponseCode();
            if (locationHeader != null && responseCode == HttpConstants.CREATED) {
                return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
            }

            logger.debug("Ticket granting ticket request failed: " + locationHeader + " " + responseCode +
                    HttpUtils.buildHttpErrorMessage(connection));

            return null;
        } catch (final IOException e) {
            throw new TechnicalException(e);
        } finally {
            HttpUtils.closeConnection(connection);
        }
    }

    /**
     * Request service ticket token credentials.
     *
     * @param serviceURL the service url
     * @param profile    the profile
     * @param context    the context
     * @return the token credentials
     */
    public TokenCredentials requestServiceTicket(final String serviceURL, final CasRestProfile profile, final WebContext context) {
        HttpURLConnection connection = null;
        try {
            final URL endpointURL = new URL(configuration.computeFinalRestUrl(context));
            final URL ticketURL = new URL(endpointURL, endpointURL.getPath() + "/" + profile.getTicketGrantingTicketId());

            connection = HttpUtils.openPostConnection(ticketURL);
            final String payload = HttpUtils.encodeQueryParam("service", serviceURL);

            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            out.write(payload);
            out.close();

            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpConstants.OK) {
                try (final BufferedReader in =
                             new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return new TokenCredentials(in.readLine());
                }
            }
            throw new TechnicalException("Service ticket request for `" + profile + "` failed: " +
                    HttpUtils.buildHttpErrorMessage(connection));
        } catch (final IOException e) {
            throw new TechnicalException(e);
        } finally {
            HttpUtils.closeConnection(connection);
        }
    }

    /**
     * Validate service ticket cas profile.
     *
     * @param serviceURL the service url
     * @param ticket     the ticket
     * @param context    the context
     * @return the cas profile
     */
    public CasProfile validateServiceTicket(final String serviceURL, final TokenCredentials ticket, final WebContext context) {
        try {
            final Assertion assertion = configuration.retrieveTicketValidator(context).validate(ticket.getToken(), serviceURL);
            final AttributePrincipal principal = assertion.getPrincipal();
            final CasProfile casProfile = new CasProfile();
            casProfile.setId(ProfileHelper.sanitizeIdentifier(casProfile, principal.getName()));
            casProfile.addAttributes(principal.getAttributes());
            return casProfile;
        } catch (final TicketValidationException e) {
            throw new TechnicalException(e);
        }
    }


    /**
     * Gets configuration.
     *
     * @return the configuration
     */
    public CasConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets configuration.
     *
     * @param configuration the configuration
     */
    public void setConfiguration(CasConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Gets service url.
     *
     * @return the service url
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Sets service url.
     *
     * @param serviceUrl the service url
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
