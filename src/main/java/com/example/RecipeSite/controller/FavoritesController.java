package com.example.RecipeSite.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.RecipeSite.entity.Favorite;
import com.example.RecipeSite.entity.Recipe;
import com.example.RecipeSite.entity.UserInf;
import com.example.RecipeSite.form.RecipeForm;
import com.example.RecipeSite.repository.FavoriteRepository;

import jakarta.transaction.Transactional;

@Controller
public class FavoritesController {
	
	@Autowired
	private FavoriteRepository repository;
	@Autowired
	private RecipesController recipesController;
	
	@GetMapping("/favorites")
	public String index(Principal principal, Model model) throws IOException {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		List<Favorite> recipes = repository.findByUserIdOrderByUpdatedAtDesc(user.getUserId());
		List<RecipeForm> list = new ArrayList<>();
		for(Favorite entity : recipes) {
			Recipe recipeEntity = entity.getRecipe();
			RecipeForm form = recipesController.getRecipe(user, recipeEntity);
			list.add(form);
		}
		model.addAttribute("list", list);
		
		return "recipes/index";
	}
	
	@PostMapping("/favorite")
	public String create(Principal principal, @RequestParam("recipe_id") long recipeId,
			RedirectAttributes redirAttrs) {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		Long userId = user.getUserId();
		List<Favorite> results = repository.findByUserIdAndRecipeId(userId, recipeId);
		if (results.size() == 0) {
			Favorite entity = new Favorite();
			entity.setUserId(userId);
			entity.setRecipeId(recipeId);
			repository.saveAndFlush(entity);
			
			redirAttrs.addFlashAttribute("hasMessage", true);
			redirAttrs.addFlashAttribute("class", "alert-info");
			redirAttrs.addFlashAttribute("message", "お気に入りに登録しました" );
		}
		
		return "redirect:/recipes";
	}
	
	@DeleteMapping("/favorite")
	@Transactional
	public String destroy(Principal principal, @RequestParam("recipe_id") long recipeId,
			RedirectAttributes redirAttrs) {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		Long userid = user.getUserId();
		List<Favorite> results = repository.findByUserIdAndRecipeId(userid, recipeId);
		if(results.size() == 1) {
			repository.deleteByUserIdAndRecipeId(user.getUserId(), recipeId);
			
			redirAttrs.addFlashAttribute("hasMessage", true);
			redirAttrs.addFlashAttribute("class", "alert-info");
			redirAttrs.addFlashAttribute("message", "お気に入りを削除しました");
		}
		return "redirect:/recipes";
	}

}
