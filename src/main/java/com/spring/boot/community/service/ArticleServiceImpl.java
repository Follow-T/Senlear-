package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.Article;
import com.spring.boot.community.domin.Comment;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.repository.ArticleRepository;


@Service
public  class ArticleServiceImpl implements ArticleService{

	@Autowired
	private ArticleRepository articleRepository;
	
	@Transactional
	@Override
	public Article save(Article article) {
		articleRepository.save(article);
		return article;
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		articleRepository.deleteById(id);
	}
	
	@Transactional
	@Override
	public Article update(Article article) {
		articleRepository.save(article);
		return article;
	}
	
	@Override
	public Article getArticleById(Long id) {
		return articleRepository.getOne(id);
	}

	@Override
	public Page<Article> listArticlesByTitleLike(User user, String title, Pageable pageable) {
		// ģ����ѯ
		title = "%" + title + "%";
		Page<Article> articles = articleRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
		return articles;
	}
	
	@Override
	public Page<Article> listArticlesByTitleLikeAndSort(User user, String title, Pageable pageable) {
		title = "%" + title + "%";
		Page<Article> articles = articleRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
		return articles;
	}
	
	@Transactional
	@Override
	public void readingIncrease(Long id) {
		Article article=articleRepository.getOne(id);
		Long reading=article.getReading();
		reading++;
		article.setReading(reading);
		articleRepository.save(article);
		
	}

	@Override
	public Page<Article> listArticlesByTitleLike(String title, Pageable pageable) {
		title = "%" + title + "%";
		Page<Article> articles=articleRepository.findByTitleLikeOrderByCreateTimeDesc(title,pageable);
		return articles;
	}


	@Override
	public Page<Article> listArticles(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	@Override
	public List<Article> listArticleByUser(User user) {
		return articleRepository.findByUser(user);
	}

	@Override
	@Transactional
	public Article CreateComment(Long articleid, String content, String userid, String useravatar,String username,String name) {
		Article article=articleRepository.getOne(articleid);
		Comment comment=new Comment(content, userid, useravatar,username,name);
		article.addComment(comment);
		return articleRepository.save(article);
	}

	@Override
	@Transactional
	public void RemoveComment(Long articleid, Long commentid) {
		Article article=articleRepository.getOne(articleid);
		article.removeComment(commentid);
	}

	

	@Override
	public Page<Article> listArticlesByTypeAndTitleLike(int type, String title, Pageable pageable) {
		title = "%" + title + "%";
		Page<Article> articles=articleRepository.findByTypeAndTitleLikeOrderByCreateTimeDesc(type, title, pageable);
		return articles;

	}

	@Override
	public Page<Article> listArticlesByUserAndTitleLike(User user, String title, Pageable pageable) {
		title = "%" + title + "%";
		Page<Article> articles=articleRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user, title, pageable);
		return articles;
	}

	@Override
	public Page<Article> listArticlesByTypeAndUserAndTitleLike(int type, String title, Pageable pageable, User user) {
		title = "%" + title + "%";
		Page<Article> articles=articleRepository.findByTypeAndUserAndTitleLikeOrderByCreateTimeDesc(type, user, title, pageable);
		return articles;
	}

	@Override
	public Page<Article> listArticlesByStatus(int status, Pageable pageable) {
		Page<Article> articles=articleRepository.findByStatusOrderByCreateTimeDesc(status, pageable);
		return articles;
	}

	@Override
	public List<Article> listArticles() {	
		return articleRepository.findAll();
	}

	@Override
	public List<Article> listArticleByIstop(Boolean istop) {
		return articleRepository.findByIstop(istop);
	}

	@Override
	public List<Article> listArticleByTypeAndStatus(int type,int status) {
		return articleRepository.findByTypeAndStatusOrderByCreateTimeDesc(type, status);
	}

	@Override
	public Page<Article> listArticleByTypeAndStatusSortByHot(int type, int status, Pageable pageable) {
		Page<Article> articles=articleRepository.findByTypeAndStatusOrderByCreateTimeDesc(type, status, pageable);
		return articles;
	}

	@Override
	public List<Article> listArticleByStatus(int status) {
		
		return articleRepository.findByStatusOrderByCreateTimeDesc(status);
	}

	@Override
	public Page<Article> listArticleByStatus(int status, Pageable pageable) {
		Page<Article> articles=articleRepository.findByStatus( status, pageable);
		return articles;
	}

	@Override
	public List<Article> listArticlesBypageAndType(int type, int startIndex, int displayLength) {
		
		return articleRepository.findPage(type, startIndex, displayLength);
	}

	@Override
	public List<Article> listArticlesBypageAllType(int startIndex, int displayLength) {
		
		return articleRepository.findPageAllType(startIndex, displayLength);
	}

	

	

}
