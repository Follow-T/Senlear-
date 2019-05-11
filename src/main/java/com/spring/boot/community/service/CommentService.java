package com.spring.boot.community.service;

import java.util.List;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.User;

public interface CommentService {
	
	
	Comment getCommentById(Long id);
	
	void removeComment(Long id);
	
	void SaveComment(Comment comment);
	
	void likeComment(Long id);
	
	void hateComment(Long id);
	
	Comment createReply(Long commentid,String content,Long userid,String useravatar,String username,String name,Long touserid,String touseravatar,String tousername,String toname);
	
	void removeReply(Long commentid, Long replyid);
	
	List<Comment> ListCommentByUserid(String userid);
}
