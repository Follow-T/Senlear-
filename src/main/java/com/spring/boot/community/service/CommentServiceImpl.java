package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Reply;
import com.spring.boot.community.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.getOne(id);
	}
	
	@Override
	@Transactional
	public void removeComment(Long id) {
		commentRepository.deleteById(id);
	}

	@Override
	public void likeComment(Long id) {
		Comment comment=commentRepository.getOne(id);
		Long like=comment.getLike();
		like+=1;
		comment.setLike(like);
		commentRepository.save(comment);
	}

	@Override
	public void hateComment(Long id) {
		Comment comment=commentRepository.getOne(id);
		Long hate=comment.getHate();
		hate+=1;
		comment.setLike(hate);
		commentRepository.save(comment);
	}
	
	//添加回复
	@Override
	@Transactional
	public Comment createReply(Long commentid, String content, Long userid, String useravatar,String username,String name,Long touserid,String touseravatar,String tousername,String toname) {
		Comment comment=commentRepository.getOne(commentid);
		Reply reply=new Reply(content, userid, useravatar,username,name,touserid,touseravatar,tousername,toname);
		comment.addReply(reply);
		return commentRepository.save(comment);
	}
	
	//删除回复
	@Override
	@Transactional
	public void removeReply(Long commentid, Long replyid) {
		Comment comment=commentRepository.getOne(commentid);
		comment.removeReply(replyid);
		commentRepository.save(comment);
	}

	

	@Override
	@Transactional
	public void SaveComment(Comment comment) {
		
		commentRepository.save(comment);
	}

	@Override
	public List<Comment> ListCommentByUserid(String userid) {	
		
		return commentRepository.findByUserid(userid);
	}

	
}
