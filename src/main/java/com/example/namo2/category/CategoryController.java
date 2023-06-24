package com.example.namo2.category;

import com.example.namo2.category.dto.CategoryDto;
import com.example.namo2.category.dto.CategoryIdRes;
import com.example.namo2.category.dto.PostCategoryReq;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Api(value = "Category")
public class CategoryController {
    private final CategoryService categoryService;

    @ResponseBody
    @PostMapping("")
    @ApiOperation(value = "카테고리 생성")
    public BaseResponse<CategoryIdRes> createCategory(@RequestBody PostCategoryReq postcategoryReq,
                                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        CategoryIdRes categoryIdRes = categoryService.create(userId, postcategoryReq);
        return new BaseResponse<>(categoryIdRes);
    }

    @ResponseBody
    @GetMapping("")
    @ApiOperation(value = "카테고리 조회")
    public BaseResponse<List<CategoryDto>> findAllCategory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<CategoryDto> categories = categoryService.findAll(userId);
        return new BaseResponse<>(categories);
    }

    @ResponseBody
    @PatchMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 수정")
    public BaseResponse<CategoryIdRes> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody PostCategoryReq postcategoryReq) {
        CategoryIdRes categoryIdRes = categoryService.update(categoryId, postcategoryReq);
        return new BaseResponse<>(categoryIdRes);
    }

    @ResponseBody
    @DeleteMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 삭제")
    public BaseResponse<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return new BaseResponse<>("삭제에 성공하셨습니다.");
    }

}
