package com.spring.boot.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.User;

public interface ArticleRepository extends JpaRepository<Article, Long>{
	
	
	Page<Article> findByTypeAndTitleLikeOrderByCreateTimeDesc(int type, String title, Pageable pageable);
	
	Page<Article> findByTypeAndUserAndTitleLikeOrderByCreateTimeDesc(int type, User user,String title, Pageable pageable);
	
	Page<Article> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);
	
	//按时间顺序以及Title返回用户文章
	Page<Article> findByTitleLikeOrderByCreateTimeDesc(String title, Pageable pageable);
	
	//按时间顺序以及Status返回用户文章
	Page<Article> findByStatusOrderByCreateTimeDesc(int status, Pageable pageable);
	
	Page<Article> findByStatus(int status, Pageable pageable);
	
	//根据用户返回文章
	List<Article> findByUser(User user);
	
	List<Article> findByStatusOrderByCreateTimeDesc(int status);
	
	List<Article> findByIstop(Boolean istop);
	
	List<Article> findByTypeAndStatusOrderByCreateTimeDesc(int type,int status);
	
	Page<Article> findByTypeAndStatusOrderByCreateTimeDesc(int type,int status,Pageable pageable);
	
	 @Query(nativeQuery=true, value = "select * from article where type=:type and status=1 ORDER BY create_time desc LIMIT :startIndex,:displayLength")
	List<Article> findPage(@Param("type")int type,@Param("startIndex")int startIndex, @Param("displayLength")int displayLength);
	 
	 @Query(nativeQuery=true, value = "select * from article where status=1 ORDER BY create_time desc LIMIT :startIndex,:displayLength")
	List<Article> findPageAllType(@Param("startIndex")int startIndex, @Param("displayLength")int displayLength);
	
	
}
