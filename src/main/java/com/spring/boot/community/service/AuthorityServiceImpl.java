package com.spring.boot.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.community.domin.Authority;
import com.spring.boot.community.repository.AuthorityRepository;



@Service
public class AuthorityServiceImpl  implements AuthorityService {
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Override
	public Authority getAuthorityById(Long id) {
		return authorityRepository.getOne(id);
	}

}
