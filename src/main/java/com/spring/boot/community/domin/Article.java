package com.spring.boot.community.domin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Article {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@Column(nullable = false, length = 50) 
	String title;
	
	@Column(nullable = false) 
	String summary;
	
	@Lob  
	//@Basic(fetch=FetchType.LAZY) 
	@Column(nullable = false)
	String content;

	@Column
	Boolean hasResource=false;

	public String getResourceurl() {
		return resourceurl;
	}

	public void setResourceurl(String resourceurl) {
		this.resourceurl = resourceurl;
	}

	@Column
	String resourceurl;

	//, fetch = FetchType.LAZY
	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="user_id")
	User user;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "article_comment", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
	private Set<Comment> comments;

	@Column(nullable = false) 
	@org.hibernate.annotations.CreationTimestamp  
	private Timestamp createTime;
	
	@Column
	Long reading=0L;
	
	@Column
	Long commentsize=0L;
	
	@Column
	Long likes=0L;
	
	@Column
	int type=0;
	
	@Column
	Boolean istop=false;
	
	public Boolean getIstop() {
		return istop;
	}

	public void setIstop(Boolean istop) {
		this.istop = istop;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column
	int status=0;
	
	@Column
	int classify=0;
	
	public int getClassify() {
		return classify;
	}

	public void setClassify(int classify) {
		this.classify = classify;
	}

	@Column
	Boolean ispublish;
	
	@Column
	Boolean iscollect;
	
	
	
	public Boolean getIscollect() {
		return iscollect;
	}

	public void setIscollect(Boolean iscollect) {
		this.iscollect = iscollect;
	}

	protected Article() {
		// TODO Auto-generated constructor stub
	}
	
	public Article(String title,String summary,String content)
	{
		this.title=title;
		this.summary=summary;
		this.content=content;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Long getCommentsize() {
		return commentsize;
	}

	public void setCommentsize(Long commentsize) {
		this.commentsize = commentsize;
	}
	
	public Set<Comment> getComment() {
		return comments;
	}

	public Boolean getHasResource() {
		return hasResource;
	}

	public void setHasResource(Boolean hasResource) {
		this.hasResource = hasResource;
	}

	public void setComment(Set<Comment> comment) {
        if(comment!=null) {
            this.comments = comment;
            this.commentsize=(long) comment.size();
        }
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
	
	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean getIspublish() {
		return ispublish;
	}

	public void setIspublish(Boolean ispublish) {
		this.ispublish = ispublish;
	}
	
}
