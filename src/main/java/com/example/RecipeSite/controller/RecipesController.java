package com.example.RecipeSite.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.RecipeSite.entity.Favorite;
import com.example.RecipeSite.entity.Recipe;
import com.example.RecipeSite.entity.UserInf;
import com.example.RecipeSite.form.FavoriteForm;
import com.example.RecipeSite.form.RecipeForm;
import com.example.RecipeSite.form.UserForm;
import com.example.RecipeSite.repository.RecipeRepository;

@Controller
public class RecipesController {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RecipeRepository repository;
	
	@GetMapping("/recipes")
	public String index(Principal principal, Model model) throws IOException {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		
		List<Recipe> recipes = repository.findByDeletedFalseOrderByUpdatedAtDesc();
		List<RecipeForm> list = new ArrayList<>();
		for(Recipe entity : recipes) {
			RecipeForm form = getRecipe(user, entity);
			list.add(form);
		}
		model.addAttribute("list", list);
		
		return "recipes/index";
	}
	
	public RecipeForm getRecipe(UserInf user, Recipe entity) throws IOException {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.typeMap(Recipe.class, RecipeForm.class).addMappings(mapper ->
				mapper.skip(RecipeForm::setUser));
		modelMapper.typeMap(Recipe.class, RecipeForm.class).addMappings(mapper ->
				mapper.skip(RecipeForm::setFavorites));
		modelMapper.typeMap(Favorite.class, FavoriteForm.class).addMappings(mapper ->
				mapper.skip(FavoriteForm::setRecipe));
		
		RecipeForm form = modelMapper.map(entity, RecipeForm.class);
		
		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);
		
		List<FavoriteForm> favorites = new ArrayList<FavoriteForm>();
		for(Favorite favoriteEntity : entity.getFavorites()) {
			FavoriteForm favorite= modelMapper.map(favoriteEntity, FavoriteForm.class);
			favorites.add(favorite);
			if(user.getUserId().equals(favoriteEntity.getUserId())) {
				form.setFavorite(favorite);
			}
		}
		form.setFavorites(favorites);
		 
		return form;
	
	}
	@GetMapping("/recipes/new")
	public String newRecipe(Model model) {
		model.addAttribute("form", new RecipeForm());
		return "recipes/new";
	}

	@PostMapping("/recipes")
	public String create(Principal principal, @Validated @ModelAttribute("form") RecipeForm form,
			BindingResult result, Model model, RedirectAttributes redirAttrs) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "投稿に失敗しました");
			return "recipes/new";
		}
		
		Recipe entity = new Recipe();
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		entity.setUserId(user.getUserId());
		entity.setRecipeName(form.getRecipeName());
		entity.setServing(form.getServing());
		entity.setMaterials(form.getMaterials());
		entity.setProcesses(form.getProcesses());
		entity.setDescription(form.getDescription());
		entity.setTag(form.getTag());
		repository.saveAndFlush(entity);
		
		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", "投稿に成功しました");
		
		return "redirect:/recipes";
	}
	
	/*
	 * 編集する投稿を表示する
	 */
	@GetMapping("/edit")
	public String edit(Principal principal, @RequestParam Long id,  RecipeForm form, Model model) throws IOException {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		
		Optional<Recipe>  optionalRecipe = repository.findById(id);
		Recipe recipe = optionalRecipe.get();
		model.addAttribute("form", recipe);
		model.addAttribute("path", "update");
		return "recipes/edit";
	}
	/*
	 * 更新する
	 */
	@PostMapping("/update")
	public String update(Principal principal, @Validated @ModelAttribute("form") RecipeForm form,
			BindingResult result, Model model, RedirectAttributes redirAttrs) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "更新に失敗しました");
			return "recipes/index"; 
		}
		
		Optional<Recipe> optionalRecipe = repository.findById(form.getId());
		Recipe entity = optionalRecipe.get();
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		entity.setUserId(user.getUserId());
		entity.setRecipeName(form.getRecipeName());
		entity.setServing(form.getServing());
		entity.setMaterials(form.getMaterials());
		entity.setProcesses(form.getProcesses());
		entity.setDescription(form.getDescription());
		entity.setTag(form.getTag());
		repository.saveAndFlush(entity);
		
		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", "更新に成功しました");
		
		return "redirect:/recipes";
	}
	
	/*
	 * 削除する
	 */
	@GetMapping("/delete")
	public String delete(Principal principal, @RequestParam Long id, Recipe form, 
			BindingResult result, Model model, RedirectAttributes redirAttrs) throws IOException {
		if (result.hasErrors()) {
			model.addAttribute("hasMessage", true);
			model.addAttribute("class", "alert-danger");
			model.addAttribute("message", "削除に失敗しました");
			return "recipes/index";
		}
		
		Optional<Recipe> entity = repository.findById(form.getId());
		Recipe recipe = entity.get();
		//recipe.setDeleted(true);
		recipe.setDeleted(true);
		Date current = new Date();
		recipe.setUpdatedAt(current);
		repository.saveAndFlush(recipe);
		
		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", "削除に成功しました");
		
		return "redirect:/recipes";
	}
}
