package com.spring.boot.community.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.client.MultipartBodyBuilder.PublisherEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.Gson;
import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.CollectArticle;
import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Reply;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ArticleService;
import com.spring.boot.community.service.CollectArticleService;
import com.spring.boot.community.service.CommentService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@RestController
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CollectArticleService collectArticleService;
	
	private HttpServletRequest request;
	private String username;
	private Long id;

	//浏览用户文章
	@GetMapping("/{username}/article/{id}")
	public ModelAndView article(@PathVariable("username") String username,@PathVariable("id") Long id,Model model)
	{
		
		ModelAndView modelAndView =new ModelAndView();
		User user=userService.findByUsername(username);
		Article article=articleService.getArticleById(id);
		articleService.readingIncrease(id);
		boolean isOwner = false;
		boolean isCollect=false;
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			} 
			List<CollectArticle> collectArticles=collectArticleService.listCollectArticleByUser_id(principal.getId());
			for(CollectArticle collectArticle:collectArticles) {
				if(collectArticle.getArticleid()==id) {
					isCollect=true;
					break;
				}
			}
		} 
		//获取文章的评论
		Set<Comment> comments=article.getComment();
		List<Comment> commentList = new ArrayList<>(comments);
		Collections.sort(commentList, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                //升序
                return o1.getId().compareTo(o2.getId());
            }
            
		});
		
		
		Boolean hasResource=article.getHasResource();
		System.out.println(hasResource);
		model.addAttribute("article", article);
		model.addAttribute("user", user);
		model.addAttribute("commentList", commentList);
		model.addAttribute("isArticleOwner", isOwner);
		model.addAttribute("isCollect", isCollect);
		if(hasResource==false||hasResource==null) {
			Sort sort = new Sort(Direction.DESC,"reading","commentsize","likes","createTime");
			Pageable pageable=PageRequest.of(0, 10,sort);
			Page<Article> page=articleService.listArticleByStatus(1, pageable);
			List<Article> articleHost=page.getContent();
			model.addAttribute("articleHost", articleHost);
			modelAndView.setViewName("/user/detail");
		}else{
			modelAndView.setViewName("/user/detailfile");
		}
		return modelAndView;
	}
	
	//根据用户登录的账号返回用户的文章
	@GetMapping("/{username}/article")
	@PreAuthorize("authentication.name.equals(#username)")
	public String UserArticle(HttpServletRequest request,@PathVariable("username") String username,@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
		String callback=request.getParameter("callback");
		JsonData jsonData;
		Pageable page=PageRequest.of(pageIndex, pageSize);
		Page<Article> articles=articleService.listArticles(page);
		List<Article> list=articles.getContent();
		list.get(0).getUser().setPassword("");
		Gson gson=new Gson();
		//JSONArray json = JSONArray.fromObject(list);
		String str = gson.toJson(list);
		System.out.println(str);
		//model.addAttribute("ArticleList", list);
		jsonData=new JsonData("200", "Success", gson.toJson(list), callback);
		return jsonData.toString();
	}
	
	//获取用户编写文章界面
	@GetMapping("/{username}/article/publish")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView articleEdit(@PathVariable("username") String username,Model model)
	{
		ModelAndView modelAndView =new ModelAndView();
		modelAndView.addObject(username);
		modelAndView.setViewName("/user/articlepublish");
		return modelAndView;
	}
	//获取编辑文章界面
	@GetMapping("/{username}/article/edit/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView EditArticle(HttpServletRequest request,@PathVariable("username") String username,@PathVariable("id") Long id)
	{
		ModelAndView modelAndView =new ModelAndView();
		modelAndView.addObject(username);
		User user = (User)userDetailsService.loadUserByUsername(username);
		modelAndView.addObject(user);
		Article article = articleService.getArticleById(id);
		modelAndView.addObject(article);
		modelAndView.setViewName("/user/edit");
		return modelAndView;
	}
	//获取文章内容
	@PostMapping("/{username}/article/edit/content/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public String getArticleById(HttpServletRequest request,@PathVariable("username") String username,@PathVariable("id") Long id)
	{
		String callback=request.getParameter("callback");
		JsonData jsonData;
		Article article=articleService.getArticleById(id);
		if(article==null)
		{
			jsonData=new JsonData("201", "Error", "Article is not exit", callback);
			return jsonData.toString();
		}
		jsonData=new JsonData("200", "Success", article.getContent(), callback);
		return jsonData.toString();
	}
	//保存编辑
	@PostMapping("/{username}/article/edit/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public String saveEditArticle(HttpServletRequest request,@PathVariable("username") String username,@PathVariable("id") Long id,Article article)
	{
			User user=(User) userDetailsService.loadUserByUsername(username);
			Article article1 = articleService.getArticleById(id);
			String callback=request.getParameter("callback");
			JsonData jsonData;
			if(article.getTitle().length()>=50)
			{
				jsonData=new JsonData("201", "Error", "Title length is too long!", callback);
			}
			article1.setTitle(article.getTitle());
			article1.setSummary(article.getSummary());
			article1.setType(article.getType());
			article1.setContent(article.getContent());
			user.setArticlenum(user.getArticlenum()+1);
			userService.saveUser(user);
			articleService.save(article1);
			jsonData=new JsonData("200", "Success", "Article is save success!", callback);
			return jsonData.toString();
	}
	//用户提交文章
	@PostMapping("/{username}/article/edit")
	@PreAuthorize("authentication.name.equals(#username)") 
	public String submitArticle(HttpServletRequest request,@PathVariable("username") String username,Article article)
	{
		User user=(User) userDetailsService.loadUserByUsername(username);
		String callback=request.getParameter("callback");
		JsonData jsonData;
		if(article.getTitle().length()>=50)
		{
			jsonData=new JsonData("201", "Error", "Title length is too long!", callback);
		}
		article.setUser(user);
		article.setReading(0L);
		article.setHasResource(false);
		articleService.save(article);
		jsonData=new JsonData("200", "Success", "Article is save success!", callback);
		return jsonData.toString();
	}

	//更改文章的状态
	@PostMapping("/{username}/changeArticle/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public String changeArticle(HttpServletRequest request,@PathVariable String username,@PathVariable Long id,int status)
	{
		String callback=request.getParameter("callback");
		JsonData jsonData;
		Article article=articleService.getArticleById(id);
		if(article==null)
		{
			jsonData=new JsonData("201", "Error", "Article is not exit", callback);
			return jsonData.toString();
		}
		article.setStatus(status);
		jsonData=new JsonData("200", "Success", "Change status success", callback);
		return jsonData.toString();
	}
	
	//删除文章
	@PostMapping("/{username}/article/delete/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public String deleteArticle(HttpServletRequest request,@PathVariable("username") String username,@PathVariable("id") Long id)
	{
		String callback=request.getParameter("callback");
		JsonData jsonData;
		Article article=articleService.getArticleById(id);
		if(article==null)
		{
			jsonData=new JsonData("201", "Error", "Article is not exit", callback);
			return jsonData.toString();
		}
		articleService.delete(id);
		jsonData=new JsonData("200", "Success", "Delete article success", callback);
		return jsonData.toString();
	}
	
	//根据文章状态，排序方式进行模糊查询
	@PostMapping("/articles")
	public String searchArticle(HttpServletRequest request,int type,int sortway,
			@RequestParam(value="pageStart",required=false,defaultValue="0") int pageStart,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize){
		String callback=request.getParameter("callback");
		JsonData jsonData;
		Sort sort=null;
		if(sortway==1) {
			sort = new Sort(Direction.DESC,"createTime"); 
		}else if(sortway==2){
			sort = new Sort(Direction.DESC,"reading","commentsize","likes","createTime"); 
		}	
		List<Article> articleList=new ArrayList<>();
		if(type==0) {
			articleList=articleService.listArticlesBypageAllType(pageStart, pageSize);
		}else if(type==1) {
			articleList=articleService.listArticlesBypageAndType(1, pageStart, pageSize);
		}else if(type==2) {
			articleList=articleService.listArticlesBypageAndType(2, pageStart, pageSize);
		}else if(type==3) {
			
			articleList=articleService.listArticlesBypageAndType(3, pageStart, pageSize);
		}else if(type==4) {
			
			articleList=articleService.listArticlesBypageAndType(4, pageStart, pageSize);
		}else if(type==5) {
		
			articleList=articleService.listArticlesBypageAndType(5, pageStart, pageSize);
		}else if(type==6) {
			
			articleList=articleService.listArticlesBypageAndType(6, pageStart, pageSize);
		}else if(type==7) {
			
			articleList=articleService.listArticlesBypageAndType(7, pageStart, pageSize);
		}else if(type==8) {
			
			articleList=articleService.listArticlesBypageAndType(8, pageStart, pageSize);
		}
		
        Set<Comment> comment=null;
		System.out.println(articleList.size());
		if(articleList.size()==0)
		{
			jsonData=new JsonData("201", "Error", "Article is null!", callback);
			
		}else {
			for(int i=0;i<articleList.size();i++)
			{
				Article article=articleList.get(i);
				String title=article.getTitle();
				title=title.replaceAll("\"", "'");
				title=title.replaceAll("\n", "");
				article.setContent("");
				article.setSummary("");
				article.setComment(comment);
				article.getUser().setPassword("");
			}
			Gson gson=new Gson();
			jsonData=new JsonData("200", "Success", gson.toJson(articleList), callback);
		}
		
		return jsonData.toString();
	}
	
	//审核文章（发布）更改文章状态
	@GetMapping("/change/article/{id}/{status}")
	public String changeArticleStatus(HttpServletRequest request,@PathVariable("status") int status,@PathVariable("id") Long id) {
		String callback=request.getParameter("callback");
		JsonData jsonData;
		Article article=articleService.getArticleById(id);
		article.setStatus(status);
		articleService.save(article);
		jsonData=new JsonData("200", "Success", "Change ArticleStatus Success!", callback);
		return jsonData.toString();
	}
	
	//根据文章Title进行模糊查询
	@PostMapping("/user/manageArticle")
	 public String GetArticles(
	    		HttpServletRequest request,
	    		@RequestParam(value="username",required=false,defaultValue="") String username,
	    		@RequestParam(value="title",required=false,defaultValue="") String title,
	    		@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
				@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
	    	System.out.println(username);
	    	System.out.println(title);
	    	String callback=request.getParameter("callback");
	    	JsonData jsonData;
	    	Page<Article> articles=null;
	    	List<Article> list=null;
	    	Set<Comment> comment=null;
	    	User user=null;
	    	if(username!="") {
	    		user=userService.findByUsername(username);
	    		if(user==null) {
	    			jsonData=new JsonData("201", "Error", "无此用户!", callback);
	    			return jsonData.toString();
	    		}
	    	}
	    	Pageable pageable=PageRequest.of(pageIndex, pageSize);
	    	articles=articleService.listArticlesByTitleLikeAndSort(user, title, pageable);
	    	list=articles.getContent();
	    	if(list.size()==0)
			{
				jsonData=new JsonData("201", "Error", "Article is null!", callback);
				
			}else {
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
	
	@PostMapping("/collect/{userid}/{articleid}")
	public String collectArticle(HttpServletRequest request,@PathVariable("userid") Long userid,@PathVariable("articleid") Long articleid) {
		String callback=request.getParameter("callback");
		String type=request.getParameter("type");
		JsonData jsonData;
		if(type.equals("1")) {
			CollectArticle collectArticle=new CollectArticle(userid, articleid);
			collectArticleService.saveCollectArticle(collectArticle);
			jsonData=new JsonData("200", "Success", "收藏文章成功", callback);
		}else {
			CollectArticle collectArticle=collectArticleService.getCollectArticle(userid, articleid);
			collectArticleService.removeCollectArticle(collectArticle.getId());
			jsonData=new JsonData("200", "Success", "取消收藏文章成功", callback);
		}
		return jsonData.toString();
	}
	
}
