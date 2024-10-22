package com.example.RecipeSite.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@MappedSuperclass
@Data
public class AbstractEntity {
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "updated_at")
	private  Date updatedAt;
	
	/**
	 * データ登録前に共通で実行されるメソッド
	 */
	@PrePersist
	public void onPrePersist() {
		Date date = new Date();
		setCreatedAt(date);
		setUpdatedAt(date);
	}

	 /**
	  * データ更新前に共通で実行されるメソッド
	  */
	@PreUpdate
	public  void onPreUpdate() {
		setUpdatedAt(new Date());
	}

}
