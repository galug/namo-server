package com.example.namo2.domain.category.application.impl;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.dao.repository.PaletteRepository;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.CategoryStatus;
import com.example.namo2.domain.category.domain.Palette;
import com.example.namo2.domain.category.ui.dto.CategoryRequest;
import com.example.namo2.domain.category.ui.dto.CategoryResponse;
import com.example.namo2.domain.schedule.ScheduleRepository;
import com.example.namo2.domain.user.UserRepository;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PaletteRepository paletteRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userDao;

    public CategoryResponse.CategoryIdDto create(Long userId, CategoryRequest.PostCategoryDto postcategoryDto) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Palette palette = paletteRepository.findById(postcategoryDto.getPaletteId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        Category category = Category.builder()
                .name(postcategoryDto.getName())
                .user(user)
                .palette(palette)
                .share(postcategoryDto.isShare())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse.CategoryIdDto(savedCategory.getId());
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public List<CategoryResponse.CategoryDto> findAll(Long userId) throws BaseException {
        List<Category> categories = categoryRepository.findCategoriesByUserIdAndStatusEquals(userId, CategoryStatus.ACTIVE);
        return categories.stream()
                .map((category) -> new CategoryResponse.CategoryDto(category.getId(), category.getName(),
                        category.getPalette().getId(), category.getShare()))
                .collect(Collectors.toList());
    }


    public CategoryResponse.CategoryIdDto update(Long categoryId, CategoryRequest.PostCategoryDto postcategoryDto) throws BaseException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        Palette palette = paletteRepository.findById(postcategoryDto.getPaletteId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        category.update(postcategoryDto.getName(), postcategoryDto.isShare(), palette);
        return new CategoryResponse.CategoryIdDto(category.getId());
    }

    public void delete(Long categoryId) throws BaseException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        category.delete();
    }


}
