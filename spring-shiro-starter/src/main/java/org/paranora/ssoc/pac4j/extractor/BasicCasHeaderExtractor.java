package org.paranora.ssoc.pac4j.extractor;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.util.CommonHelper;

import java.util.Optional;

/**
 * The type Basic cas header extractor.
 */
public class BasicCasHeaderExtractor implements CredentialsExtractor<UsernamePasswordCredentials> {

    /**
     * The Username parameter.
     */
    protected final String usernameParameter;

    /**
     * The Password parameter.
     */
    protected final String passwordParameter;

    /**
     * Instantiates a new Basic cas header extractor.
     *
     * @param usernameParameter the username parameter
     * @param passwordParameter the password parameter
     */
    public BasicCasHeaderExtractor(final String usernameParameter, final String passwordParameter) {
        this.usernameParameter = usernameParameter;
        this.passwordParameter = passwordParameter;
    }

    /**
     * Gets username parameter.
     *
     * @return the username parameter
     */
    public String getUsernameParameter() {
        return usernameParameter;
    }

    /**
     * Gets password parameter.
     *
     * @return the password parameter
     */
    public String getPasswordParameter() {
        return passwordParameter;
    }

    @Override
    public Optional<UsernamePasswordCredentials> extract(WebContext context) {
        CommonHelper.assertNotBlank("usernameParameter", this.usernameParameter);
        CommonHelper.assertNotNull("passwordParameter", this.passwordParameter);

        final Optional<String> username = context.getRequestHeader(this.usernameParameter);
        final Optional<String> password = context.getRequestHeader(this.passwordParameter);
        if (!username.isPresent() || !password.isPresent()) {
            return null;
        }

        return Optional.of(new UsernamePasswordCredentials(username.get(), password.get()));
    }
}