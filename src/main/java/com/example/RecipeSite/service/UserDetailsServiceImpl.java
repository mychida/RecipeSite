package com.example.RecipeSite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.RecipeSite.entity.User;
import com.example.RecipeSite.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private  UserRepository repository;
	protected static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("username={}", username);
		
		if(username == null || "".equals(username))	{
			throw new UsernameNotFoundException("Usernameis empty");
		}
		User entity = repository.findByUsername(username)
;
		return entity;
	}
}
