package org.paranora.ssoc.shiro.session;

import org.apache.shiro.session.mgt.SimpleSession;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * 由于SimpleSession lastAccessTime更改后也会调用SessionDao update方法，
 * 增加标识位，如果只是更新lastAccessTime SessionDao update方法直接返回
 *
 * Session Attribute
 * DefaultSubjectContext.PRINCIPALS_SESSION_KEY 保存 principal
 * DefaultSubjectContext.AUTHENTICATED_SESSION_KEY 保存 boolean是否登陆
 * @see org.apache.shiro.subject.support.DefaultSubjectContext
 */
public abstract class BasicSessionAbstract extends SimpleSession implements Serializable {

	//发Last Access Time改变时间超过最少Touch周期时, 变更为isChange=true
	private final static int MIN_TOUCH_PERIOD = 60000;
	
	// 除lastAccessTime以外其他字段发生改变时为true
    private boolean isChanged;

    public BasicSessionAbstract() {
        super();
        this.setChanged(true);
    }
    public BasicSessionAbstract(String host) {
        super(host);
        this.setChanged(true);
    }


    @Override
    public void setId(Serializable id) {
        super.setId(id);
        this.setChanged(true);
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        super.setStopTimestamp(stopTimestamp);
        this.setChanged(true);
    }

    @Override
    public void setExpired(boolean expired) {
        super.setExpired(expired);
        this.setChanged(true);
    }

    @Override
    public void setTimeout(long timeout) {
        super.setTimeout(timeout);
        this.setChanged(true);
    }

    @Override
    public void setHost(String host) {
        super.setHost(host);
        this.setChanged(true);
    }

    @Override
    public void setAttributes(Map<Object, Object> attributes) {
        super.setAttributes(attributes);
        this.setChanged(true);
    }

    @Override
    public void setLastAccessTime(Date lastAccessTime) {
        if(getLastAccessTime()!=null ){
            long last = getLastAccessTime().getTime();
            long now = lastAccessTime.getTime();

            //如果MIN_TOUCH_PERIOD(60000) ms内访问 则不更新session,否则需要更新远端过期时间
            if( (now - last)  >= MIN_TOUCH_PERIOD ){
                this.setChanged(true);
                super.setLastAccessTime(lastAccessTime);
            }
        }
    }

    /**
     * 防止过于频繁的保存
     */
    @Override
    public void setAttribute(Object key, Object value) {
    	if(key.equals(BasicUserSession.SESSION_USER)){
    		super.setAttribute(key, value);
            this.setChanged(true);
    	}else{
    		Object obj = this.getAttribute(key);
    		if (obj != null && obj.equals(value)) {
    			return;
    		}
    		super.setAttribute(key, value);
    		this.setChanged(true);
    	}
    }

    @Override
    public Object removeAttribute(Object key) {
        this.setChanged(true);
        return super.removeAttribute(key);
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        super.stop();
        this.setChanged(true);
    }

    /**
     * 设置过期
     */
    @Override
    protected void expire() {
        this.stop();
        this.setExpired(true);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected boolean onEquals(SimpleSession ss) {
        return super.onEquals(ss);	
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }
    @Override
    public void touch() {
    	this.setLastAccessTime(new Date());
   }
}
