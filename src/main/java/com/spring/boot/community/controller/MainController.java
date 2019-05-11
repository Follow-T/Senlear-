package com.spring.boot.community.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ArticleService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.CaptchaUtil;



@Controller
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class MainController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/")
	public String ToIndex() {
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
	public ModelAndView index(Model model) {
		Boolean Login=false;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			Login=true;
		}
		if(Login==true)
		{
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			model.addAttribute("user", user);
		}
		
		Pageable p=PageRequest.of(0, 10);
		Page<Article> page0=articleService.listArticlesByStatus(1, p);
		Page<Article> page1=articleService.listArticleByTypeAndStatusSortByHot(1, 1, p);
		Page<Article> page2=articleService.listArticleByTypeAndStatusSortByHot(2, 1, p);
		Page<Article> page3=articleService.listArticleByTypeAndStatusSortByHot(3, 1, p);
		Page<Article> page4=articleService.listArticleByTypeAndStatusSortByHot(4, 1, p);
		Page<Article> page5=articleService.listArticleByTypeAndStatusSortByHot(5, 1, p);
		Page<Article> page6=articleService.listArticleByTypeAndStatusSortByHot(6, 1, p);
		Page<Article> page7=articleService.listArticleByTypeAndStatusSortByHot(7, 1, p);
		Page<Article> page8=articleService.listArticleByTypeAndStatusSortByHot(8, 1, p);
		List<Article> list=articleService.listArticleByIstop(true);
		List<Article> articleslist=null;
		List<Article> question=null;
		List<Article> share=null;
		List<Article> advice=null;
		List<Article> life=null;
		List<Article> resource=null;
		List<Article> joke=null;
		List<Article> science_reading=null;
		List<Article> other=null;
		if(page0!=null) {
			articleslist=page0.getContent();
		}
		if(page1!=null) {
			question=page1.getContent();
		}
		if(page2!=null) {
			share=page2.getContent();
		}
		if(page3!=null) {
			advice=page3.getContent();
		}
		if(page4!=null) {
			life=page4.getContent();
		}
		if(page5!=null) {
			resource=page5.getContent();
		}
		if(page6!=null) {
			joke=page6.getContent();
		}
		if(page7!=null) {
			science_reading=page7.getContent();
		}
		if(page8!=null) {
			other=page8.getContent();
		}
		
		Sort sort = new Sort(Direction.DESC,"reading","commentsize","likes","createTime"); 
		Sort usersort = new Sort(Direction.DESC,"articlenum","scan","createTime"); 
		Pageable pageable=PageRequest.of(0, 10,sort);
		Pageable userpageable=PageRequest.of(0, 12,usersort);
		Page<Article> page=articleService.listArticleByStatus(1, pageable);
		Page<User> userpage=userService.listUsersByHost(userpageable);
		List<Article> articleHost=page.getContent();
		List<User> userHost=userpage.getContent();
		
		
		model.addAttribute("Login", Login);
		
		model.addAttribute("topArticleList", list);
		model.addAttribute("articleslist", articleslist);
		model.addAttribute("question", question);
		model.addAttribute("share", share);
		model.addAttribute("advice", advice);
		model.addAttribute("life", life);
		model.addAttribute("resource", resource);
		model.addAttribute("joke", joke);
		model.addAttribute("science_reading", science_reading);
		model.addAttribute("other", other);
		model.addAttribute("articleHost", articleHost);
		model.addAttribute("userHost", userHost);
		
		return new ModelAndView("/index");
	}
	
	@RequestMapping("/catalog")
	public String catalog() {
		return "catalog";
	}
	@RequestMapping("/login")
	public String login() {
		return "login";
	}	 
	@RequestMapping("/reg")
	public String register() {
		return "reg";
	}
	
	 @RequestMapping("/check.jpg")  
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        
        response.setHeader("Expires", "-1");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setHeader("Pragma", "-1");  
        CaptchaUtil util = CaptchaUtil.Instance();  
       
        String code = util.getString();  
        request.getSession().setAttribute("checkcode", code);  
      
        ImageIO.write(util.getImage(), "jpg", response.getOutputStream());  
    }  
    @RequestMapping("/login-error")
	public String loginerror() {
		return "login";
	}
}
