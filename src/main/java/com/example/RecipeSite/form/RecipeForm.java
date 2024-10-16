package com.example.RecipeSite.form;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecipeForm {
	
	private Long id;
	
	private Long userId;
	
	@NotEmpty
	private String recipeName;
	
	@NotEmpty
	private String serving;
	
	@NotEmpty
	@Size(max = 1000)
	private String materials;
	
	@NotEmpty
	@Size(max = 1000)
	private String processes;
	
	@Size(max = 1000)
	private String description;
	
	@NotEmpty
	@Size(max = 30)
	private String tag;
	
	private UserForm user;
	
	private List<FavoriteForm> favorites;
	
	private FavoriteForm favorite;

}
