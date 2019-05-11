package com.spring.boot.community.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.spring.boot.community.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Resource;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ResourceService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;

@RestController
public class ResourceController {
	
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private UserService userService;
	@Autowired
	private ArticleService articleService;
	
	//首页显示资源
    @GetMapping("/resources")
    public String resources(HttpServletRequest request,@RequestParam(value="title",required=false,defaultValue="")String title,@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
    	JsonData jsonData;
    	String callback=request.getParameter("callback");
    	Pageable pageable=PageRequest.of(pageIndex, pageSize);
    	Page<Resource> resoruces=resourceService.listResoruces(pageable);
    	List<Resource> resourceList=resoruces.getContent();
    	if(resourceList.size()==0)
		{
			jsonData=new JsonData("201", "Error", "Resource is null!", callback);
			
		}else {
			for(int i=0;i<resourceList.size();i++)
			{
				Resource resoruce=resourceList.get(i);
				String content=resoruce.getContent();
				content=content.replaceAll("\"", "'");
				content=content.replaceAll("\n", "");
				resoruce.setContent(content);
			}
			Gson gson=new Gson();
			jsonData=new JsonData("200", "Success", gson.toJson(resourceList), callback);
		}		
		return jsonData.toString();

    }
    
    //分享资源
    @ResponseBody
    @PostMapping("/{username}/shareResource")
    public String shareResource(HttpServletRequest request,String url,@RequestParam(value="status",required=false,defaultValue="0") String status,@PathVariable("username") String username,String title,String content,String summary,int type) {
    	JsonData jsonData;
    	String callback=request.getParameter("callback");
    	//判断操作用户是否为本人操作
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				Article article=new Article(title,summary,content);
				article.setHasResource(true);
				article.setResourceurl(url);
				article.setType(type);
				article.setUser(userService.findByUsername(username));
				article.setReading(0L);
				articleService.save(article);
				loginuser.setArticlenum(loginuser.getArticlenum()+1);
				userService.saveUser(loginuser);
		        jsonData=new JsonData("200", "Success",article.getId() , callback);
		        
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
				
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
    }
    
    //用户浏览资源
    @GetMapping("/{username}/resource/{id}")
    public ModelAndView resource(@PathVariable("username") String username,@PathVariable("id") Long id,Model model) {
    	User user=userService.findByUsername(username);
    	Resource resource=resourceService.getResoruceById(id);
    	resourceService.readingIncrease(id);
    	boolean isOwner = false;
    	//判断操作用户是否为本人
    	if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			} 
		} 
    	//获取资源的评论
    	List<Comment> commentList=resource.getComments();
    	model.addAttribute("resource", resource);
		model.addAttribute("user", user);
		model.addAttribute("commentList", commentList);
		model.addAttribute("isResourceOwner", isOwner);
    	return new ModelAndView("");
    }
    
    //根据用户登录的账号返回用户的文章
    @GetMapping("/{username}/resource")
	@PreAuthorize("authentication.name.equals(#username)")
    public String UserResource(HttpServletRequest request,@PathVariable("username") String username,@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
    	String callback=request.getParameter("callback");
    	JsonData jsonData;
    	Pageable page=PageRequest.of(pageIndex, pageSize);
    	Page<Resource> resources=resourceService.listResoruces(page);
    	List<Resource> resourceList=resources.getContent();
    	Gson gson=new Gson();
    	jsonData=new JsonData("200", "Success", gson.toJson(resourceList), callback);
    	return jsonData.toString();
    }
    
    //删除资源
  	@DeleteMapping("/{username}/resource/{id}")
  	@PreAuthorize("authentication.name.equals(#username)")
  	public String deleteArticle(HttpServletRequest request,@PathVariable String username,@PathVariable Long id)
  	{
  		String callback=request.getParameter("callback");
  		JsonData jsonData;
  		Resource resource=resourceService.getResoruceById(id);
  		if(resource==null)
  		{
  			jsonData=new JsonData("201", "Error", "Article is not exit", callback);
  			return jsonData.toString();
  		}
  		resourceService.delete(id);
  		jsonData=new JsonData("200", "Success", "Delete article success", callback);
  		return jsonData.toString();
  	}
  	
    //更改资源的状态
  	@GetMapping("/{username}/changeResource/{id}")
  	@PreAuthorize("authentication.name.equals(#username)")
  	public String changeResource(HttpServletRequest request,@PathVariable String username,@PathVariable Long id,String status)
  	{
  		String callback=request.getParameter("callback");
  		JsonData jsonData;
  		Resource resource=resourceService.getResoruceById(id);
  		if(resource==null)
  		{
  			jsonData=new JsonData("201", "Error", "Article is not exit", callback);
  			return jsonData.toString();
  		}
  		resource.setStatus(status);
  		jsonData=new JsonData("200", "Success", "Change status success", callback);
  		return jsonData.toString();
  	}
    
    
}
