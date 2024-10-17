package com.example.RecipeSite.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "recipes")
@Data
@EqualsAndHashCode(callSuper = false)
public class Recipe extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "recipe_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(name = "recipe_name", nullable = false)
	private String recipeName;
	
	@Column(nullable = false)
	private String serving;
	
	@Column(nullable = false, length = 1000)
	private String materials;
	
	@Column(nullable = false, length = 1000)
	private String processes;
	
	@Column(nullable = false, length = 1000)
	private String description;
	
	@Column(nullable = false, length = 30)
	private String  tag;
	
	@ManyToOne
	@JoinColumn(name = "userId", insertable = false, updatable = false)
	private User user; 
	
	@OneToMany
	@JoinColumn(name = "recipeId", insertable = false, updatable = false)
	private List<Favorite> favorites;
	
}
