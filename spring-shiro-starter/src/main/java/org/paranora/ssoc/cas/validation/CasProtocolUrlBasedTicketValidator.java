package org.paranora.ssoc.cas.validation;

import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * The interface Cas protocol url based ticket validator.
 */
public interface CasProtocolUrlBasedTicketValidator extends UrlBasedTicketValidator {

    /**
     * The constant logger.
     */
    Logger logger = LoggerFactory.getLogger(CasProtocolUrlBasedTicketValidator.class);

    /**
     * Gets rest operations.
     *
     * @return the rest operations
     */
    RestOperations getRestOperations();

    default String retrieveResponse(URL validationUrl, String ticket) {
        RestOperations restOperations = getRestOperations();
        if (null == restOperations) {
            return CommonUtils.getResponseFromServer(validationUrl, getUrlConnectionFactory(), "UTF-8");
        } else {
            try {
                ResponseEntity<String> responseEntity = restOperations.getForEntity(validationUrl.toURI(), String.class);
                return responseEntity.getBody();
            } catch (URISyntaxException e) {
                logger.error("uri trans error: {}",e.getMessage());
                throw new RuntimeException(e);
            } catch (final RuntimeException e) {
                throw e;
            } catch (final Exception e) {
                throw new RuntimeException(e);
            } finally {
            }
        }
    }

}
