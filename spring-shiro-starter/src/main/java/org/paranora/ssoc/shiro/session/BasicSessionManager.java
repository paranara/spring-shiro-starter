package org.paranora.ssoc.shiro.session;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class BasicSessionManager extends DefaultWebSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(BasicSessionManager.class);

    public BasicSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return super.getSessionId(request, response);
    }
}