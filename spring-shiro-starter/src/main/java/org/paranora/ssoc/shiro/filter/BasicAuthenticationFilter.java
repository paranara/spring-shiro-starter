package org.paranora.ssoc.shiro.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.paranora.ssoc.shiro.vo.RestfulResponse;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The type Basic authentication filter.
 */
public class BasicAuthenticationFilter extends BasicHttpAuthenticationFilter {

    protected ObjectMapper objectMapper;

    public BasicAuthenticationFilter(){
        this.objectMapper=new ObjectMapper();
    }

    /**
     * Write response content.
     *
     * @param response the response
     * @param content  the content
     */
    protected void writeResponseContent(ServletResponse response, String content) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            out.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void writeResponseContent(ServletResponse response, RestfulResponse content) {
        try {
            String json=this.objectMapper.writeValueAsString(content);
            writeResponseContent(response,json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        boolean state= super.preHandle(request, response);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return state;
    }

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        boolean loggedIn= super.sendChallenge(request, response);
        writeResponseContent(response,RestfulResponse.fail("508", "rest authentication error!"));
        return loggedIn;
    }
}
