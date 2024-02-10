package com.example.namo2.domain.category.application.converter;

import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.ui.dto.CategoryResponse;

public class CategoryResponseConverter {

    private CategoryResponseConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static CategoryResponse.CategoryIdDto toCategoryIdDto(Category category) {
        return new CategoryResponse.CategoryIdDto(category.getId());
    }
}
