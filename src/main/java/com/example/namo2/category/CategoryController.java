package com.example.namo2.category;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


}
