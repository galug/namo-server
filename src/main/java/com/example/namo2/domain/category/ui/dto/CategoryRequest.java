package com.example.namo2.domain.category.ui.dto;

import jakarta.validation.constraints.NotNull;

import com.example.namo2.domain.category.validation.annotation.CategoryName;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryRequest {

	private CategoryRequest() {
		throw new IllegalStateException("Utility class");
	}

	@AllArgsConstructor
	@Getter
	public static class PostCategoryDto {
		@CategoryName
		private String name;
		@NotNull
		private Long paletteId;
		@NotNull
		private boolean isShare;
	}
}
