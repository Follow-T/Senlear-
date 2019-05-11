package com.spring.boot.community.service;

import java.util.List;

import com.spring.boot.community.domin.Follow;

public interface FollowService {
	
	List<Follow> listFollowByUser_id(Long user_id);
	
	List<Follow> listFollowByFollowUser_id(Long followuser_id);
	
	void removeFollow(Long user_id);
	
	void saveFollow(Follow follow);
	
	Follow getFolloew(Long user_id,Long followuser_id);
}