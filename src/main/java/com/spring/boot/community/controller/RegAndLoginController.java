package com.spring.boot.community.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.community.domin.Authority;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.AuthorityService;
import com.spring.boot.community.service.EmailService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;



@RestController
public class RegAndLoginController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private AuthorityService authorityService;
	//发件人
	@Value("${spring.mail.username}")
	private String userName;
	
	
	@RequestMapping(value = "/toreg", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public String toreg(HttpServletRequest request,User user,String checkcode)
	{
		
		String codeSession = (String) request.getSession().getAttribute("checkcode");
		System.out.println(codeSession);
		System.out.println(checkcode);
		String callback=request.getParameter("callback");
	    if(codeSession.toLowerCase().equals(checkcode.toLowerCase()))
	    {
	    	
	    	JsonData jsonData;
	    	if(userService.findByUsername(user.getUsername())!=null) {
	    		jsonData=new JsonData("101", "该用户名已经被注册！", "123","");
	    		return jsonData.toString();
	    	} else{
	    		System.out.println(user.getName());
	    		jsonData=new JsonData("200", "Sucuess", "RegisterSucuess",callback);
	    		user.setEncodePassword(user.getPassword());
	    		List<Authority> authorities=new ArrayList<>();
	    		authorities.add(authorityService.getAuthorityById(1L));
	    		user.setAuthorities(authorities);
	    		userService.saveUser(user);
	    		System.out.println("Yes");
	    		userService.saveUser(user);
	    		request.getSession().removeAttribute("checkcode");
	    		System.out.println(request.getSession().getAttribute("checkcode"));
	    		return jsonData.toString();
	    	}
	    	
	    }
	    System.out.println("No");
	    JsonData jsonData=new JsonData("201", "Error", "CheckCodeError",callback);
		return jsonData.toString();	
	}
	
	@PostMapping("/sendemail")
	public String sendemail(HttpServletRequest request,String email,String type)
	{
		JsonData jsonData;
		System.out.println(type);
		String callback=request.getParameter("callback");
		if(type.equals("reg")) {
			emailService.sendSimpleTextMailActual("注册提示","您已成功注册为社区会员，欢迎您！",new String[]{email},
	    			null,null,null);
			jsonData=new JsonData("200", "Success", "SendEmailSuccess",callback);
		}else if(type.equals("updatePwd")){
			emailService.sendSimpleTextMailActual("密码变更提示","您已成功修改密码！",new String[]{email},
	    			null,null,null);
			jsonData=new JsonData("200", "Success", "SendEmailSuccess",callback);
		}else {
			jsonData=new JsonData("201", "Error", "SendEmailError",callback);
		}
		return jsonData.toString();
		
	}
	
}
