package org.paranora.ssoc.pac4j.extractor;

import org.pac4j.core.credentials.extractor.HeaderExtractor;

/**
 * The type Basic header extractor.
 */
public class BasicHeaderExtractor extends HeaderExtractor {

    /**
     * Instantiates a new Basic header extractor.
     *
     * @param headerName   the header name
     * @param prefixHeader the prefix header
     */
    public BasicHeaderExtractor(final String headerName, final String prefixHeader) {
        super(headerName,prefixHeader);
    }
}
