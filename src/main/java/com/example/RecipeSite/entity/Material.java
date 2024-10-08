package com.example.RecipeSite.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "materials")
@Data
@EqualsAndHashCode(callSuper = false)
public class Material extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "material_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "material_name", nullable = false, length = 30)
	private String materialName;
	
	@Column(nullable = false, length = 30)
	private String quantity;
	
	@ManyToOne
	@JoinColumn(name = "recipe_id")
	private Recipe recipe;

}
