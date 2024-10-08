package com.example.RecipeSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RecipeSite.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>{
	
	List<Recipe> findAllByOrderByUpdatedAtDesc();

}
