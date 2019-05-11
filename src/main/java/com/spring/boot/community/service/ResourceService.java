package com.spring.boot.community.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Resource;
import com.spring.boot.community.domin.User;


public interface ResourceService {
	
	Resource save(Resource resource);
	
	void delete(Long id);
	
	Resource update(Resource resoruce);
	
	Resource getResoruceById(Long id);

	Page<Resource> listResoruces(Pageable pageable);
	
	Page<Resource> listResorucesByTitleLikeAndSort(String title, Pageable pageable);
	
	List<Resource> listResoruceByUser(Long userid);
	
	Resource CreateComment(Long resourceid, String content, String userid, String useravatar, String username, String name);
	
	void RemoveComment(Long resourceid, Long commentid);
	
	void readingIncrease(Long id);
}

