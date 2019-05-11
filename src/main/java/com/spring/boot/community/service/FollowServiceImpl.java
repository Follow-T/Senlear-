package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.Follow;
import com.spring.boot.community.repository.FollowRepository;

@Service
public class FollowServiceImpl implements FollowService{

	@Autowired
	private FollowRepository followRepository;
	@Override
	public List<Follow> listFollowByUser_id(Long user_id) {	
		return followRepository.findByUserid(user_id);
	}

	@Override
	@Transactional
	public void removeFollow(Long user_id) {
		followRepository.deleteById(user_id);
	}

	@Override
	@Transactional
	public void saveFollow(Follow follow) {
		followRepository.save(follow);
	}

	@Override
	public Follow getFolloew(Long user_id, Long followuser_id) {
		
		return followRepository.findByUseridAndFollowuserid(user_id, followuser_id);
	}

	@Override
	public List<Follow> listFollowByFollowUser_id(Long followuser_id) {
	
		return followRepository.findByFollowuserid(followuser_id);
	}

}
