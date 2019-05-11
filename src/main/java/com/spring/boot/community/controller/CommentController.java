package com.spring.boot.community.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Reply;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.service.ArticleService;
import com.spring.boot.community.service.CommentService;
import com.spring.boot.community.service.ReplyService;
import com.spring.boot.community.service.UserService;
import com.spring.boot.community.tools.JsonData;


@RestController
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ReplyService replyService;
	//喜欢 点赞
	@PostMapping("/{username}/likeComment")
	public String likeUserComment(HttpServletRequest request,@PathVariable("username") String username,Long commentid) {
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				commentService.likeComment(commentid);
				jsonData=new JsonData("200", "success", "Comment Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//踩一下
	@GetMapping("/{username}/hateComment")
	public String hateUserComment(HttpServletRequest request,@PathVariable("username") String username,Long commentid) {
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				commentService.hateComment(commentid);
				jsonData=new JsonData("200", "success", "Comment Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//用户评论文章
	@PostMapping("/{username}/submitComment")
	public String submitUserComment(HttpServletRequest request,@PathVariable("username") String username,Long articleid,String content)
	{
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				articleService.CreateComment(articleid, content, loginuser.getId()+"", loginuser.getAvatar(),loginuser.getUsername(),loginuser.getName());
				jsonData=new JsonData("200", "success", "Comment Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//用户删除自己的评论
	@DeleteMapping("/{username}/deleteComment")
	public String deleteUserComment(HttpServletRequest request,@PathVariable("username") String username,Long articleid,Long commentid) 
	{
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				articleService.RemoveComment(articleid, commentid);
				commentService.removeComment(commentid);
				jsonData=new JsonData("200", "success", "Delete Comment Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//登录用户回复评论
	@PostMapping("/reply/{username}")
	public String replyComments(HttpServletRequest request,@PathVariable("username") String username,Long commentid,String content,String tousername) {
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				User touser=userService.findByUsername(tousername);
				if(touser==null) {
					jsonData=new JsonData("201", "Error", "评论的用户不存在", callback);
				}else {
					commentService.createReply(commentid,content,loginuser.getId(), loginuser.getAvatar(),loginuser.getUsername(),loginuser.getName(),
							touser.getId(),touser.getAvatar(),touser.getUsername(),touser.getName());
					jsonData=new JsonData("200", "success", "Reply Comment Success", callback);
				}
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//用户删除自己的回复
	@DeleteMapping("/{username}/deleteReply")
	public String deleteUserReply(HttpServletRequest request,@PathVariable("username") String username,Long replyid,Long commentid) 
	{
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				commentService.removeReply(commentid, replyid);
				replyService.removeReply(replyid);
				jsonData=new JsonData("200", "success", "Delete Reply Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	//喜欢 点赞
	@PostMapping("/{username}/likeReply")
	public String likeUserReply(HttpServletRequest request,@PathVariable("username") String username,Long replyid) {
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				replyService.likeReply(replyid);
				jsonData=new JsonData("200", "success", "Comment Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}
	
	//踩一下
	@GetMapping("/{username}/hateReply")
	public String hateUserReply(HttpServletRequest request,@PathVariable("username") String username,Long replyid) {
		JsonData jsonData;
		String callback=request.getParameter("callback");
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User loginuser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(loginuser.getUsername().equals(username)){
				replyService.hateReply(replyid);
				jsonData=new JsonData("200", "success", "Comment Success", callback);
			}else {
				jsonData=new JsonData("201", "Error", "操作用户不为本人!", callback);
			}
		}else {
			jsonData=new JsonData("202", "Error", "请先登录!", callback);
		}
		return jsonData.toString();
	}

	//test
	@GetMapping("/test")
	public List<Comment> comment() {
		Set<Comment> comments=articleService.getArticleById(1L).getComment();
		List<Comment> result = new ArrayList<>(comments);
		Collections.sort(result, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                //升序
                return o1.getId().compareTo(o2.getId());
            }
		});
		for(Comment comment:result) {
			System.out.println(comment.getId());
			/*if(comment.getId()==1L) {
				for(int i=0;i<comment.getReplies().size();i++) {
					System.out.println(comment.getReplies().get(i).getId());
				}
				//return comment.getReplies();
			}*/
		}
		return result;
	}
	
}
