package com.spring.boot.community.domin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class User implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	Long id;
	
	@Column(nullable = false,name="user_username")
	String username;
	
	@Column(name="user_email")
	String email;
	
	@Column(nullable = false,name="user_name")
	String name;
	
	@Column(nullable = false,name="user_password")
	String password;
	
	@Column(name="user_sex")
	String sex;
	
	@Column(name="user_sign")
	String sign;
	
	@Column(name="user_info")
	String info;
	
	@Column(name="user_address")
	String address;
	
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@Column(name="user_level")
	String level;
	
	@Column(name="user_phonenumber")
	String phonenumber;
	
	@Column(name="user_status")
	int status=1;
	
	@Column(name="user_role")
	int role=3;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	@Column(name="user_online")
	String online;
	
	@Column(name="user_scan")
	int scan=0;
	
	@Column(name="user_articlenum")
	int articlenum=0;
	
	public int getScan() {
		return scan;
	}

	public void setScan(int scan) {
		this.scan = scan;
	}

	public int getArticlenum() {
		return articlenum;
	}

	public void setArticlenum(int articlenum) {
		this.articlenum = articlenum;
	}

	@Column(nullable = false) 
	@org.hibernate.annotations.CreationTimestamp  
	private Timestamp createTime;

	@Column(nullable = false,name="user_avatar")
	private String avatar="//tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg";

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;
	
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	protected User(){
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public User(String username,String name,String password)
	{
		this.username=username;
		this.email=username;
		this.name=name;
		this.password=password;
	}
	public User(String username,String name,String password,int type)
	{
		this.username=username;
		this.name=name;
		this.password=password;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.email=username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setEncodePassword(String password) {
		PasswordEncoder  encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(password);
		this.password = encodePasswd;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
		for(com.spring.boot.community.domin.Authority authority : this.authorities){
			simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		}
		return simpleAuthorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, username='%s', name='%s', email='%s', password='%s']", id, username, name, email,
				password);
	}
	
}
