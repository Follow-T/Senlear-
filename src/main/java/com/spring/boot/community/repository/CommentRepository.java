package com.spring.boot.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.boot.community.domin.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByUserid(String userid);
}
