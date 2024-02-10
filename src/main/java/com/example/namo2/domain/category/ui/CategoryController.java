package com.example.namo2.domain.category.ui;

import com.example.namo2.domain.category.application.CategoryFacade;
import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.ui.dto.CategoryRequest;
import com.example.namo2.domain.category.ui.dto.CategoryResponse;
import com.example.namo2.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "카테고리 관련 API")
@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryFacade categoryFacade;

    @Operation(summary = "카테고리 생성", description = "카테고리 생성 API")
    @PostMapping("")
    public BaseResponse<CategoryResponse.CategoryIdDto> createCategory(@RequestBody CategoryRequest.PostCategoryDto postcategoryDto,
                                                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        CategoryResponse.CategoryIdDto categoryIdDto = categoryFacade.create(userId, postcategoryDto);
        return new BaseResponse<>(categoryIdDto);
    }

    @Operation(summary = "카테고리 조회", description = "카테고리 조회 API")
    @GetMapping("")
    public BaseResponse<List<CategoryResponse.CategoryDto>> findAllCategory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<CategoryResponse.CategoryDto> categories = categoryService.findAll(userId);
        return new BaseResponse<>(categories);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
    @PatchMapping("/{categoryId}")
    public BaseResponse<CategoryResponse.CategoryIdDto> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryRequest.PostCategoryDto postcategoryDto) {
        CategoryResponse.CategoryIdDto categoryIdDto = categoryService.update(categoryId, postcategoryDto);
        return new BaseResponse<>(categoryIdDto);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
    @DeleteMapping("/{categoryId}")
    public BaseResponse<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return new BaseResponse<>("삭제에 성공하셨습니다.");
    }

}
