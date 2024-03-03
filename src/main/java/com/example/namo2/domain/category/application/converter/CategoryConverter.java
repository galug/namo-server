package com.example.namo2.domain.category.application.converter;

import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.Palette;
import com.example.namo2.domain.category.ui.dto.CategoryRequest;

import com.example.namo2.domain.user.domain.User;

public class CategoryConverter {

	private CategoryConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static Category toCategory(
		CategoryRequest.PostCategoryDto dto,
		User user,
		Palette palette
	) {
		return Category.builder()
			.name(dto.getName())
			.user(user)
			.palette(palette)
			.share(dto.isShare())
			.build();
	}

	public static Category toCategory(
		String name,
		Palette palette,
		Boolean isShare,
		User user
	) {
		return Category.builder()
			.name(name)
			.palette(palette)
			.share(isShare)
			.user(user)
			.build();
	}
}
