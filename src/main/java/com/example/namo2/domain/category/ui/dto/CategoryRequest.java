package com.example.namo2.domain.category.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryRequest {

	private CategoryRequest() {
		throw new IllegalStateException("Utility class");
	}

	@AllArgsConstructor
	@Getter
	public static class PostCategoryDto {
		private String name;
		private Long paletteId;
		private boolean isShare;
	}
}
