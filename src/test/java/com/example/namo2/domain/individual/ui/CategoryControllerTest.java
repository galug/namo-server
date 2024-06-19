package com.example.namo2.domain.individual.ui;

import com.example.namo2.domain.individual.application.CategoryFacade;
import com.example.namo2.domain.individual.ui.dto.CategoryRequest;
import com.example.namo2.domain.individual.ui.dto.CategoryResponse;
import com.example.namo2.global.config.interceptor.AuthenticationInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryFacade categoryFacade;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void initTest() throws Exception {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("카테고리를 생성한다.")
    void createCategory() throws Exception {
        // given
        Long categoryId = 5L;
        CategoryRequest.PostCategoryDto request
                = new CategoryRequest.PostCategoryDto("새로운 일정", 1L, true);
        CategoryResponse.CategoryIdDto categoryIdDto = new CategoryResponse.CategoryIdDto(categoryId);

        given(categoryFacade.create(any(), any())).willReturn(categoryIdDto);

        // when, then
        mockMvc.perform(
                        post("/api/v1/categories")
                                .header(HttpHeaders.FROM, "localhost")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("요청 성공"))
                .andExpect(jsonPath("$.result.id").value(categoryId));
    }

    @Test
    @DisplayName("카테고리를 모두 찾는다.")
    void findAllCategory() throws Exception {
        // given
        Long paletteId = 1L;
        Long userId = 1L;
        List<CategoryResponse.CategoryDto> categoryDtos = new ArrayList<>();
        for (Long categoryId = 1L; categoryId < 3L; categoryId++) {
            CategoryResponse.CategoryDto categoryDto
                    = new CategoryResponse.CategoryDto(categoryId, "일정" + categoryId, paletteId, Boolean.TRUE);
            categoryDtos.add(categoryDto);
        }
        given(categoryFacade.getCategories(any())).willReturn(categoryDtos);

        // when, then
        mockMvc.perform(
                        get("/api/v1/categories")
                                .header(HttpHeaders.FROM, "localhost")
                ).andDo(print())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("요청 성공"))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(categoryDtos.size()))
                .andExpect(jsonPath("$.result[0].categoryId").value(categoryDtos.get(0).getCategoryId()))
                .andExpect(jsonPath("$.result[0].name").value(categoryDtos.get(0).getName()))
                .andExpect(jsonPath("$.result[0].paletteId").value(categoryDtos.get(0).getPaletteId()))
                .andExpect(jsonPath("$.result[0].isShare").value(categoryDtos.get(0).getIsShare()))
                .andExpect(jsonPath("$.result[1].categoryId").value(categoryDtos.get(1).getCategoryId()))
                .andExpect(jsonPath("$.result[1].name").value(categoryDtos.get(1).getName()))
                .andExpect(jsonPath("$.result[1].paletteId").value(categoryDtos.get(1).getPaletteId()))
                .andExpect(jsonPath("$.result[1].isShare").value(categoryDtos.get(1).getIsShare()));
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategory() throws Exception {
        Long categoryId = 5L;
        CategoryRequest.PostCategoryDto request
                = new CategoryRequest.PostCategoryDto("새로운 일정", 1L, true);
        CategoryResponse.CategoryIdDto categoryIdDto = new CategoryResponse.CategoryIdDto(categoryId);
        given(categoryFacade.modifyCategory(any(), any(), any())).willReturn(categoryIdDto);

        // when, then
        mockMvc.perform(
                        patch("/api/v1/categories/{categoryId}", categoryId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("요청 성공"))
                .andExpect(jsonPath("$.result.id").value(categoryId));
    }

    @Test
    void deleteCategory() {
        Long categoryId = 5L;
    }
}
