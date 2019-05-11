package com.spring.boot.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.boot.community.domin.Authority;






public interface AuthorityRepository extends JpaRepository<Authority, Long>{
	
}
