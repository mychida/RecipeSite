package com.example.RecipeSite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RecipeSite.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

}
