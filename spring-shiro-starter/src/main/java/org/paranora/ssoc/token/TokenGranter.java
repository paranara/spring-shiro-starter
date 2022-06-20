package org.paranora.ssoc.token;


public interface TokenGranter {
    AccessToken grant(Authentication authentication);
}
