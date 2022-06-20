package org.paranora.ssoc.pac4j.authenticator;

import org.paranora.ssoc.pac4j.credentials.SecretKeyCredentials;
import org.paranora.ssoc.pac4j.profile.SimpleCiphertextProfile;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.profile.definition.CommonProfileDefinition;
import org.pac4j.core.profile.definition.ProfileDefinitionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The type Simple secret key authenticator.
 */
public class SimpleSecretKeyAuthenticator extends ProfileDefinitionAware<SimpleCiphertextProfile> implements Authenticator<SecretKeyCredentials> {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private long signTimeRange = 10 * 60 * 1000;


    /**
     * Instantiates a new Simple secret key authenticator.
     */
    public SimpleSecretKeyAuthenticator() {

    }

    /**
     * Instantiates a new Simple secret key authenticator.
     *
     * @param signTimeRange the sign time range
     */
    public SimpleSecretKeyAuthenticator(long signTimeRange) {
        this.signTimeRange = signTimeRange;

    }

    @Override
    public void validate(SecretKeyCredentials credentials, WebContext context) {
        init();
        try {
            long currentTimestamp = currentTime();
            if (credentials.getTimestamp() <= 0) {
                throw new CredentialsException(String.format("error timestamp : %s , value is error ! ", credentials.getTimestamp()));
            }
            if (credentials.getTimestamp() > currentTimestamp && credentials.getTimestamp() - currentTimestamp > signTimeRange()) {
                throw new CredentialsException(String.format("error timestamp : %s, are you from the future? you are time old man!!! ", credentials.getTimestamp()));
            }
            if ((currentTimestamp - credentials.getTimestamp()) >= signTimeRange()) {
                throw new CredentialsException(String.format("error timestamp %s , time is overdue! ", credentials.getTimestamp()));
            }

            if (StringUtils.isBlank(credentials.getSign())) {
                throw new CredentialsException(String.format("sign: %s , token : %s , value is null !!! ", credentials.getSign(), credentials.getToken()));
            }

            String createSign = sign(credentials);
            logger.debug(String.format("create sign : %s, input sign: %s ", credentials.getSign(), createSign));
            if (!createSign.equalsIgnoreCase(credentials.getSign())) {
                throw new CredentialsException(String.format("sign: %s , validate is error !!! ", credentials.getSign()));
            }

            SimpleCiphertextProfile simpleCiphertextProfile = new SimpleCiphertextProfile();
            simpleCiphertextProfile.addAttribute("token", credentials.getToken());
            simpleCiphertextProfile.addAttribute("timestamp", credentials.getTimestamp());
            simpleCiphertextProfile.addAttribute("sign", credentials.getSign());
            credentials.setUserProfile(simpleCiphertextProfile);
        } catch (Exception e) {
            throw new CredentialsException("SecretKeyAuthenticator validate error", e);
        }
    }

    /**
     * Current time long.
     *
     * @return the long
     */
    protected long currentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Sign time range long.
     *
     * @return the long
     */
    protected long signTimeRange() {
        return this.signTimeRange;
    }

    /**
     * Sign string.
     *
     * @param credentials the credentials
     * @return the string
     */
    protected String sign(SecretKeyCredentials credentials) {
        String secretStr = serializeCredentials(credentials);
        return DigestUtils.md5Hex(secretStr);
    }

    /**
     * Serialize credentials string.
     *
     * @param credentials the credentials
     * @return the string
     */
    protected String serializeCredentials(SecretKeyCredentials credentials) {
        StringBuilder secretStr = new StringBuilder();
        secretStr.append(credentials.getToken())
                .append(credentials.getTimestamp())
                .append(credentials.getSecretKey());
        return secretStr.toString();
    }

    @Override
    protected void internalInit() {
        defaultProfileDefinition(new CommonProfileDefinition<>(x -> new SimpleCiphertextProfile()));
    }
}
