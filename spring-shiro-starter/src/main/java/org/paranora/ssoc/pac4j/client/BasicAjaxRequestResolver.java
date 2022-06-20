package org.paranora.ssoc.pac4j.client;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.*;
import org.pac4j.core.http.ajax.DefaultAjaxRequestResolver;
import org.pac4j.core.redirect.RedirectionActionBuilder;
import org.pac4j.core.util.CommonHelper;

/**
 * The type Basic ajax request resolver.
 */
public class BasicAjaxRequestResolver extends DefaultAjaxRequestResolver {

    @Override
    public HttpAction buildAjaxResponse(WebContext context, RedirectionActionBuilder redirectionActionBuilder) {
        String url = null;
        if (isAddRedirectionUrlAsHeader()) {
            final RedirectionAction action = redirectionActionBuilder.getRedirectionAction(context).orElse(null);
            if (action instanceof WithLocationAction) {
                url = ((WithLocationAction) action).getLocation();
            }
        }

        if (!context.getRequestParameter(FACES_PARTIAL_AJAX_PARAMETER).isPresent()) {
            if (CommonHelper.isNotBlank(url)) {
                context.setResponseHeader(HttpConstants.LOCATION_HEADER, url);
            }
            throw UnauthorizedAction.INSTANCE;
        }

        final StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        buffer.append("\"success\":false,");
        buffer.append("\"code\":401,");
        buffer.append("\"message\":\"unauthorized!\",");
        buffer.append("\"content\":\"" + url.replaceAll("&", "&amp;") + "\"");
        buffer.append("}");

        return RedirectionActionHelper.buildFormPostContentAction(context, buffer.toString());
    }
}
