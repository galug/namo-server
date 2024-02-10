package com.example.namo2.domain.category.application;

import com.example.namo2.domain.category.application.converter.CategoryConverter;
import com.example.namo2.domain.category.application.converter.CategoryResponseConverter;
import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.application.impl.PaletteService;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.Palette;
import com.example.namo2.domain.category.ui.dto.CategoryRequest;
import com.example.namo2.domain.category.ui.dto.CategoryResponse;
import com.example.namo2.domain.user.UserService;
import com.example.namo2.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryFacade {
    private final CategoryService categoryService;
    private final PaletteService paletteService;
    private final UserService userService;

    public CategoryResponse.CategoryIdDto create(Long userId, CategoryRequest.PostCategoryDto dto) {
        User user = userService.getUser(userId);
        Palette palette = paletteService.getPalette(dto.getPaletteId());
        Category category = CategoryConverter.toCategory(dto, user, palette);
        Category savedCategory = categoryService.create(category);

        return CategoryResponseConverter.toCategoryIdDto(savedCategory);
    }

    public List<CategoryResponse.CategoryDto> getCategories(Long userId) {
        List<Category> categories = categoryService.getCategories(userId);

        return CategoryResponseConverter.toCategoryDtoList(categories);
    }
}
