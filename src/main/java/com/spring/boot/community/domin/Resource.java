package com.spring.boot.community.domin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.apache.commons.codec.language.bm.Rule;

@Entity
public class Resource {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Column(nullable = false,name="resource_title")
	private String title;
	
	@Column(nullable = false,name="resource_content")
	private String content;
	
	@Column(nullable = false,name="resource_url")
	private String url;
	
	@Column(nullable = false,name="resource_status")
	private String status;
	

	@Column(nullable = false,name="user_id")
	private Long userid;
	
	@Column(nullable = false,name="user_name")
	private String name;
	
	@Column(nullable = false,name="user_username")
	private String username;
	
	@Column(nullable = false,name="user_avatar")
	private String useravatar;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "resource_comment", joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
	private List<Comment> comments;

	@Column(nullable = false) 
	@org.hibernate.annotations.CreationTimestamp  
	private Timestamp createTime;
	
	@Column(name="resource_reading")
	Long reading=0L;
	
	@Column(name="resource_commentsize")
	Long commentsize;
	
	@Column(name="resource_likes")
	Long likes=0L;
	
	protected Resource() {
		
	}
	
	public Resource(String title,String content,String url,String status,Long userid ,String username,String name,String useravatar) {
		this.title=title;
		this.content=content;
		this.url=url;
		this.userid=userid;
		this.username=username;
		this.name=name;
		this.useravatar=useravatar;
		this.status=status;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getUseravatar() {
		return useravatar;
	}

	public void setUseravatar(String useravatar) {
		this.useravatar = useravatar;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getCommentsize() {
		return commentsize;
	}

	public void setCommentsize(Long commentsize) {
		this.commentsize = commentsize;
	}


	public List<Comment> getComments() {
		return comments;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getReading() {
		return reading;
	}

	public void setReading(Long reading) {
		this.reading = reading;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public void setComment(List<Comment> comment) {
		this.comments = comment;
		this.commentsize=(long) comment.size();
	}
	
	//添加评论
	public void addComment(Comment comment) {
		this.comments.add(comment);
		this.commentsize = (long) this.comments.size();
	}
	
	//删除评论
	public void removeComment(Long commentId) {
		for(Comment comment:comments) {
			if(comment.getId()==commentId) {
				comments.remove(comment);
				break;
			}
		}
		this.commentsize = (long) this.comments.size();
	}
}
