package com.spring.boot.community.domin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Follow {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable=false,name="user_id")
	Long userid;
	
	@Column(nullable=false,name="followuser_id")
	Long followuserid;
	
	public Follow(Long user_id,Long followuser_id) {
		this.userid=user_id;
		this.followuserid=followuser_id;
	}
	
	protected Follow() {
		
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long user_id) {
		this.userid = user_id;
	}

	public Long getFollowuserid() {
		return followuserid;
	}

	public void setFollowuserid(Long followuser_id) {
		this.followuserid = followuser_id;
	}
}
