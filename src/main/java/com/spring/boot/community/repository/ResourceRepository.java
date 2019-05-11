package com.spring.boot.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.boot.community.domin.Resource;
import com.spring.boot.community.domin.User;


public  interface ResourceRepository extends JpaRepository<Resource, Long>{
	
	//按时间顺序以及Title返回用户文章
	Page<Resource> findByTitleLikeOrderByCreateTimeDesc(String title, Pageable pageable);
		
	//根据用户返回资源
	List<Resource> findByUserid(Long userid);
	
	 
	
}
