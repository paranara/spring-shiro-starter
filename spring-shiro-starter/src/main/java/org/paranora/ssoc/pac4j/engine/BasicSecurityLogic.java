package org.paranora.ssoc.pac4j.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.paranora.ssoc.shiro.vo.RestfulResponse;
import org.pac4j.core.client.Client;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.exception.http.OkAction;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.profile.UserProfile;

import java.util.List;

/**
 * The type Basic security logic.
 *
 * @param <R> the type parameter
 * @param <C> the type parameter
 */
public class BasicSecurityLogic<R, C extends WebContext> extends DefaultSecurityLogic<R, C> {

    protected ObjectMapper objectMapper;

    public BasicSecurityLogic(){
        this.objectMapper=new ObjectMapper();
    }

    @Override
    protected HttpAction unauthorized(C context, List<Client<? extends Credentials>> currentClients) {
        if (context instanceof JEEContext) {
            return new OkAction(responseToJson(RestfulResponse.fail("507", "unauthorized !!!")));
        } else {
            return super.unauthorized(context, currentClients);
        }
    }

    @Override
    protected R handleException(Exception e, HttpActionAdapter<R, C> httpActionAdapter, C context) {
        String error = "server error !!!";
        HttpAction ok = new OkAction(responseToJson(RestfulResponse.fail("506", error)));
        return httpActionAdapter.adapt(ok, context);
    }

    @Override
    protected HttpAction forbidden(C context, List<Client<? extends Credentials>> currentClients, List<UserProfile> profiles, String authorizers) {
        if (context instanceof JEEContext) {
            return new OkAction(responseToJson(RestfulResponse.fail("507", "forbidden !!!")));
        } else {
            return super.forbidden(context, currentClients, profiles, authorizers);
        }
    }

    /**
     * Response to json string.
     *
     * @param restfulResponse the restful response
     * @return the string
     */
    protected String responseToJson(RestfulResponse restfulResponse){
        try {
            return objectMapper.writeValueAsString(restfulResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected RuntimeException runtimeException(Exception exception) {
        return super.runtimeException(exception);
    }

}
