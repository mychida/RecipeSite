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
@Table(name = "processes")
@Data
@EqualsAndHashCode(callSuper = false)
public class Process extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "process_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "process_number", nullable = false)
	private int processNumber;
	
	@Column(nullable = false, length = 500)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "recipe_id")
	private Recipe recipe;

}
