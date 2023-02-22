package com.example.namo2.category;

import com.example.namo2.category.dto.CategoryDto;
import com.example.namo2.category.dto.CategoryIdRes;
import com.example.namo2.category.dto.PostCategoryReq;
import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponse;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.schedule.dto.ScheduleIdRes;
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
    public BaseResponse<CategoryIdRes> createCategory(@RequestBody PostCategoryReq postcategoryReq) {
        try {
            CategoryIdRes categoryIdRes = categoryService.create(1L, postcategoryReq);
            return new BaseResponse<>(categoryIdRes);
        } catch (BaseException baseException) {
            System.out.println("baseException.getStatus() = " + baseException.getStatus());
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("")
    @ApiOperation(value = "카테고리 조회")
    public BaseResponse<List<CategoryDto>> findAllCategory() {
        try {
            List<CategoryDto> categories = categoryService.findAll(1L);
            return new BaseResponse<>(categories);
        } catch (BaseException baseException) {
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{category}")
    @ApiOperation(value = "카테고리 수정")
    public BaseResponse<CategoryIdRes> updateCategory(@PathVariable("category") Long categoryId, @RequestBody PostCategoryReq postcategoryReq) {
        try {
            CategoryIdRes categoryIdRes = categoryService.update(categoryId, postcategoryReq);
            return new BaseResponse<>(categoryIdRes);
        } catch (BaseException baseException) {
            System.out.println("baseException.getStatus() = " + baseException.getStatus());
            return new BaseResponse(baseException.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/{category}")
    @ApiOperation(value = "카테고리 삭제")
    public BaseResponse<String> deleteCategory(@PathVariable("category") Long categoryId) {
        try {
            categoryService.delete(categoryId);
            return new BaseResponse<>("삭제에 성공하셨습니다.");
        } catch (BaseException baseException) {
            System.out.println("baseException.getStatus() = " + baseException.getStatus());
            return new BaseResponse(baseException.getStatus());
        }
    }

}
