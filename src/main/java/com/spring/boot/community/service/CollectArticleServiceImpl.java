package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.CollectArticle;
import com.spring.boot.community.repository.CollectArticleRepository;

@Service
public class CollectArticleServiceImpl implements CollectArticleService{

	@Autowired
	private CollectArticleRepository collectArticleRepository;
	
	@Override
	public List<CollectArticle> listCollectArticleByUser_id(Long userid) {
		return collectArticleRepository.findByUserid(userid);
	}

	@Override
	@Transactional
	public void removeCollectArticle(Long userid) {
		collectArticleRepository.deleteById(userid);
	}

	@Override
	@Transactional
	public void saveCollectArticle(CollectArticle collectArticle) {
		collectArticleRepository.save(collectArticle);
	}

	@Override
	public CollectArticle getCollectArticle(Long userid, Long articleid) {
		
		return collectArticleRepository.findByUseridAndArticleid(userid, articleid);
	}

}
