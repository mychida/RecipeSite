package com.example.RecipeSite.form;

import lombok.Data;

@Data
public class FavoriteForm {
	
	private Long userId;
	
	private Long recipeId;
	
	private RecipeForm recipe;

}
