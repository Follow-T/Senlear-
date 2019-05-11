package com.spring.boot.community.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.spring.boot.community.domin.User;
import com.spring.boot.community.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService,UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public User saveUser(User user) {
		
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}
	
	@Transactional
	@Override
	public void removeUser(Long id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
		
	}
	
	@Transactional
	@Override
	public void removeUsersInBatch(List<User> users) {
		// TODO Auto-generated method stub
		userRepository.deleteInBatch(users);
	}
	
	@Transactional
	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.getOne(id);
	}

	@Override
	public List<User> listUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public Page<User> listUsersByNameLike(String name, Pageable pageable) {
		// TODO Auto-generated method stub
		
		name = "%" + name + "%";
		Page<User> users = userRepository.findByNameLike(name, pageable);
		return users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		
		return userRepository.findByUsername(username);
	}

	@Override
	public Page<User> listUsersByHost(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

}
