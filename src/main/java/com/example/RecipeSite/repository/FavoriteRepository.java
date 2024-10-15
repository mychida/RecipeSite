package com.example.RecipeSite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RecipeSite.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	
	public Optional<Favorite> findById(Long id);
	
	public List<Favorite> findByUserIdOrderByUpdatedAtDesc(Long userId);
	
	public List<Favorite> findByUserIdAndRecipeId(Long userId, Long recipeId);
	
	public void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

}
