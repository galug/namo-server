package com.example.namo2.domain.category.application.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.ui.dto.CategoryResponse;

public class CategoryResponseConverter {

	private CategoryResponseConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static CategoryResponse.CategoryIdDto toCategoryIdDto(Category category) {
		return new CategoryResponse.CategoryIdDto(category.getId());
	}

	public static List<CategoryResponse.CategoryDto> toCategoryDtoList(List<Category> categories) {
		return categories.stream()
			.map(CategoryResponseConverter::toCategoryDto)
			.collect(Collectors.toList());
	}

	public static CategoryResponse.CategoryDto toCategoryDto(Category category) {
		return new CategoryResponse.CategoryDto(
			category.getId(),
			category.getName(),
			category.getPalette().getId(),
			category.getShare()
		);
	}
}
