package com.spring.boot.community.service;

import java.util.List;

import com.spring.boot.community.domin.CollectArticle;



public interface CollectArticleService {
	
	List<CollectArticle> listCollectArticleByUser_id(Long userid);
	
	void removeCollectArticle(Long userid);
	
	void saveCollectArticle(CollectArticle collectArticle);
	
	CollectArticle getCollectArticle(Long userid,Long articleid);
}
