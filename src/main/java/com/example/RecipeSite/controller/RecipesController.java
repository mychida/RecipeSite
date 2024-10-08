package com.example.RecipeSite.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.RecipeSite.entity.Recipe;
import com.example.RecipeSite.entity.UserInf;
import com.example.RecipeSite.form.RecipeForm;
import com.example.RecipeSite.form.UserForm;
import com.example.RecipeSite.repository.RecipeRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RecipesController {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RecipeRepository repository;
	@Autowired
	private HttpServletRequest request;
	
	@GetMapping("/recipes")
	public String index(Principal principal, Model model) throws IOException {
		Authentication authentication = (Authentication) principal;
		UserInf user = (UserInf) authentication.getPrincipal();
		
		List<Recipe> recipes = repository.findAllByOrderByUpdatedAtDesc();
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
		
		RecipeForm form = modelMapper.map(entity, RecipeForm.class);
		
		UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
		form.setUser(userForm);
		 
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
		entity.setDescription(form.getDescription());
		entity.setTag(form.getTag());
		repository.saveAndFlush(entity);
		
		redirAttrs.addFlashAttribute("hasMessage", true);
		redirAttrs.addFlashAttribute("class", "alert-info");
		redirAttrs.addFlashAttribute("message", "投稿に成功しました");
		
		return "redirect:/recipes";
	}
}
