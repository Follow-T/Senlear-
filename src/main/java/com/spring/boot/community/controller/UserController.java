package com.spring.boot.community.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.spring.boot.community.domin.Resource;
import com.spring.boot.community.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.CollectArticle;
import com.spring.boot.community.domin.Follow;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ArticleService;
import com.spring.boot.community.service.CollectArticleService;
import com.spring.boot.community.service.FollowService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ArticleService articleService;

	@Autowired
	FollowService followService;
	
	@Autowired
	private CollectArticleService collectArticleService;
	
	//用户主页
	@RequestMapping("/{username}/home")
    public ModelAndView home(Model model,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		List<Article> articlesList=articleService.listArticleByUser(user);
		user.setScan(user.getScan()+1);
		userService.saveUser(user);
		model.addAttribute("user", user);
		model.addAttribute("articlesList", articlesList);
		Boolean isFollow=false;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			List<Follow> follows=followService.listFollowByUser_id(loginuser.getId());
			for(Follow follow:follows) {
				if(follow.getFollowuserid()==user.getId()) {
					isFollow=true;
					break;
				}
			}
		}
		model.addAttribute("isFollow", isFollow);
        return new ModelAndView("/user/home");
    }
	@GetMapping("/{username}/file/index")
	public ModelAndView fileIndex(Model model,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/file/index");
	}
	@GetMapping("/{username}/file/share")
	public  ModelAndView share(Model model,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/file/share");
	}
	//用户后台   当前用户才可以访问
	@RequestMapping("/{username}")
	@PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView UserIndex(Model model,@PathVariable("username") String username) {
		
		User user=userService.findByUsername(username);
		List<Article> articlesList=articleService.listArticleByUser(user);
		//收藏的文章
		List<CollectArticle> collectArticles=collectArticleService.listCollectArticleByUser_id(user.getId());	
		List<Article> collectArticleList=new ArrayList<>();	
		for(CollectArticle collectArticle:collectArticles) {
			collectArticleList.add(articleService.getArticleById(collectArticle.getArticleid()));
		}
		//关注的用户
		List<Follow> follows=followService.listFollowByUser_id(user.getId());
		List<Article> followArticleList=new ArrayList<>();	
		for(Follow follow:follows) {
			User followuser=userService.getUserById(follow.getFollowuserid());
			List<Article> articles=articleService.listArticleByUser(followuser);
			followArticleList.addAll(articles);
		}
		//我的粉丝
				List<Follow> followMe=followService.listFollowByFollowUser_id(user.getId());
				List<Article> followMeArticleList=new ArrayList<>();	
				for(Follow follow:followMe) {
					User followuser=userService.getUserById(follow.getFollowuserid());
					List<Article> articles=articleService.listArticleByUser(followuser);
					followMeArticleList.addAll(articles);
				}
		
		model.addAttribute("user", user);
		model.addAttribute("followuserSize", follows.size());
		model.addAttribute("followMeSize", followMe.size());
		model.addAttribute("articlesList", articlesList);
		model.addAttribute("collectArticleList", collectArticleList);
		model.addAttribute("followArticleList", followArticleList);
		model.addAttribute("followMeArticleList", followMeArticleList);
        return new ModelAndView("/user/index");
    }
	
	@RequestMapping("/{username}/index")
	@PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView index(Model model,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		List<Article> articlesList=articleService.listArticleByUser(user);
		//收藏的文章
		List<CollectArticle> collectArticles=collectArticleService.listCollectArticleByUser_id(user.getId());	
		List<Article> collectArticleList=new ArrayList<>();	
		for(CollectArticle collectArticle:collectArticles) {
			collectArticleList.add(articleService.getArticleById(collectArticle.getArticleid()));
		}
		
		//关注的用户
		List<Follow> follows=followService.listFollowByUser_id(user.getId());
		List<Article> followArticleList=new ArrayList<>();	
		for(Follow follow:follows) {
			User followuser=userService.getUserById(follow.getFollowuserid());
			List<Article> articles=articleService.listArticleByUser(followuser);
			followArticleList.addAll(articles);
		}
		
		//我的粉丝
		List<Follow> followMe=followService.listFollowByFollowUser_id(user.getId());
		List<Article> followMeArticleList=new ArrayList<>();	
		for(Follow follow:followMe) {
			User followuser=userService.getUserById(follow.getFollowuserid());
			List<Article> articles=articleService.listArticleByUser(followuser);
			followMeArticleList.addAll(articles);
		}
		model.addAttribute("followMeSize", followMe.size());
		model.addAttribute("user", user);
		model.addAttribute("followuserSize", follows.size());
		model.addAttribute("articlesList", articlesList);
		model.addAttribute("collectArticleList", collectArticleList);
		model.addAttribute("followArticleList", followArticleList);
		model.addAttribute("followMeArticleList", followMeArticleList);
        return new ModelAndView("/user/index");
    }
	@RequestMapping("/{username}/file/manager")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView fileManager(Model model,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		List<Article> articlesList=articleService.listArticleByUser(user);
		model.addAttribute("user", user);
		model.addAttribute("articlesList", articlesList);
		return new ModelAndView("/user/file");
	}
	
	//更改用户信息
	@RequestMapping("/{username}/set")
	@PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView set(Model modle,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		modle.addAttribute("user", user);
        return new ModelAndView("/user/set");
    }
	
	@PostMapping("/{username}/changeUserDetail")
	public String changeUserDetail(HttpServletRequest request,User user,@PathVariable("username") String username)
	{
		String callback=request.getParameter("callback");
		JsonData jsonData;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){

				loginuser.setAddress(user.getAddress());
				loginuser.setEmail(user.getEmail());
				loginuser.setName(user.getName());
				loginuser.setInfo(user.getInfo());
				loginuser.setSex(user.getSex());
				loginuser.setPhonenumber(user.getPhonenumber());
				loginuser.setSign(user.getSign());
				userService.saveUser(loginuser);
				jsonData=new JsonData("200", "Success", "Success Save UserInfo!", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//更改用户密码
	@GetMapping("/{username}/changeUserPassword")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView changeUserPassword(Model model,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		model.addAttribute("user", user);
		ModelAndView modelAndView=new ModelAndView("");
		return modelAndView;
	}
	

	@PostMapping("/{username}/changeUserPassword")
	public String changeUserPassword(HttpServletRequest request,String oldPass,String newPass,@PathVariable("username") String username)
	{
		String callback=request.getParameter("callback");
		JsonData jsonData;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				User user=userService.findByUsername(username);
				PasswordEncoder  encoder = new BCryptPasswordEncoder();
				String encodeOldpass = encoder.encode(oldPass);
				String encodeNewpass = encoder.encode(newPass);
				if(encoder.matches(oldPass, user.getPassword())) {
					if(encoder.matches(newPass, user.getPassword())){
						jsonData=new JsonData("201", "Error", "SamePassword", callback);
					}else{
						user.setPassword(encodeNewpass);
						userService.saveUser(user);
						jsonData=new JsonData("200", "Success", "Success Update Password!", callback);
					}
				}
				else {
					jsonData=new JsonData("201", "Error", "Error Old Pass!", callback);
				}
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	 //detailfile
	 //用户消息
	 @RequestMapping("/{username}/detailfile/{id}")
	 public ModelAndView detailfile(Model modle,@PathVariable("username") String username,@PathVariable("id") Long id) {
		 User user=userService.findByUsername(username);
		 modle.addAttribute("user", user);
		 return new ModelAndView("/user/detailfile");
	 }
	//用户消息
    @RequestMapping("/{username}/message")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView message(Model modle,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		modle.addAttribute("user", user);
        return new ModelAndView("/user/message");
    }
	//用户文章管理
	@RequestMapping("/{username}/articlemanage")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView article(Model modle,@PathVariable("username") String username) {
		User user=userService.findByUsername(username);
		List<Article> article = articleService.listArticleByUser(user);
		modle.addAttribute("articles", article);
		modle.addAttribute("user", user);
		return new ModelAndView("/user/articlemanage");
	}
	
	//添加关注
	@PostMapping("/follow/{user_id}/{followuser_id}")
	public String followUser(HttpServletRequest request,@PathVariable("user_id") Long user_id,@PathVariable("followuser_id") Long followuser_id) {
		String callback=request.getParameter("callback");
		String type=request.getParameter("type");
		JsonData jsonData;
		if(type.equals("1")) {
			Follow follow=new Follow(user_id, followuser_id);
			followService.saveFollow(follow);
			jsonData=new JsonData("200", "Success", "关注用户成功", callback);
		}else {
			Follow follow=followService.getFolloew(user_id, followuser_id);
			followService.removeFollow(follow.getId());
			jsonData=new JsonData("200", "Success", "取消关注用户成功", callback);
		}
		
		return jsonData.toString();
		
	}
	
	
}
