package org.paranora.ssoc.shiro.session;

import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The type Basic user session.
 */
public class BasicUserSession extends SimpleSession implements Cloneable{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 7624884432830922018L;

    /**
     * The constant SESSION_USER.
     */
    public static final String SESSION_USER = "SESSION_USER";


    private String id;

    private String username;

    private String userCode;

    private String sex;

    private String email;

    private String mobile;

    private String avatar;

    private String ssoSessionId;


    /**
     * Instantiates a new Basic user session.
     */
    public BasicUserSession(){
    }

    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets user code.
     *
     * @return the user code
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * Sets user code.
     *
     * @param userCode the user code
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * Gets sex.
     *
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets sex.
     *
     * @param sex the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile.
     *
     * @param mobile the mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets avatar.
     *
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets avatar.
     *
     * @param avatar the avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Gets sso session id.
     *
     * @return the sso session id
     */
    public String getSsoSessionId() {
        return ssoSessionId;
    }

    /**
     * Sets sso session id.
     *
     * @param ssoSessionId the sso session id
     */
    public void setSsoSessionId(String ssoSessionId) {
        this.ssoSessionId = ssoSessionId;
    }

    public BasicUserSession clone() {
        BasicUserSession obj = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(this);

            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = (BasicUserSession) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Simple User Session Clone error：",e);
        }
        return obj;
    }

}