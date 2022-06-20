package org.paranora.ssoc.pac4j.context;

import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.SessionStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Basic web context.
 */
public class BasicWebContext extends JEEContext {


    /**
     * Instantiates a new Basic web context.
     *
     * @param request  the request
     * @param response the response
     */
    public BasicWebContext(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        settingResponse(response);
    }

    /**
     * Instantiates a new Basic web context.
     *
     * @param request      the request
     * @param response     the response
     * @param sessionStore the session store
     */
    public BasicWebContext(HttpServletRequest request, HttpServletResponse response, SessionStore<JEEContext> sessionStore) {
        super(request, response, sessionStore);
        settingResponse(response);
    }

    /**
     * Setting response.
     *
     * @param response the response
     */
    protected void settingResponse(HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
    }
}
