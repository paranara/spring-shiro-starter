package org.paranora.ssoc.pac4j.extractor;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * The type Simple secret key provider.
 */
public class SimpleSecretKeyProvider implements SecretKeyProvider{
    @Override
    public String getSecretKey(String token) {
        return DigestUtils.md5Hex(token + "[_Paranora_Is_King!!!_]");
    }
}
