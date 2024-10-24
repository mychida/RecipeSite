package com.example.RecipeSite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RecipeSite.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>{
	
	List<Recipe> findAllByOrderByUpdatedAtDesc();
	
	/*
	 * 編集するためのメソッド:IDで検索して編集するレシピを探す
	 */
	public Optional<Recipe> findById(Long id);

	//論理削除したものを表示させない
	List<Recipe> findByDeletedFalseOrderByUpdatedAtDesc();
}
