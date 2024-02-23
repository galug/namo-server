package com.example.namo2.domain.category.application.impl;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.CategoryStatus;
import com.example.namo2.domain.category.domain.Palette;
import com.example.namo2.domain.category.ui.dto.CategoryRequest;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getCategories(Long userId) {
        return categoryRepository.findCategoriesByUserIdAndStatusEquals(userId, CategoryStatus.ACTIVE);
    }

    public void delete(Long categoryId) {
        Category category = getCategory(categoryId);
        category.delete();
    }


    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
    }

    public Category modifyCategory(Long categoryId, CategoryRequest.PostCategoryDto dto, Palette palette) {
        Category category = getCategory(categoryId);
        category.update(dto.getName(), dto.isShare(), palette);
        return category;
    }

    public List<Category> getMoimUsersCategories(List<User> users) {
        return categoryRepository.findMoimCategoriesByUsers(users);
    }
}
