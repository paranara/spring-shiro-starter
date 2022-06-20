package org.paranora.ssoc.shiro.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * The type Restful response.
 *
 * @param <T> the type parameter
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestfulResponse<T> implements Serializable{

    private Boolean success;

    private String code;

    private String message;
    
    private T content;

    private Object exception;

    /**
     * Instantiates a new Restful response.
     */
    public RestfulResponse() {
        this(Boolean.TRUE, "操作成功");
    }

    /**
     * Instantiates a new Restful response.
     *
     * @param success the success
     */
    public RestfulResponse(Boolean success) {
        this(success, null);
    }

    /**
     * Instantiates a new Restful response.
     *
     * @param message the message
     */
    public RestfulResponse(String message) {
        this(Boolean.TRUE, "操作成功");
    }

    /**
     * Instantiates a new Restful response.
     *
     * @param success the success
     * @param message the message
     */
    public RestfulResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        if (this.message == null) {
            if (Boolean.FALSE.equals(success)) {
                this.message = "操作失败";
            }
            if (Boolean.TRUE.equals(success)) {
                this.message = "操作成功";
            }

        }
    }

    /**
     * Instantiates a new Restful response.
     *
     * @param success the success
     * @param code    the code
     * @param message the message
     */
    public RestfulResponse(Boolean success, String code, String message) {
        this(success,message);
        this.code=code;
    }

    /**
     * Instantiates a new Restful response.
     *
     * @param success   the success
     * @param code      the code
     * @param message   the message
     * @param exception the exception
     */
    public RestfulResponse(Boolean success, String code, String message, Object exception) {
        this(success,message);
        this.code=code;
        this.exception=exception;
    }

    /**
     * Instantiates a new Restful response.
     *
     * @param success the success
     * @param message the message
     * @param content the content
     */
    public RestfulResponse(Boolean success, String message, T content) {
        this.success = success;
        this.message = message;
        this.content = content;
        if (this.message == null) {
            if (Boolean.FALSE.equals(success)) {
                this.message = "操作失败";
            }
            if (Boolean.TRUE.equals(success)) {
                this.message = "操作成功";
            }

        }
    }


    /**
     * Fail restful response.
     *
     * @return the restful response
     */
    public static RestfulResponse fail() {
        return fail(null);
    }

    /**
     * Fail restful response.
     *
     * @param message the message
     * @return the restful response
     */
    public static RestfulResponse fail(String message) {
        return new RestfulResponse(Boolean.FALSE, message);
    }

    /**
     * Fail restful response.
     *
     * @param code    the code
     * @param message the message
     * @return the restful response
     */
    public static RestfulResponse fail(String code, String message) {
        return new RestfulResponse(Boolean.FALSE,code, message);
    }

    /**
     * Fail restful response.
     *
     * @param code      the code
     * @param message   the message
     * @param exception the exception
     * @return the restful response
     */
    public static RestfulResponse fail(String code, String message, Object exception) {
        return new RestfulResponse(Boolean.FALSE,code, message,exception);
    }

    /**
     * Success restful response.
     *
     * @return the restful response
     */
    public static RestfulResponse success() {
        return success(null);
    }

    /**
     * Success restful response.
     *
     * @param message the message
     * @return the restful response
     */
    public static RestfulResponse success(String message) {
        return new RestfulResponse(Boolean.TRUE, message);
    }

    /**
     * Success restful response.
     *
     * @param message the message
     * @param content the content
     * @return the restful response
     */
    public static RestfulResponse success(String message,Object content){
        RestfulResponse res = RestfulResponse.success(message);
        res.setContent(content);
        return res;
    }

    /**
     * Success restful response.
     *
     * @param content the content
     * @return the restful response
     */
    public static RestfulResponse success(Object content){
    	RestfulResponse res = RestfulResponse.success();
    	res.setContent(content);
    	return res;
    }

    /**
     * Fail exception restful response.
     *
     * @param exception the exception
     * @return the restful response
     */
    public static RestfulResponse failException(Object exception){
    	RestfulResponse res = RestfulResponse.fail();
    	res.setException(exception);
    	return res;
    }


    /**
     * Gets success.
     *
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * Sets success.
     *
     * @param success the success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public Object getContent() {
		return content;
	}

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(T content) {
		this.content = content;
	}

    /**
     * Gets exception.
     *
     * @return the exception
     */
    public Object getException() {
		return exception;
	}

    /**
     * Sets exception.
     *
     * @param exception the exception
     */
    public void setException(Object exception) {
		this.exception = exception;
	}

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
		return code;
	}

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
		this.code = code;
	}

}
