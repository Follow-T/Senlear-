package com.spring.boot.community.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.User;


public interface ArticleService {
	

	Article save(Article article);
	

	void delete(Long id);
	

	Article update(Article article);
	

	Article getArticleById(Long id);
	

	Page<Article> listArticles(Pageable pageable);
	

	Page<Article> listArticlesByTitleLike(User user, String title, Pageable pageable);
	

	Page<Article> listArticlesByTitleLikeAndSort(User user, String title, Pageable pageable);
	

	Page<Article> listArticlesByTitleLike(String title, Pageable pageable);
	
	Page<Article> listArticlesByStatus(int status, Pageable pageable);
	
	Page<Article> listArticlesByUserAndTitleLike(User user,String title,Pageable pageable);
	
	Page<Article> listArticlesByTypeAndTitleLike(int type,String title,Pageable pageable);
	
	Page<Article> listArticlesByTypeAndUserAndTitleLike(int type,String title,Pageable pageable,User user);
	
	Page<Article> listArticleByStatus(int status,Pageable pageable);
	
	List<Article> listArticleByUser(User user);
	
	List<Article> listArticleByStatus(int status);
	
	List<Article> listArticleByIstop(Boolean istop);
	
	List<Article> listArticleByTypeAndStatus(int type,int status);
	
	Page<Article> listArticleByTypeAndStatusSortByHot(int type,int status,Pageable pageable);
	
	List<Article> listArticles();
	
	Article CreateComment(Long articleid,String content,String userid,String useravatar,String username,String name);
	
	void RemoveComment(Long articleid, Long commentid);
	
	void readingIncrease(Long id);
	
	List<Article> listArticlesBypageAndType(int type,int startIndex,int displayLength);
	
	List<Article> listArticlesBypageAllType(int startIndex,int displayLength);
}
