package org.paranora.ssoc.shiro.subject;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * The type Basic stateless subject factory.
 */
public class BasicStatelessSubjectFactory extends DefaultWebSubjectFactory {

    /**
     * Instantiates a new Basic stateless subject factory.
     */
    public BasicStatelessSubjectFactory() {

    }

    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(Boolean.FALSE);
        return super.createSubject(context);
    }
}
