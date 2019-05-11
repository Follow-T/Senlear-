package com.spring.boot.community.config;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.spring.boot.community.tools.JsonData;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
 
/**
 * 校验验证码
 */
public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
 
    private String processUrl;
 
   public CaptchaAuthenticationFilter(String defaultFilterProcessesUrl,String failureUrl) {
        super(defaultFilterProcessesUrl);
        this.processUrl = defaultFilterProcessesUrl;
       
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res=(HttpServletResponse)response;
        if(processUrl.equals(req.getServletPath()) && "POST".equalsIgnoreCase(req.getMethod())){
            String expect = req.getSession().getAttribute("checkcode").toString();
 
            //销毁验证码
            req.getSession().removeAttribute("checkcode");
            String callback=request.getParameter("callback");
            if (expect != null && !expect.toLowerCase().equals(req.getParameter("checkcode").toLowerCase())){
            	 PrintWriter out = response.getWriter();
	             out.write( new JsonData("201", "Fail", "CodeError", callback).toString());
	             out.flush();
	             out.close();
	             //unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException(new JsonData("201", "Fail", "CodeError", callback).toString()));
	             return;
            }
        }
        chain.doFilter(request, response);
    }
 
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) 
        throws AuthenticationException, IOException, ServletException {
        return null;
    }

}
