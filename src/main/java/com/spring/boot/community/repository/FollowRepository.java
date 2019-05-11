package com.spring.boot.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>{
	
	List<Follow> findByUserid(Long user_id);
	
	List<Follow> findByFollowuserid(Long followuser_id);
	
	Follow findByUseridAndFollowuserid(Long user_id,Long followuser_id);
}
