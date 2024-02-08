package com.example.namo2.domain.category;

import com.example.namo2.domain.category.dto.CategoryDto;
import com.example.namo2.domain.category.dto.CategoryIdRes;
import com.example.namo2.domain.category.dto.PostCategoryReq;
import com.example.namo2.global.config.response.BaseResponse;
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

    @Operation(summary = "카테고리 생성", description = "카테고리 생성 API")
    @PostMapping("")
    public BaseResponse<CategoryIdRes> createCategory(@RequestBody PostCategoryReq postcategoryReq,
                                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        CategoryIdRes categoryIdRes = categoryService.create(userId, postcategoryReq);
        return new BaseResponse<>(categoryIdRes);
    }

    @Operation(summary = "카테고리 조회", description = "카테고리 조회 API")
    @GetMapping("")
    public BaseResponse<List<CategoryDto>> findAllCategory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<CategoryDto> categories = categoryService.findAll(userId);
        return new BaseResponse<>(categories);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
    @PatchMapping("/{categoryId}")
    public BaseResponse<CategoryIdRes> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody PostCategoryReq postcategoryReq) {
        CategoryIdRes categoryIdRes = categoryService.update(categoryId, postcategoryReq);
        return new BaseResponse<>(categoryIdRes);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
    @DeleteMapping("/{categoryId}")
    public BaseResponse<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return new BaseResponse<>("삭제에 성공하셨습니다.");
    }

}
