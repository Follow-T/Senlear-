package com.spring.boot.community.domin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;


@Entity
public class Comment {
	
	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id; // 用户的唯一标识
	
	@Size(min=2, max=500)
	@Column(nullable = false,name="comment_content")
	private String content;
	
	@Column(nullable = false,name="user_id")
	private String userid;
	
	@Column(nullable = false,name="user_username")
	private String username;

	@Column(nullable = false,name="user_name")
	private String name;
	
	@Column(nullable = false,name="user_avatar")
	private String useravatar="https://tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg";
	
	@Column(nullable = false) 
	@org.hibernate.annotations.CreationTimestamp  
	private Timestamp createTime;
	
	@Column(name="user_like")
	private Long like=0L;
	
	@Column(name="user_hate")
	private Long hate=0L;
	
	
	public Integer getReplySize() {
		return replySize;
	}

	public void setReplySize(Integer replySize) {
		this.replySize = replySize;
	}

	public Set<Reply> getReplies() {
		return replies;
	}
	
	public List<Reply> getListReplies() {
		Set<Reply> replies = this.getReplies();
		List<Reply> replyList = new ArrayList<>(replies);
		Collections.sort(replyList, new Comparator<Reply>() {
            @Override
            public int compare(Reply o1, Reply o2) {
                //升序
                return o1.getId().compareTo(o2.getId());
            }
            
		});
		return replyList;
	}
	
	public void setReplies(Set<Reply> replies) {
		this.replies = replies;
	}

	@Column(name="replySize")
	private Integer replySize;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "comment_reply", joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "reply_id", referencedColumnName = "id"))
	private Set<Reply> replies;
	
	

	protected Comment() {
		
	}
	
	public Comment(String content,String userid,String useravatar,String username,String name) {
		this.content=content;
		this.userid=userid;
		this.useravatar=useravatar;
		this.username=username;
		this.name=name;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
	
	public void addReply(Reply reply) {
		this.replies.add(reply);
		this.replySize = this.replies.size();
	}
	
	public void removeReply(Long replyId) {
		for (Reply reply:replies) {
			if (reply.getId() == replyId) {
				this.replies.remove(reply);
				break;
			}
		}
		this.replySize = this.replies.size();
	}
}
