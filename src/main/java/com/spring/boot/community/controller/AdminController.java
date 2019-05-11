package com.spring.boot.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Authority;
import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ArticleService;
import com.spring.boot.community.service.AuthorityService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private UserService userService;
	
    @RequestMapping("/admin")
    public String adminIndex(Model model) {
    	User user=(User) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	model.addAttribute("user", user);
        return "admin/index";
    }
    @RequestMapping("/admin/index")
    public String index() {
        return "redirect:/admin";
    }
    
    @RequestMapping("/admin/managerHome")
    public String adminManageHome() {
        return "redirect:/admin";
    }
    @RequestMapping("/admin/adminManager")
    public String adminManager(Model model) {
    	User user=(User) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	System.out.println(user.getName());
    	System.out.println(111);
    	model.addAttribute("user", user);
        return "admin/admin-list";
    }
    
    @RequestMapping("/admin/article-add")
    public String articleadd(Model model) {
    	User user=(User) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	model.addAttribute("user", user);
        return "admin/article-add";
    }
    
    @RequestMapping("/admin/checkArticle")
    public String adminCheckArticle(){
        return "admin/admin-add";
    }
    @RequestMapping("/admin/manage")
    public String adminManageUser(Model model) {
    	User user=(User) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	System.out.println(user.getName());
    	System.out.println(111);
    	List<User> userList=userService.listUsers();
    	model.addAttribute("user", user);
    	model.addAttribute("userList", userList);
        return "admin/admin-list";
    }
    
    @RequestMapping("/admin/useradd")
    public String useradd(Model model) {
        return "admin/admin-user-add";
    }
    
    @RequestMapping("/admin/useradd/{id}")
    public String useredit(Model model,@PathVariable("id") Long id) {
    	User user=userService.getUserById(id);
    	model.addAttribute("user", user);
        return "admin/admin-add";
    }
    
    @PostMapping("/admin/user/edit")
    @ResponseBody
    public String useredit(User user,HttpServletRequest request) {
    	JsonData jsonData;
    	if(userService.findByUsername(user.getUsername())!=null) {
    		User exitUser=userService.findByUsername(user.getUsername());
    		if(user.getPassword().equals(exitUser.getPassword())) {
    			
    		}else {
    			exitUser.setEncodePassword(user.getPassword());
    			System.out.println("密码已更改");
    		}
    		exitUser.setName(user.getName());
			exitUser.setSex(user.getSex());
			exitUser.setPhonenumber(user.getPhonenumber());
			exitUser.setEmail(user.getEmail());
			exitUser.setRole(user.getRole());
			exitUser.setInfo(user.getInfo());
			userService.saveUser(exitUser);
    		jsonData=new JsonData("200", "修改信息成功！", "123","");
    		return jsonData.toString();
    	} else{
    		System.out.println(user.getName());
    		jsonData=new JsonData("200", "Sucuess", "RegisterSucuess","");
    		user.setEncodePassword(user.getPassword());
    		List<Authority> authorities=new ArrayList<>();
    		authorities.add(authorityService.getAuthorityById(1L));
    		user.setAuthorities(authorities);
    		userService.saveUser(user);
    		System.out.println("Yes");
    		userService.saveUser(user);
    		return jsonData.toString();
    	}
    }
    
    
    @PostMapping("/admin/delUser/{id}")
    @ResponseBody
    public String userdel(HttpServletRequest request,@PathVariable("id") Long id) {
    	String callback=request.getParameter("callback");
    	userService.removeUser(id);
    	JsonData jsonData=new JsonData("200", "Success", "删除用户成功!", callback);
        return jsonData.toString();
    }
    
    @PostMapping("/admin/changeUserStatus/{id}")
    @ResponseBody
    public String changeUserStatus(HttpServletRequest request,@PathVariable("id") Long id,int status) {
    	String callback=request.getParameter("callback");
    	User user=userService.getUserById(id);
    	user.setStatus(status);
    	userService.saveUser(user);
    	JsonData jsonData=new JsonData("200", "Success", "更改用户状态成功!", callback);
        return jsonData.toString();
    }
    
    
    //charts-1
    @RequestMapping("/admin/charts/{number}")
    public String adminCharts(@PathVariable("number") String number) {
        if(this.isNumeric(number)){
            return "admin/charts-1";
        }else{
            return "admin/charts-2";
        }

    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    
    
    @GetMapping("/admin/managerArticle")
    public String managerArticle(Model model) {
    	User user=null;
    	if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
    		user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
    	
    	List<Article> list=articleService.listArticles();
    	
    	List<Article> userArticles=articleService.listArticleByUser(user);
    	
    	
    	System.out.println(userArticles.size());
    	model.addAttribute("articleList", list);
    	model.addAttribute("userArticles", userArticles);
    	model.addAttribute("size", list.size());
    	model.addAttribute("user", user);
    	return "/admin/article-list";
    }
    
    //根据文章type,发布人，title进行模糊查询
    @ResponseBody
    @PostMapping("/admin/managerArticle")
    public String GetArticles(
    		HttpServletRequest request,
    		@RequestParam(value="type",required=false,defaultValue="0") int type,
    		@RequestParam(value="username",required=false,defaultValue="") String username,
    		@RequestParam(value="title",required=false,defaultValue="") String title,
    		@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
    	System.out.println(type);
    	System.out.println(username+"11111");
    	System.out.println(title);
    	String callback=request.getParameter("callback");
    	JsonData jsonData;
    	Page<Article> articles=null;
    	List<Article> list=null;
    	Set<Comment> comment=null;
    	User user=null;
    	if(username.equals("")) {
    		System.out.println(username);
    	}else {
    		user=userService.findByUsername(username);
    		if(user==null) {
    			jsonData=new JsonData("201", "Error", "无此用户!", callback);
    			return jsonData.toString();
    		}
    	}
    	Pageable pageable=PageRequest.of(pageIndex, pageSize);
    	if(type==0&&username.equals("")) {
    		articles=articleService.listArticlesByTitleLike(title, pageable);
    		
    		
    	}else if(type==1&&username.equals("")) {
    		articles=articleService.listArticlesByTypeAndTitleLike(type, title, pageable);
    		
    	}else if(type==0&&username!="") {
    		articles=articleService.listArticlesByUserAndTitleLike(user, title, pageable);
    		
    	}else if(type==1&&username!="") {
    		articles=articleService.listArticlesByTypeAndUserAndTitleLike(type,title, pageable,user);
    		
    	}
    	if(articles==null) {
    		jsonData=new JsonData("201", "Error", "Article is null!", callback);
    	}else {
    		list=articles.getContent();
        	System.out.println(list.size());
        	for(int i=0;i<list.size();i++)
			{
				Article article=list.get(i);
				String content=article.getContent();
				//content=content.replaceAll("\"", "'");
				//content=content.replaceAll("\n", "");
				article.setContent("");
				article.setComment(comment);
				article.getUser().setPassword("");
			}
			Gson gson=new Gson();
			jsonData=new JsonData("200", "Success", gson.toJson(list), callback);
		}
    	return jsonData.toString();
    }
}
