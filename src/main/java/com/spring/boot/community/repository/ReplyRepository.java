package com.spring.boot.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Reply;


public interface ReplyRepository extends JpaRepository<Reply, Long>{
	List<Reply> findByUserid(Long userid);
}
