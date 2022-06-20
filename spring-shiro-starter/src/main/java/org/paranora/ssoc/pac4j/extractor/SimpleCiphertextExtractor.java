package org.paranora.ssoc.pac4j.extractor;

import org.paranora.ssoc.pac4j.credentials.SecretKeyCredentials;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The type Simple ciphertext extractor.
 */
public class SimpleCiphertextExtractor implements CredentialsExtractor<SecretKeyCredentials> {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String timestampName = "timestamp";
    private String signName = "sign";
    private String tokenName = "token";

    /**
     * The Secret key provider.
     */
    protected SecretKeyProvider secretKeyProvider;

    /**
     * Instantiates a new Simple ciphertext extractor.
     *
     * @param timestampName the timestamp name
     * @param signName      the sign name
     * @param tokenName     the token name
     */
    public SimpleCiphertextExtractor(String timestampName, String signName, String tokenName) {
        this.timestampName = timestampName;
        this.signName = signName;
        this.tokenName = tokenName;
    }

    /**
     * Instantiates a new Simple ciphertext extractor.
     *
     * @param secretKeyProvider the secret key provider
     */
    public SimpleCiphertextExtractor(SecretKeyProvider secretKeyProvider) {
        initSecretKeyProvider(secretKeyProvider);
    }

    /**
     * Instantiates a new Simple ciphertext extractor.
     *
     * @param timestampName     the timestamp name
     * @param signName          the sign name
     * @param tokenName         the token name
     * @param secretKeyProvider the secret key provider
     */
    public SimpleCiphertextExtractor(String timestampName, String signName, String tokenName,SecretKeyProvider secretKeyProvider) {
        this(timestampName,signName,tokenName);
        initSecretKeyProvider(secretKeyProvider);
    }

    /**
     * Instantiates a new Simple ciphertext extractor.
     */
    public SimpleCiphertextExtractor() {
        init();
    }

    /**
     * Init secret key provider.
     *
     * @param secretKeyProvider the secret key provider
     */
    protected void initSecretKeyProvider(SecretKeyProvider secretKeyProvider) {
        if (ObjectUtils.isEmpty(secretKeyProvider)) {
            defaultSecretKeyProvider();
        } else {
            this.secretKeyProvider = secretKeyProvider;
        }
    }

    /**
     * Default secret key provider.
     */
    protected void defaultSecretKeyProvider() {
        if (ObjectUtils.isEmpty(this.secretKeyProvider)) {
            this.secretKeyProvider = new SimpleSecretKeyProvider();
        }
    }

    /**
     * Init.
     */
    protected void init() {
        defaultSecretKeyProvider();
    }

    /**
     * Gets secret key.
     *
     * @param token the token
     * @return the secret key
     */
    protected String getSecretKey(String token) {
//        return DigestUtils.md5Hex(token + "[_Paranora_Is_King!!!_]");
        return this.secretKeyProvider.getSecretKey(token);
    }

    @Override
    public Optional<SecretKeyCredentials> extract(WebContext context) {
        CommonHelper.assertNotBlank("timestampName", this.timestampName);
        CommonHelper.assertNotBlank("signName", this.signName);
//        CommonHelper.assertNotBlank("tokenName", this.tokenName);

        Optional<String> timestamp = getValueFromRequest(context,this.timestampName);
        Optional<String> sign = getValueFromRequest(context,this.signName);
        Optional<String> token = getValueFromRequest(context,this.tokenName);

        if (!timestamp.isPresent() || StringUtils.isBlank(timestamp.get())
                || !sign.isPresent() || StringUtils.isBlank(sign.get())) {
            logger.error(String.format("timestamp : %s , sign : %s , token :%s , one or all is null!", timestamp, sign, token));
            return Optional.empty();
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        if (!pattern.matcher(timestamp.get()).matches()) {
            logger.error(String.format("timestamp : %s , value is error !", timestamp));
            return Optional.empty();
        }

        String secretKey=getSecretKey(token.get());
        if(StringUtils.isBlank(secretKey)){
            logger.error(String.format("secretKey is null!"));
            return Optional.empty();
        }

        SecretKeyCredentials credentials = new SecretKeyCredentials(token.get());
        credentials.setSign(sign.get());
        credentials.setTimestamp(Long.parseLong(timestamp.get()));
        credentials.setSecretKey(secretKey);

        return Optional.of(credentials);
    }

    /**
     * Get value from request optional.
     *
     * @param context    the context
     * @param requestKey the request key
     * @return the optional
     */
    protected Optional<String> getValueFromRequest(WebContext context,String requestKey){
        if(ObjectUtils.isEmpty(requestKey))return Optional.of("");
        return context.getRequestHeader(requestKey);
    }
}
