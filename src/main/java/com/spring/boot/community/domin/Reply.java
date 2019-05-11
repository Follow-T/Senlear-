package com.spring.boot.community.domin;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Reply {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id; // 
	
	@Size(min=2, max=500)
	@Column(nullable = false,name="reply_content")
	private String content;
	
	@Column(nullable = false,name="touser_id")
	private Long touserid;
	
	@Column(nullable = false,name="touser_username")
	private String tousername;

	@Column(nullable = false,name="touser_name")
	private String toname;

	@Column(nullable = false,name="touser_avatar")
	private String touseravatar="//tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg";
	
	@Column(nullable = false,name="user_id")
	private Long userid;
	
	@Column(nullable = false,name="user_username")
	private String username;

	@Column(nullable = false,name="user_name")
	private String name;

	@Column(nullable = false,name="user_avatar")
	private String useravatar="//tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg";
	
	@Column(nullable = false) 
	@org.hibernate.annotations.CreationTimestamp  
	private Timestamp createTime;
	
	@Column(name="user_like")
	private Long like=0L;
	
	public Long getTouserid() {
		return touserid;
	}

	public void setTouserid(Long touserid) {
		this.touserid = touserid;
	}

	public String getTousername() {
		return tousername;
	}

	public void setTousername(String tousername) {
		this.tousername = tousername;
	}

	public String getToname() {
		return toname;
	}

	public void setToname(String toname) {
		this.toname = toname;
	}

	public String getTouseravatar() {
		return touseravatar;
	}

	public void setTouseravatar(String touseravatar) {
		this.touseravatar = touseravatar;
	}

	@Column(name="user_hate")
	private Long hate=0L;
	
	protected Reply() {
		
	}
	
	public Reply(String content,Long userid,String useravatar,String username,String name,Long touserid,String touseravatar,String tousername,String toname) {
		this.content=content;
		this.userid=userid;
		this.useravatar=useravatar;
		this.username=username;
		this.name=name;
		this.touserid=touserid;
		this.touseravatar=touseravatar;
		this.tousername=tousername;
		this.toname=toname;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getUseravatar() {
		return useravatar;
	}

	public void setUseravatar(String useravatar) {
		this.useravatar = useravatar;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getLike() {
		return like;
	}

	public void setLike(Long like) {
		this.like = like;
	}

	public Long getHate() {
		return hate;
	}

	public void setHate(Long hate) {
		this.hate = hate;
	}
}
