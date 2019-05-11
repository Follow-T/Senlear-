package com.spring.boot.community.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ArticleService;
import java.util.ArrayList;
import java.util.List;

@Controller
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class Test {
	@Autowired
	ArticleService articleService;
	
	@PostMapping("/k")
	@ResponseBody
	public String index(String username,String password,String checkcode)
	{
		System.out.println(11111);
		return "index";
	}
	
	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserNameSimple(HttpServletRequest request) {
		
		/*UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();*/
		List<Article> articles=articleService.listArticlesBypageAndType(7, 10, 1);
		for(Article article:articles) {
			System.out.println(article.getId());
		}
        return "1111";
    }

}
