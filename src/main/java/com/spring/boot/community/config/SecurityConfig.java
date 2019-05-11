package com.spring.boot.community.config;

import com.spring.boot.community.qqfilter.QQAuthenticationFilter;
import com.spring.boot.community.qqfilter.QQAuthenticationManager;
import com.spring.boot.community.tools.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String KEY = "waylau.com";
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Bean  
    public PasswordEncoder passwordEncoder() {  
        return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
    }  
	
	@Bean  
    public AuthenticationProvider authenticationProvider() {  
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder); // 设置密码加密方式
        return authenticationProvider;  
    }  
 
	/**
	 * 自定义配�?
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new CaptchaAuthenticationFilter("/login","/login-error"), UsernamePasswordAuthenticationFilter.class);
		http.authorizeRequests().antMatchers("/**").permitAll() 
				.and()
				.formLogin()   //基于 Form 表单登录验证
				.loginPage("/login").successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						RequestCache cache = new HttpSessionRequestCache();
						SavedRequest savedRequest = cache.getRequest(request, response);
						String url="";
						if(savedRequest==null){
							url="";
						}else {
							url = savedRequest.getRedirectUrl();
						}
						String callback=request.getParameter("callback");
						response.setContentType("application/json;charset=utf-8");
		                PrintWriter out = response.getWriter();
		                out.write( new JsonData("200", "Success", url, callback).toString());
		                out.flush();
		                out.close();
					}
					}).failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						String callback=request.getParameter("callback");
						response.setContentType("application/json;charset=utf-8");
		                PrintWriter out = response.getWriter();
		                out.write( new JsonData("201", "Fail", "LoginFail", callback).toString());
		                out.flush();
		                out.close();
					}
		        	
		        })
				//failureUrl("/login-error").successForwardUrl("/login-success") // 自定义登录界�?
				.and().rememberMe().key(KEY) // 启用 remember me
				.and().exceptionHandling().accessDeniedPage("/403");  // 处理异常，拒绝访问就重定向到 403 页面
		http.csrf().ignoringAntMatchers("/h2-console/**"); // 禁用 H2 控制台的 CSRF 防护
		http.headers().frameOptions().sameOrigin(); // 允许来自同一来源的H2 控制台的请求
		http.csrf().disable();//禁用crsf防护
		//用户退出登录，退出成功返回/index界面
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/index").invalidateHttpSession(true);
		http.addFilterAt(qqAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	
	/**
     * 自定义 QQ登录 过滤器
     */
    private QQAuthenticationFilter qqAuthenticationFilter(){
        QQAuthenticationFilter authenticationFilter = new QQAuthenticationFilter("/QQLogin");
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        successHandler.setDefaultTargetUrl("/index");
        authenticationFilter.setAuthenticationManager(new QQAuthenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return authenticationFilter;
    }
	/**
	 * 认证信息管理
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}
}
