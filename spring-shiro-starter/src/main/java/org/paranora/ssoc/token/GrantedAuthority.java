package org.paranora.ssoc.token;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {

    String getAuthority();
}
