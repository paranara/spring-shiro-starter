package org.paranora.ssoc.pac4j.profile;

import com.nimbusds.jwt.JWTClaimsSet;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.encryption.EncryptionConfiguration;
import org.pac4j.jwt.config.signature.SignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * The type Basic jwt generator.
 *
 * @param <U> the type parameter
 */
public class BasicJwtGenerator<U extends CommonProfile> extends JwtGenerator<U> {

    /**
     * The Expires time.
     */
    protected int expiresTime=30*60;

    /**
     * Instantiates a new Basic jwt generator.
     *
     * @param signatureConfiguration  the signature configuration
     * @param encryptionConfiguration the encryption configuration
     */
    public BasicJwtGenerator(final SignatureConfiguration signatureConfiguration, final EncryptionConfiguration encryptionConfiguration){
        super(signatureConfiguration,encryptionConfiguration);
    }

    /**
     * Create expiration time date.
     *
     * @param expiresTime the expires time
     * @return the date
     */
    protected Date createExpirationTime(int expiresTime){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND, expiresTime);
        Date expirationTime= c.getTime();
        setExpirationTime(expirationTime);
        return expirationTime;
    }

    /**
     * Create expiration time date.
     *
     * @return the date
     */
    protected Date createExpirationTime(){
        return createExpirationTime(expiresTime);
    }

    @Override
    protected JWTClaimsSet buildJwtClaimsSet(U profile) {
        final JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(profile.getTypedId())
                .issueTime(new Date())
                .expirationTime(createExpirationTime());
//                .issuer(profile.getUsername());

        // add attributes
        final Map<String, Object> attributes = profile.getAttributes();
        for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }
        builder.claim(INTERNAL_ROLES, profile.getRoles());
        builder.claim(INTERNAL_PERMISSIONS, profile.getPermissions());

        // claims
        return builder.build();
    }

    @Override
    public String generate(U profile) {
        return super.generate(profile);
    }

    @Override
    protected void verifyProfile(U profile) {
        super.verifyProfile(profile);
    }

    @Override
    public String generate(final Map<String, Object> claims) {
        // claims builder
        final JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        // add claims
        for (final Map.Entry<String, Object> entry : claims.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }

        builder.expirationTime(createExpirationTime());
        return internalGenerate(builder.build());
    }

    /**
     * Gets expires time.
     *
     * @return the expires time
     */
    public int getExpiresTime() {
        return expiresTime;
    }

    /**
     * Sets expires time.
     *
     * @param expiresTime the expires time
     */
    public void setExpiresTime(int expiresTime) {
        this.expiresTime = expiresTime;
    }

}
