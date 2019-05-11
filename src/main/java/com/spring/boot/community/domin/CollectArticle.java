package com.spring.boot.community.domin;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CollectArticle {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@Column(nullable=false,name="user_id")
	Long userid;
	
	@Column(nullable=false,name="article_id")
	Long articleid;
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(nullable = false) 
	@org.hibernate.annotations.CreationTimestamp  
	private Timestamp createTime;
	
	public CollectArticle(Long userid,Long articleid) {
		this.userid=userid;
		this.articleid=articleid;
	}
	
	protected CollectArticle() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getArticleid() {
		return articleid;
	}

	public void setArticleid(Long articleid) {
		this.articleid = articleid;
	}
}
