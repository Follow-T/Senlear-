package com.spring.boot.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.boot.community.domin.CollectArticle;

public interface CollectArticleRepository extends JpaRepository<CollectArticle,Long>{
	
	List<CollectArticle> findByUserid(Long userid);
	
	
	CollectArticle findByUseridAndArticleid(Long userid,Long articleid);
}
