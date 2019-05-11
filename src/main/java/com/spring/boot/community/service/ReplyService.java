package com.spring.boot.community.service;

import java.util.List;

import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Reply;

public interface ReplyService {
	
	void saveReply(Reply reply);
	
	Reply getReplyById(Long id);
	
	void removeReply(Long id);
	
	void likeReply(Long id);
	
	void hateReply(Long id);
	
	List<Reply> ListReplyByUserid(Long userid);
}
