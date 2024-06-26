package com.example.namo2.domain.individual.application.converter;

import com.example.namo2.domain.individual.domain.Category;
import com.example.namo2.domain.individual.domain.constant.CategoryKind;
import com.example.namo2.domain.individual.domain.Palette;
import com.example.namo2.domain.individual.ui.dto.CategoryRequest;

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
			.kind(CategoryKind.CUSTOM)
			.build();
	}

	public static Category toCategory(
		String name,
		Palette palette,
		Boolean isShare,
		User user,
		CategoryKind kind
	) {
		return Category.builder()
			.name(name)
			.palette(palette)
			.share(isShare)
			.user(user)
			.kind(kind)
			.build();
	}
}
