package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.Resource;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.repository.ResourceRepository;
import com.spring.boot.community.service.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService{
	
	@Autowired
	private ResourceRepository resourceRepository;
	
	
	@Transactional
	@Override
	public Resource save(Resource resource) {
		
		return resourceRepository.save(resource);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		
		resourceRepository.deleteById(id);
	}
	
	@Transactional
	@Override
	public Resource update(Resource resoruce) {
		return resourceRepository.save(resoruce);
	}

	@Override
	public Resource getResoruceById(Long id) {
		return resourceRepository.getOne(id);
	}

	@Override
	public Page<Resource> listResoruces(Pageable pageable) {
		return resourceRepository.findAll(pageable);
	}

	@Override
	public Page<Resource> listResorucesByTitleLikeAndSort( String title, Pageable pageable) {
		title = "%" + title + "%";
		Page<Resource> resoruces=resourceRepository.findByTitleLikeOrderByCreateTimeDesc(title,pageable);
		return resoruces;
		
	}

	@Override
	public List<Resource> listResoruceByUser(Long userid) {
		return resourceRepository.findByUserid(userid);
	}

	@Override
	@Transactional
	public Resource CreateComment(Long resourceid, String content, String userid, String useravatar, String username,
			String name) {
		Resource resource=resourceRepository.getOne(resourceid);
		Comment comment=new Comment(content, userid, useravatar,username,name);
		resource.addComment(comment);
		return resourceRepository.save(resource);
	}

	@Override
	@Transactional
	public void RemoveComment(Long resourceid, Long commentid) {
		Resource resource=resourceRepository.getOne(resourceid);
		resource.removeComment(commentid);
	}

	@Override
	@Transactional
	public void readingIncrease(Long id) {
		Resource resource=resourceRepository.getOne(id);
		Long likes=resource.getLikes();
		likes++;
		resource.setLikes(likes);
		resourceRepository.save(resource);	
	}
	
}
