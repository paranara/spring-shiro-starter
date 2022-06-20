package org.paranora.ssoc.shiro.session;


/**
 * 由于SimpleSession lastAccessTime更改后也会调用SessionDao update方法，
 * 增加标识位，如果只是更新lastAccessTime SessionDao update方法直接返回
 * <p>
 * Session Attribute
 * DefaultSubjectContext.PRINCIPALS_SESSION_KEY 保存 principal
 * DefaultSubjectContext.AUTHENTICATED_SESSION_KEY 保存 boolean是否登陆
 *
 * @see org.apache.shiro.subject.support.DefaultSubjectContext
 */
public class BasicSession extends BasicSessionAbstract {

    /**
     * Instantiates a new Basic session.
     */
    public BasicSession() {
        super();
        this.setChanged(true);
    }

    /**
     * Instantiates a new Basic session.
     *
     * @param host the host
     */
    public BasicSession(String host) {
        super(host);
        this.setChanged(true);
    }

    /**
     * Clear attributes.
     */
    public void clearAttributes(){
    	super.getAttributes().clear();
    }

}
