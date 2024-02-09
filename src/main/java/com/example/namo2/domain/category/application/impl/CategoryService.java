package com.example.namo2.domain.category.application.impl;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.dao.repository.PaletteRepository;
import com.example.namo2.domain.category.ui.dto.CategoryDto;
import com.example.namo2.domain.category.ui.dto.CategoryIdRes;
import com.example.namo2.domain.category.ui.dto.PostCategoryReq;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.Palette;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.category.domain.CategoryStatus;
import com.example.namo2.domain.schedule.ScheduleRepository;
import com.example.namo2.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_PALETTE_FAILURE;
import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PaletteRepository paletteRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userDao;

    @Transactional(readOnly = false)
    public CategoryIdRes create(Long userId, PostCategoryReq postcategoryReq) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Palette palette = paletteRepository.findById(postcategoryReq.getPaletteId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        Category category = Category.builder()
                .name(postcategoryReq.getName())
                .user(user)
                .palette(palette)
                .share(postcategoryReq.isShare())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return new CategoryIdRes(savedCategory.getId());
    }

    public List<CategoryDto> findAll(Long userId) throws BaseException {
        List<Category> categories = categoryRepository.findCategoriesByUserIdAndStatusEquals(userId, CategoryStatus.ACTIVE);
        return categories.stream()
                .map((category) -> new CategoryDto(category.getId(), category.getName(),
                        category.getPalette().getId(), category.getShare()))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = false)
    public CategoryIdRes update(Long categoryId, PostCategoryReq postcategoryReq) throws BaseException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        Palette palette = paletteRepository.findById(postcategoryReq.getPaletteId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        category.update(postcategoryReq.getName(), postcategoryReq.isShare(), palette);
        return new CategoryIdRes(category.getId());
    }

    @Transactional(readOnly = false)
    public void delete(Long categoryId) throws BaseException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        category.delete();
    }
}
