package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.Reply;
import com.spring.boot.community.repository.ReplyRepository;

@Service
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyRepository replyRepository;
	
	@Override
	public Reply getReplyById(Long id) {
		return replyRepository.getOne(id);
	}

	@Override
	@Transactional
	public void removeReply(Long id) {
		replyRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void likeReply(Long id) {
		Reply reply=replyRepository.getOne(id);
		Long like=reply.getLike();
		like+=1;
		reply.setLike(like);
		replyRepository.save(reply);
	}

	@Override
	@Transactional
	public void hateReply(Long id) {
		Reply reply=replyRepository.getOne(id);
		Long hate=reply.getHate();
		hate+=1;
		reply.setHate(hate);
		replyRepository.save(reply);
		
	}

	@Override
	public List<Reply> ListReplyByUserid(Long userid) {
		// TODO Auto-generated method stub
		return replyRepository.findByUserid(userid);
	}

	@Override
	public void saveReply(Reply reply) {
		replyRepository.save(reply);
		
	}
	
}
